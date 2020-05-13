/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.services;

import com.custom.dialog.lab.utils.Props;
import com.custom.dialog.lab.utils.Utils;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jovixe
 */
@Service
public class SessionProcessor {

    @Autowired
    Firestore firestore;

    private String phoneNumber;

    private String sessionId;

    private String input;

    private HashMap<String, Object> extraData;

    private List<String> errors = new ArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(SessionProcessor.class.getName());
    private static final Props SETTINGS = new Props();

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

    public Map screenNavigate() {

        DocumentSnapshot ss_snapshot = null;
        try {
            ss_snapshot = firestore.collection("sessions").document(sessionId).get().get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(SessionProcessor.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap();
        }
        Map<String, Object> sessionData = (Map<String, Object>) ss_snapshot.getData();
        HashMap<String, String> response = new HashMap<>();
        // means it is the first screen
        if (sessionData == null || sessionData.isEmpty()) {
            response.put("shortCode", input);
            response.put("nextScreen", "start_page");
            return response;

        }

        Map<String, Object> currentScreenData = (Map<String, Object>) sessionData.get("screenNode");
        String screenType = String.valueOf(currentScreenData.get("screenType"));
        String shortCode = String.valueOf(currentScreenData.get("shortCode"));

        // session already existed
        if ("raw_input".equalsIgnoreCase(screenType) || "options".equalsIgnoreCase(screenType)) {
            String screenNext = currentScreenData.get("screenNext").toString();
            response.put("shortCode", shortCode);
            response.put("nextScreen", screenNext);
            return response;

        } else if ("items".equalsIgnoreCase(screenType)) {
            List<HashMap<String, String>> nodeItems = (List<HashMap<String, String>>) currentScreenData.get("nodeItems");
            String nextScreen;
            try {
                nextScreen = nodeItems.get(Integer.parseInt(input) - 1).get("nextScreen");
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
        
        
        LOGGER.log(Level.INFO,
                Utils.prelogString(sessionId,
                        Utils.getCodelineNumber(), "Data submitted to function :: " + screenData),
                screenData);
        if (screenData.isEmpty()) {
            LOGGER.log(Level.SEVERE,
                    Utils.prelogString(sessionId,
                            Utils.getCodelineNumber(), "failed database transaction or invalid screen type"),
                    screenData);
            return SETTINGS.getFlowError("3");
        }
        try {
            firestore.collection("sessions").document(sessionId).set(screenData).get();
        } catch (InterruptedException  | ExecutionException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return SETTINGS.getFlowError("3");
        }

        if (!errors.isEmpty()) {
            return errors.get(0);
        }

        Map<String, Object> retrievedScreen = (Map<String, Object>) screenData.get("screenNode");

        String screenType = retrievedScreen.get("screenType").toString();
        String screenText = retrievedScreen.get("screenText").toString();
        if ("raw_input".equalsIgnoreCase(screenType)) {
            return screenText;
        } else if ("items".equalsIgnoreCase(screenType)) {

            List<HashMap<String, String>> nodeItems = (List<HashMap<String, String>>) retrievedScreen.get("nodeItems");
            int count = 1;
            for (HashMap item : nodeItems) {
                screenText += "\n" + count + ". " + String.valueOf(item.get("displayText"));
                count++;
            }
            return screenText;
        } else {
            int count = 1;
            List<String> nodeOptions = (List<String>) retrievedScreen.get("nodeOptions");

            for (String opt : nodeOptions) {
                screenText += "\n" + count + ". " + opt;
                count++;
            }

            return screenText;
        }

    }

    public Map getNextScreenDetails(String shortcode, String nextScreen) {

        
        DocumentSnapshot sc_snapshot;
        try {
            sc_snapshot = firestore.collection("menus").document(shortcode).get().get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(SessionProcessor.class.getName()).log(Level.SEVERE, null, ex);
            errors.add("Error fetching menu");
            return new HashMap();
        } 
        Map flow = sc_snapshot.getData();
        
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
