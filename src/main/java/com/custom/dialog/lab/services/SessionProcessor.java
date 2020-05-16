/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.services;

import com.custom.dialog.lab.utils.Props;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author jovixe
 */
public class SessionProcessor {

    private String phoneNumber;

    private String sessionId;

    private String input;

    private HashMap<String, Object> extraData;

    private List<String> errors = new ArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(SessionProcessor.class.getName());
    private static final Props SETTINGS = new Props();

    private String exit_message = new String();

    public SessionProcessor() {
    }

    public SessionProcessor(String phoneNumber, String sessionId, String input, HashMap<String, Object> extraData) {
        this.phoneNumber = phoneNumber;
        this.sessionId = sessionId;
        this.input = input;
        this.extraData = extraData;
    }

    public HashMap<String, Object> getExtraData() {
        return extraData;
    }

    public String getInput() {
        return input;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setExtraData(HashMap<String, Object> extraData) {
        this.extraData = extraData;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Map<String,String> screenNavigate(Map<String, Object> sessionData) {
        
        
        HashMap<String, String> response = new HashMap<>();
        // means it is the first screen
        if (sessionData == null || sessionData.isEmpty()) {

            response.put("shortCode", input);
            response.put("nextScreen", "start_page");

            // capture 1st input
            extraData.put("start_page", input);
            return response;

        }

        Map<String, Object> currentScreenData = (Map<String, Object>) sessionData.get("screenNode");
        String screenType = String.valueOf(currentScreenData.get("screenType"));
        String shortCode = String.valueOf(currentScreenData.get("shortCode"));

        extraData = (HashMap<String, Object>) sessionData.get("screenNode");

        String currentNode = String.valueOf(currentScreenData.get("nodeName"));

        // session already existed
        if ("raw_input".equalsIgnoreCase(screenType)) {
            String screenNext = currentScreenData.get("screenNext").toString();
            response.put("shortCode", shortCode);
            response.put("nextScreen", screenNext);
            extraData.put(currentNode, input);
            return response;

        } else if ("options".equalsIgnoreCase(screenType)) {
            String screenNext = currentScreenData.get("screenNext").toString();
            response.put("shortCode", shortCode);
            response.put("nextScreen", screenNext);
            List<String> nodeOptions = (List<String>) currentScreenData.get("nodeOptions");
            String selection = nodeOptions.get(Integer.parseInt(input) - 1);
            extraData.put(currentNode, selection);
            return response;

        } else if ("items".equalsIgnoreCase(screenType)) {
            List<HashMap<String, String>> nodeItems = (List<HashMap<String, String>>) currentScreenData.get("nodeItems");
            String nextScreen;
            try {
                nextScreen = nodeItems.get(Integer.parseInt(input) - 1).get("nextScreen");
                String selection = nodeItems.get(Integer.parseInt(input) - 1).get("displayText");
                extraData.put(currentNode, selection);
            } catch (Exception ex) {
                // Invalid input
                errors.add(SETTINGS.getFlowError("2"));
                return new HashMap();

            }
            response.put("shortCode", shortCode);
            response.put("nextScreen", nextScreen);
            return response;

        } else {
            errors.add(SETTINGS.getFlowError("1"));
            return new HashMap();
        }
    }

    public String displayText(Map<String, Object> screenData) {

        if (screenData.isEmpty()) {
            return SETTINGS.getFlowError("3");
        }

        if (!errors.isEmpty()) {
            return errors.get(0);
        }

        Map<String, Object> retrievedScreen = (Map<String, Object>) screenData.get("screenNode");

        String screenType = retrievedScreen.get("screenType").toString();
        String screenText = retrievedScreen.get("screenText").toString();
        if ("items".equalsIgnoreCase(screenType)) {

            List<HashMap<String, String>> nodeItems = (List<HashMap<String, String>>) retrievedScreen.get("nodeItems");
            int count = 1;
            for (HashMap item : nodeItems) {
                screenText += "\n" + count + ". " + String.valueOf(item.get("displayText"));
                count++;
            }
        } else if ("options".equalsIgnoreCase(screenType)) {
            int count = 1;
            List<String> nodeOptions = (List<String>) retrievedScreen.get("nodeOptions");

            for (String opt : nodeOptions) {
                screenText += "\n" + count + ". " + opt;
                count++;
            }
        }
        return dynamicText(screenText);
    }

    public String dynamicText(String screenText) {
        if (!screenText.contains("^")) {
            return screenText;
        }
        for (String key : extraData.keySet()) {
            String placeholder = "^" + key + "^";
            if (screenText.contains(placeholder)) {
                screenText = screenText.replace(placeholder, extraData.get(key).toString());
            }
        }

        if (screenText.contains("^")) {
            return SETTINGS.getFlowError("6");
        }

        return screenText;
    }

    public HashMap<String, Object> prepareArchiveSessions(String status, String serviceCode) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("sessionId", sessionId);
        payload.put("serviceCode", serviceCode);
        payload.put("status", status);
        payload.put("phoneNumber", phoneNumber);
        return payload;
    }

    public HashMap<String, Object> prepareArchiveJourney(String displayText, String userInput) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("displayText", displayText);
        payload.put("userInput", userInput);
        return payload;
    }

    public Map getNextScreenDetails(Map flow, String nextScreen) {

        if (flow.isEmpty()) {
            errors.add("Menu Flow Not Found");
            return new HashMap();
        }

        if (!flow.containsKey(nextScreen)) {
            // document/screen does not exist
            errors.add("Screen not available in the flow");
            return new HashMap();
        }

        HashMap<String, Object> savableObj = new HashMap<>();
        savableObj.put("phoneNumber", phoneNumber);
        savableObj.put("screenNode", flow.get(nextScreen));
        savableObj.put("sessionId", sessionId);
        savableObj.put("extraData", extraData);
        return savableObj;
    }

    public List<String> getErrors() {
        return errors;
    }

}
