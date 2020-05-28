package com.saada.flows.externalServices;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MessageBirdProcessor {

    @Async("threadPoolTaskExecutor")
    public void sendRequest(String request, String urlStr, HashMap<String, String> headers,String method)
            throws MalformedURLException, IOException {

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
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

        int status = con.getResponseCode();

        System.out.println(status);
        StringBuilder response = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {

            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        con.disconnect();
        System.out.println("===================================================ASYNC END========================================================");
        // return response.toString();
    }
}