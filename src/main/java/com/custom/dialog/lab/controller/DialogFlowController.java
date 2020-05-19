package com.custom.dialog.lab.controller;

import com.custom.dialog.lab.services.FlowProcessor;
import com.custom.dialog.lab.services.SessionProcessor;
import com.custom.dialog.lab.utils.Props;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/flow", produces = MediaType.APPLICATION_JSON_VALUE)
public class DialogFlowController {

    private static final Props SETTINGS = new Props();

    private static final String FLOW_PATH = "menus";

    @Autowired
    Firestore firestore;

    @GetMapping(path = "/get/screen", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String sessionNavigator(@RequestParam String msisdn,
            @RequestParam String session, @RequestParam String input) {

        SessionProcessor session_menu = new SessionProcessor(msisdn, session, input, new HashMap<>());
        try {
            DocumentSnapshot ss_snapshot = firestore.collection("sessions").document(session).get().get();
            Map sessionData = ss_snapshot.getData();
            Map<String, String> data = session_menu.screenNavigate(sessionData);

            if (data.isEmpty()) {
                HashMap<String, Object> bkp_ssession = session_menu.prepareArchiveSessions(
                        "INCOMPLETE",
                        data.get("shortCode")
                );

                HashMap<String, Object> journey = session_menu.prepareArchiveJourney(session_menu.getErrors().get(0), input);

                firestore.collection("archived_sessions").document(session).set(bkp_ssession).get();
                firestore.collection("archived_sessions").document(session).collection("journey").document().set(journey).get();
                return session_menu.getErrors().get(0);
            }

            // Check if end of the flow
            if ("end".equalsIgnoreCase(data.get("nextScreen"))) {

                HashMap<String, Object> nodedata = (HashMap<String, Object>) sessionData.get("screenNode");
                HashMap<String, String> extraNodedata = (HashMap<String, String>) nodedata.get("nodeExtraData");

                HashMap<String, Object> bkp_ssession = session_menu.prepareArchiveSessions(
                        "COMPLETE",
                        data.get("shortCode")
                );

                HashMap<String, Object> journey = session_menu.prepareArchiveJourney(extraNodedata.get("exit_message"), input);

                firestore.collection("sessions").document(session).delete().get();
                firestore.collection("archived_sessions").document(session).set(bkp_ssession).get();
                firestore.collection("archived_sessions").document(session).collection("journey").document().set(journey).get();

                return extraNodedata.get("exit_message");
            }
            DocumentSnapshot sc_snapshot = firestore.collection("menus").document(data.get("shortCode")).get().get();
            Map data2 = session_menu.getNextScreenDetails(sc_snapshot.getData(), data.get("nextScreen"));
            String display_msg = session_menu.displayText(data2);
            data2.put("display_msg", display_msg);
            firestore.collection("sessions").document(session).set(data2).get();

            HashMap<String, Object> bkp_ssession = session_menu.prepareArchiveSessions(
                    "INCOMPLETE",
                    data.get("shortCode")
            );

            HashMap<String, Object> journey = session_menu.prepareArchiveJourney(display_msg, input);

            firestore.collection("archived_sessions").document(session).set(bkp_ssession).get();
            firestore.collection("archived_sessions").document(session).collection("journey").document().set(journey).get();
            return display_msg;
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(DialogFlowController.class.getName()).log(Level.SEVERE, null, ex);
            return SETTINGS.getFlowError("3");
        }
    }

    @ResponseBody
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String bulkCreateScreen(@RequestBody Object screens) {
        FlowProcessor flowProcessor = new FlowProcessor();

        if (flowProcessor.isValidFlow(screens)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + flowProcessor.getShortcode());
            DocumentReference reference = firestore.collection("menus").document(flowProcessor.getShortcode());
            try {
                if (reference.get().get().exists()) {
                    return SETTINGS.getStatusResponse("400_SCRN", "Shortcode already exists").toString();
                }
                reference.set(flowProcessor.getScreenData()).get();
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(DialogFlowController.class.getName()).log(Level.SEVERE, null, ex);
                return SETTINGS.getStatusResponse("500_STS_3", ex.getLocalizedMessage()).toString();
            }

            return SETTINGS.getStatusResponse("200_SCRN", flowProcessor.getErrors()).toString();
        }

        return SETTINGS.getStatusResponse("400_SCRN", flowProcessor.getErrors()).toString();
    }

    @ResponseBody
    @PostMapping(
            path = "/validate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String validateFlow(@RequestBody Object screens) {
        FlowProcessor flowProcessor = new FlowProcessor();

        if (flowProcessor.isValidFlow(screens)) {
            return SETTINGS.getStatusResponse("200_SCRN", new HashMap()).toString();
        }
        return flowProcessor.getErrors().get(0).toString();
    }

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getFlow(@RequestParam String shortcode) {
        Map<String, Object> response;
        try {
            DocumentReference reference = firestore.collection(FLOW_PATH).document(shortcode);
            if (!reference.get().get().exists()) {
                return SETTINGS.getStatusResponse("404_FLW_1", shortcode).toString();
            }

            response = reference.get().get().getData();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(DialogFlowController.class.getName()).log(Level.SEVERE, null, ex);
            return SETTINGS.getStatusResponse("500_STS_3", ex.getLocalizedMessage()).toString();
        }
        return SETTINGS.getStatusResponse("200_SCRN", response).toString();
    }

    @ResponseBody
    @DeleteMapping(path = "/delete/{shortcode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteFlow(@PathVariable String shortcode) {
        WriteResult result;
        try {
            result = firestore.collection("menus").document(shortcode).delete().get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(DialogFlowController.class.getName()).log(Level.SEVERE, null, ex);
            return SETTINGS.getStatusResponse("500_STS_3", ex.getLocalizedMessage()).toString();
        }

        return SETTINGS.getStatusResponse("200_SCRN", new SimpleDateFormat("YYYY/MM/dd HH:mm:ss").format(result.getUpdateTime().toDate())).toString();
    }

    @ResponseBody
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getShortCodes() {
        HashMap<String, List<String>> configuredCodes = new HashMap<>();
        configuredCodes.put("ussd", new ArrayList<>());
        configuredCodes.put("whatsapp", new ArrayList<>());
        configuredCodes.put("other", new ArrayList<>());
        for (DocumentReference doc : firestore.collection(FLOW_PATH).listDocuments()) {

            if (doc.getId().startsWith("*") && doc.getId().endsWith("#")) {
                configuredCodes.get("ussd").add(doc.getId());

            } else if (doc.getId().startsWith("+") || doc.getId().startsWith("0")) {
                configuredCodes.get("whatsapp").add(doc.getId());
            } else {
                configuredCodes.get("other").add(doc.getId());
            }
        }
        return SETTINGS.getStatusResponse("200_SCRN", configuredCodes).toString();
    }

    @PutMapping("/update/{shortcode}")
    public String update(@PathVariable String shortcode, @RequestBody Object flow) throws InterruptedException, ExecutionException {
        DocumentReference reference = firestore.collection(FLOW_PATH).document(shortcode);

        FlowProcessor flowProcessor = new FlowProcessor();

        if (flowProcessor.isValidFlow(flow) && reference.get().get().exists()) {
            reference.set(flowProcessor.getScreenData()).get();
            return SETTINGS.getStatusResponse("200_SCRN", flowProcessor.getErrors()).toString();
        }

        return SETTINGS.getStatusResponse("400_SCRN", flowProcessor.getErrors()).toString();

    }
}
