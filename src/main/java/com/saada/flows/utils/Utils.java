package com.saada.flows.utils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;
import org.json.XML;

/**
 * Utils
 */
public final class Utils {

    private Utils() {
    }

    public static String prelogString(String identifier, int lineNumber, String logMessage) {
        return " | LINE >> " + lineNumber + " | IDENTIFIER >> " + identifier + " | MESSAGE >> " + logMessage;
    }

    /**
     * getCodelineNumber
     *
     * @return lineNumber
     */
    public static int getCodelineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    /**
     * Convert json string to json Object
     *
     * @param jsonstring
     * @return
     */
    public static JSONObject stringToJson(String jsonstring) {
        return new JSONObject(jsonstring);
    }

    /**
     * Replace all newline and tab spaces in a string
     *
     * @param input
     * @return
     */
    public static String rmNewlineTab(String input) {
        return input.replace("\n", "").replace("\r", "").replace("\t", "");
    }

    /**
     * Convert xml string to JsonObject
     *
     * @param xmlString
     * @return
     */
    public static JSONObject xmlToJson(String xmlString) {
        return XML.toJSONObject(xmlString);
    }

    /**
     * Read file
     *
     * @param path
     * @param encoding Default encoding is UTF-8
     * @return
     * @throws IOException
     */
    public static String readFile(String path) {
        byte[] encoded = null;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Path not found : " + e.getMessage());
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }
    // StandardCharsets.UTF_8

    /**
     * Convert current datetime to date and time String array
     *
     * @return
     */
    public static String[] dateToString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        return reportDate.split("\\s+");
    }

    /**
     * Convert current datetime to date and time String array
     *
     * @return
     */
    public static String curdateToString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);

    }

    public static int randomGen(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String postRequest(String url, String payload, HashMap<String, String> headers)
            throws ClientProtocolException, IOException {

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);

        StringEntity entity = new StringEntity(payload, StandardCharsets.UTF_8);

        for (String header : headers.keySet()) {
            httppost.addHeader(header, headers.get(header));
        }

        httppost.setEntity(entity);
        

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        CloseableHttpResponse responseBody = httpclient.execute(httppost);

        String body = responseHandler.handleResponse(responseBody);

        httpclient.close();
        return body;

    }

    public static String getRequest(String url, HashMap<String,String> headers)
            throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);

        for (String header : headers.keySet()) {
            httpGet.addHeader(header, headers.get(header));
        }

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        CloseableHttpResponse responseBody = httpclient.execute(httpGet);

        String body = responseHandler.handleResponse(responseBody);

        return body;


    }


    public static void patchRequest(String url, HashMap<String,String> headers, String payload) throws Exception{
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

}
