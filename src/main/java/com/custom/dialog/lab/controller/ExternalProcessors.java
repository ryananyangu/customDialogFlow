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

        String input = session.get("serviceCode");
        if (!session.get("text").isEmpty()) {
            String[] inputs = session.get("text").split("\\*");
            input = inputs[inputs.length - 1];
        }

        AfricaStalkingProcessor session_menu = new AfricaStalkingProcessor(session.get("phoneNumber"),
                session.get("sessionId"), input, new HashMap<>());
        try {
            DocumentSnapshot ss_snapshot = firestore.collection("sessions").document(session.get("sessionId")).get()
                    .get();
            Map sessionData = ss_snapshot.getData();
            Map<String, String> data = session_menu.screenNavigate(sessionData);

            if (data.isEmpty()) {
                HashMap<String, Object> bkp_ssession = session_menu.prepareArchiveSessions("INCOMPLETE",
                        data.get("shortCode"));

                HashMap<String, Object> journey = session_menu.prepareArchiveJourney(session_menu.getErrors().get(0),
                        input);

                firestore.collection("archived_sessions").document(session.get("sessionId")).set(bkp_ssession).get();
                firestore.collection("archived_sessions").document(session.get("sessionId")).collection("journey")
                        .document().set(journey).get();
                return "END " + session_menu.getErrors().get(0);
            }

            // Check if end of the flow
            if ("end".equalsIgnoreCase(data.get("nextScreen"))) {

                HashMap<String, Object> nodedata = (HashMap<String, Object>) sessionData.get("screenNode");
                HashMap<String, String> extraNodedata = (HashMap<String, String>) nodedata.get("nodeExtraData");

                HashMap<String, Object> bkp_ssession = session_menu.prepareArchiveSessions("COMPLETE",
                        data.get("shortCode"));

                HashMap<String, Object> journey = session_menu.prepareArchiveJourney(extraNodedata.get("exit_message"),
                        input);

                firestore.collection("sessions").document(session.get("sessionId")).delete().get();
                firestore.collection("archived_sessions").document(session.get("sessionId")).set(bkp_ssession).get();
                firestore.collection("archived_sessions").document(session.get("sessionId")).collection("journey")
                        .document().set(journey).get();

                return "END " + extraNodedata.get("exit_message");
            }
            DocumentSnapshot sc_snapshot = firestore.collection("menus").document(data.get("shortCode")).get().get();
            Map data2 = session_menu.getNextScreenDetails(sc_snapshot.getData(), data.get("nextScreen"));
            String display_msg = session_menu.displayText(data2);
            data2.put("display_msg", display_msg);
            firestore.collection("sessions").document(session.get("sessionId")).set(data2).get();

            HashMap<String, Object> bkp_ssession = session_menu.prepareArchiveSessions("INCOMPLETE",
                    data.get("shortCode"));

            HashMap<String, Object> journey = session_menu.prepareArchiveJourney(display_msg, input);

            firestore.collection("archived_sessions").document(session.get("sessionId")).set(bkp_ssession).get();
            firestore.collection("archived_sessions").document(session.get("sessionId")).collection("journey")
                    .document().set(journey).get();
            return display_msg;
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(DialogFlowController.class.getName()).log(Level.SEVERE, null, ex);
            return "END " + SETTINGS.getFlowError("3");
        }

    }

}
