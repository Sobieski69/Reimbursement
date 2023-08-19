package org.example;

import org.example.adminModels.ClaimParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ReimbursementApp {
    Context context = ContextFactory.getContext();
    RequestHandler requestHandler = new RequestHandler();
    BufferedReader br;

    public void run() {
        context.claimParams = new ClaimParams();
        context.claimParams.initDefaultParams();
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is on.");
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    br = getBufferedReader(client);
                    String firstLine = getRequestFirstLine(client);
                    if (readRequestVerb(firstLine).equals("POST") && readResourceLocator(firstLine).equals("/data")) {
                        try {
                            String requestPayload = getClientRequestPayload(br);
                            requestHandler.handleClientClaimPostRequestPayload(client, requestPayload, context.claimParams);
                        } catch (Exception e) {
                            requestHandler.sendClientError(client, "Sorry this is our fault. Better call Saul: 555-555-555");
                        }
                    } else if (readRequestVerb(firstLine).equals("GET") && readResourceLocator(firstLine).equals("/")) {
                        try {
                            requestHandler.sendClientWebPage(client, FileUtil.FileType.htmlPage);
                        } catch (Exception e) {
                            requestHandler.sendClientError(client, "Sorry this is our fault. Better call Saul: 555-555-555");
                        }

                    } else if (readRequestVerb(firstLine).equals("GET") && readResourceLocator(firstLine).equals("/receiptMenu")) {
                        try {
                            requestHandler.handleGetRequest(client, requestHandler.generateDropdownResponseJson(context.claimParams));

                        } catch (Exception e) {
                            requestHandler.sendClientError(client, "Sorry this is our fault. Better call Saul: 555-555-555");
                        }
                    } else if (readRequestVerb(firstLine).equals("GET") && readResourceLocator(firstLine).equals("/script")) {
                        try {
                            requestHandler.sendClientWebPage(client, FileUtil.FileType.script);
                        } catch (Exception e) {
                            requestHandler.sendClientError(client, "Sorry this is our fault. Better call Saul: 555-555-555");
                        }
                    } else if (readRequestVerb(firstLine).equals("GET") && readResourceLocator(firstLine).equals("/admin")) {
                        try {
                            requestHandler.sendClientWebPage(client, FileUtil.FileType.adminPage);
                        } catch (Exception e) {
                            requestHandler.sendClientError(client, "Sorry this is our fault. Better call Saul: 555-555-555");
                        }
                    } else if (readRequestVerb(firstLine).equals("PATCH") && readResourceLocator(firstLine).equals("/admin")) {
                        try {
                            String requestPayload = getClientRequestPayload(br);
                            context.claimParams = requestHandler.handlePatchRequestPayload(client, requestPayload, context.claimParams);
                        } catch (Exception e) {
                            requestHandler.sendClientError(client, "Sorry this is our fault. Better call Saul: 555-555-555");
                        }
                    } else if (readRequestVerb(firstLine).equals("GET") && readResourceLocator(firstLine).equals("/styles")) {
                        try {
                            requestHandler.sendClientWebPage(client, FileUtil.FileType.styles);
                        } catch (Exception e) {
                            requestHandler.sendClientError(client, "Sorry this is our fault. Better call Saul: 555-555-555");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getRequestFirstLine(Socket client) {
        String requestHeader = readRequestHeader(client);
        return readFirstLineOfRequestHeader(requestHeader);
    }

    private BufferedReader getBufferedReader(Socket client) throws IOException {
        BufferedReader br;
        InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
        br = new BufferedReader(inputStreamReader);
        return br;
    }


    private String readRequestVerb(String firstLineOfRequestHeader) {
        return firstLineOfRequestHeader.split(" ")[0];
    }

    private String readResourceLocator(String firstLineOfRequestHeader) {
        return firstLineOfRequestHeader.split(" ")[1];
    }

    private String readFirstLineOfRequestHeader(String requestHeader) {
        return requestHeader.split("\n")[0];
    }

    private String readRequestHeader(Socket client) {

        StringBuilder requestHeader = new StringBuilder();
        try {
            String line;
            line = br.readLine();
            while (!line.isBlank()) {
                requestHeader.append(line + "\r\n");
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            requestHandler.sendClientError(client, "Problem processing request, please try again");
        }
        return requestHeader.toString();
    }

    private String getClientRequestPayload(BufferedReader br) {
        String requestPayload = "";
        try {
            StringBuilder sb = new StringBuilder();
            while (br.ready()) {
                sb.append((char) br.read());
            }
            requestPayload = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestPayload;
    }
}
