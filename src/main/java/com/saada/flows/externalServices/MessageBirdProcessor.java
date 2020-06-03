package com.saada.flows.externalServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.saada.flows.models.MessageBirdMessage;
import com.saada.flows.services.FlowService;
import com.saada.flows.services.SessionService;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MessageBirdProcessor {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private FlowService flowService;

    @Value("${message.bird.baseurl}")
    private String conversationBaseUrl;

    @Value("${message.bird.accesskey}")
    private String accesskey;

    // @Autowired
    // private ServiceCodeService serviceCodeService;

    @Async("threadPoolTaskExecutor")
    public void sendRequest(String request, String urlStr, HashMap<String, String> headers, String method)
            throws MalformedURLException, IOException {

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        if (method.equalsIgnoreCase("PATCH")) {
            con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        }
        con.setRequestMethod(method);

        headers.keySet().forEach((header) -> {
            con.setRequestProperty(header, headers.get(header));
        });
        con.setDoOutput(true);
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = request.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // int status = con.getResponseCode();

        StringBuilder response = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {

            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        con.disconnect();
    }

    public void patchRequest(String url, HashMap<String,String> headers, String payload) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(new URI(url));
        
        StringEntity entity = new StringEntity(payload, StandardCharsets.UTF_8);

        for (String header : headers.keySet()) {
            httpPatch.addHeader(header, headers.get(header));
        }
        httpPatch.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPatch);
        response.close();
    }

    public void coreProcessor(MessageBirdMessage message) {

        HashMap<String, String> headers = new HashMap<>();
        JSONObject request = new JSONObject();

        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.put("Authorization", "AccessKey " + accesskey);

        request.put("type", "text");

        String session = message.getConversation().getId();
        String input = message.getMessage().getContent().get("text").toString();
        if (message.getType().equalsIgnoreCase("message.created")
                && flowService.isAvailable(message.getMessage().getTo())) {

            String url = conversationBaseUrl + message.getConversation().getId() + "/messages";

            String response = sessionService.screenNavigate(session, input, message.getMessage().getTo(),
                    message.getContact().getMsisdn());

            request.put("content", new JSONObject().put("text", response.substring(3)));

            try {

                if (response.startsWith("END")) {
                    String urlend = conversationBaseUrl + message.getConversation().getId();
                    patchRequest(urlend, headers, new JSONObject().put("status", "archived").toString());
                }
                sendRequest(request.toString(), url, headers, "POST");
            } catch (Exception e) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + e.getLocalizedMessage()
                        + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        }
    }
}