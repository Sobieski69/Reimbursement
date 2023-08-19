package org.example.adminModels;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "mileageRate",
        "dailyAllowance",
        "receiptParams"
})
@Generated("jsonschema2pojo")
public class ClaimParams {

    @JsonProperty("mileageRate")
    private BigDecimal mileageRate;
    @JsonProperty("dailyAllowance")
    private BigDecimal dailyAllowance;
    @JsonProperty("receiptParams")
    private List<ReceiptParams> receiptParams;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<>();

    @JsonProperty("mileageRate")
    public BigDecimal getMileageRate() {
        return mileageRate;
    }

    @JsonProperty("mileageRate")
    public void setMileageRate(String mileageRate) {
        this.mileageRate = new BigDecimal(mileageRate);
    }

    @JsonProperty("dailyAllowance")
    public BigDecimal getDailyAllowance() {
        return dailyAllowance;
    }

    @JsonProperty("dailyAllowance")
    public void setDailyAllowance(String dailyAllowance) {
        this.dailyAllowance = new BigDecimal(dailyAllowance);
    }

    @JsonProperty("receiptParams")
    public List<ReceiptParams> getReceiptParams() {
        return receiptParams;
    }

    @JsonProperty("receiptParams")
    public void setReceiptParams(List<ReceiptParams> receiptParams) {
        this.receiptParams = receiptParams;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ClaimParams initDefaultParams() {
        this.mileageRate = new BigDecimal("0.3");
        this.dailyAllowance = new BigDecimal("15");

        this.receiptParams = new ArrayList<>();
        this.receiptParams.add(new ReceiptParams("Hotel", BigDecimal.valueOf(100)));
        this.receiptParams.add(new ReceiptParams("Taxi", BigDecimal.valueOf(100)));
        this.receiptParams.add(new ReceiptParams("Airplane", BigDecimal.valueOf(100)));
        this.receiptParams.add(new ReceiptParams("Train", BigDecimal.valueOf(100)));
        this.receiptParams.add(new ReceiptParams("Restaurant", BigDecimal.valueOf(100)));

        return this;
    }

}