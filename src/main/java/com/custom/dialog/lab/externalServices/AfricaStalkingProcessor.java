/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.externalServices;

import com.custom.dialog.lab.services.SessionProcessor;
import com.custom.dialog.lab.utils.Props;
import com.google.cloud.firestore.Firestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jovixe
 */
@Service
public class AfricaStalkingProcessor extends SessionProcessor {

    private static final Props SETTINGS = new Props();

    private final static Logger LOGGER = Logger.getLogger(AfricaStalkingProcessor.class.getName());

    @Autowired
    Firestore firestore;

    public AfricaStalkingProcessor() {
    }

    public AfricaStalkingProcessor(String phoneNumber, String sessionId, String input, HashMap<String, Object> extraData) {
        super(phoneNumber, sessionId, input, extraData);
    }

    @Override
    public String displayText(Map<String, Object> screenData) {

        if (screenData.isEmpty()) {
            return "END "+SETTINGS.getFlowError("3");
        }

        if (!this.getErrors().isEmpty()) {
            return "END "+this.getErrors().get(0);
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
        return "CON "+dynamicText(screenText);
    }
}
