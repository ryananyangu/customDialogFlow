/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.controller;

import com.custom.dialog.lab.externalServices.AfricaStalkingProcessor;
import com.custom.dialog.lab.services.SessionProcessor;
import com.custom.dialog.lab.utils.Props;
import com.custom.dialog.lab.utils.Utils;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jovixe
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public", produces = "application/x-www-form-urlencoded;charset=UTF-8")
public class ExternalProcessors {

    @Autowired
    Firestore firestore;

    private static final Props SETTINGS = new Props();

    @PostMapping(path = "/atussd", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String sessionNavigator(@RequestParam Map<String, String> session) {
//        System.out.println(session+ " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            // phoneNumber=%2B254702079491&serviceCode=%2A384%2A11469%23&text=&sessionId=ATUid_1c1e09269ff2b366033529d495e083e5&networkCode=99999
            
            String input = session.get("serviceCode");
            if (!session.get("text").isEmpty()) {
                String[] inputs = session.get("text").split("\\*");
                input = inputs[inputs.length - 1];
            }
            
            DocumentSnapshot ss_snapshot = firestore.collection("sessions").document(session.get("sessionId")).get().get();
            AfricaStalkingProcessor session_menu = new AfricaStalkingProcessor(session.get("phoneNumber"), session.get("sessionId"), input, new HashMap<>());
            
            Map data = session_menu.screenNavigate((Map<String, Object>) ss_snapshot.getData());
            
            if (data.isEmpty()) {
                return "END "+SETTINGS.getFlowError("3");//session_menu.getErrors().get(0);
            }
            DocumentSnapshot sc_snapshot = firestore.collection("menus").document(data.get("shortCode").toString()).get().get();
            
            Map data2 = session_menu.getNextScreenDetails(sc_snapshot.getData(), data.get("nextScreen").toString());
            firestore.collection("sessions").document(session.get("sessionId")).set(data2).get();
            
            return session_menu.displayText(data2);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ExternalProcessors.class.getName()).log(Level.SEVERE, null, ex);
            return "END "+SETTINGS.getFlowError("3");
        }
    }

}
