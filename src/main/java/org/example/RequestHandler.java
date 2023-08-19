package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.adminModels.ClaimParams;
import org.example.adminModels.ReceiptParams;
import org.example.models.Receipt;
import org.example.models.ResponseModel;
import org.example.models.UserData;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class RequestHandler {
    Context context;

    public RequestHandler() {
        this.context = ContextFactory.getContext();
    }

    public String handleClientClaimPostRequestPayload(Socket client, String requestPayload, ClaimParams claimParams) {
        try {
            String id = addUserJsonToDataStore(context.dataStore, jsonToPojo(requestPayload));
            String responsePayload = createResponse("clientSuccess",
                    calculateReimbursement(context, id, claimParams));
            sendClientResponse(client, responsePayload);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String createResponse(String responseType, List<String> dataList) throws JsonProcessingException {
        ResponseModel responseModel = new ResponseModel(responseType, dataList);
        return new ObjectMapper().writeValueAsString(responseModel);
    }

    public String generateDropdownResponseJson(ClaimParams claimParams) throws JsonProcessingException {
        List<String> dropdownItems = getDropdownList(claimParams);
        return createResponse("receiptDropdown", dropdownItems);
    }

    private List<String> getDropdownList(ClaimParams claimParams) {
        return claimParams.getReceiptParams().stream()
                .map(e -> e.getReceiptType())
                .collect(Collectors.toList());
    }


    public ClaimParams handlePatchRequestPayload(Socket client, String requestPayload, ClaimParams existingClaimParams) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ClaimParams updatedClaimParams = objectMapper.readValue(requestPayload, ClaimParams.class);
            List<String> responsePayload = new ArrayList<>();
            responsePayload.add("saved");

            String payloadToSend = createResponse("adminSuccess", responsePayload);
            sendClientResponse(client, payloadToSend);
            return updatedClaimParams;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existingClaimParams.initDefaultParams();

    }

    public String addUserJsonToDataStore(Map<String, UserData> dataStore, UserData userJson) {
        String id = UUID.randomUUID().toString();

        dataStore.put(id, userJson);
        return id;
    }

    public void sendClientResponse(Socket client, String requestPayload) {
        try {
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("Content-Type: application/json\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write((requestPayload).getBytes());
            clientOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendClientError(Socket client, String errorMessage) throws RuntimeException{
        try {
            List<String> errorMessageList = new ArrayList<>();
            errorMessageList.add(errorMessage);
            String responsePayload = createResponse("error", errorMessageList);

            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 400 Bad Request\r\n").getBytes());
            clientOutput.write(("Content-Type: application/json\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write((responsePayload).getBytes());
            clientOutput.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserData jsonToPojo(String userPayloadJson) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        UserData userData = objectMapper.readValue(userPayloadJson, UserData.class);
        return userData;

    }

    //TODO: move to business logic class
    public List<String> calculateReimbursement(Context context, String id, ClaimParams claimParams) {
        MathContext mathContext = new MathContext(2);
        UserData dataToCalculate = context.dataStore.get(id);
        BigDecimal sum = new BigDecimal("0");
        BigDecimal days = BigDecimal.valueOf(dataToCalculate.getDays());
        BigDecimal dailyAllowance = claimParams.getDailyAllowance();
        BigDecimal maxValue = days.multiply(dailyAllowance);

        BigDecimal calculatedValue;
        BigDecimal mileage = dataToCalculate.getMileage();
        BigDecimal mileageRate = claimParams.getMileageRate();

        sum = sum.add(mileage.multiply(mileageRate), mathContext);
        List<Receipt> receipts = dataToCalculate.getReceipts();

        sum = calculateReceiptsRespectingLimits(claimParams, sum, days, receipts);

        if (sum.compareTo(maxValue) == 1) {
            calculatedValue = maxValue;
        } else {
            calculatedValue = sum;
        }

        List<String> result = new ArrayList<>();
        result.add(String.valueOf(calculatedValue));
        return result;
    }

    private static BigDecimal calculateReceiptsRespectingLimits(ClaimParams claimParams, BigDecimal sum, BigDecimal days, List<Receipt> receipts) throws NoSuchElementException {
        for (Receipt receipt : receipts) {
            BigDecimal receiptDailyAllowance = claimParams.getReceiptParams().stream()
                    .filter(rp -> rp.getReceiptType().equals(receipt.getReceiptType()))
                    .findFirst()
                    .map(ReceiptParams::getValue)
                    .orElseThrow();

            if (receipt.getValue().multiply(days).compareTo(receiptDailyAllowance.multiply(days)) == -1) {
                sum = sum.add(receipt.getValue());
            } else {
                sum = sum.add(receiptDailyAllowance);
            }
        }
        return sum;
    }

    public UserData getUserDataById(String id) {
        return context.dataStore.get(id);
    }

    public void handleGetRequest(Socket client, String payload) {
        sendClientResponse(client, payload);
    }

    public void sendClientWebPage(Socket client, FileUtil.FileType fileType) throws IOException {
        try {
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(FileUtil.readFromFile(fileType).getBytes());
            clientOutput.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
