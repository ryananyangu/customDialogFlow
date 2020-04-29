package com.custom.dialog.lab.controller;

import com.custom.dialog.lab.pojo.FlowGraph;
import com.custom.dialog.lab.pojo.ScreenNode;
import com.custom.dialog.lab.pojo.Session;
import com.custom.dialog.lab.utils.Props;
import com.custom.dialog.lab.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
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

@RestController
@CrossOrigin(origins = "https://dialogflw.uc.r.appspot.com", allowedHeaders = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("/api/v1")
public class DialogFlowController {

    private static final Props SETTINGS = new Props();

    @GetMapping(path = "/get/atussd/flow", consumes = "application/json", produces = "text/html")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String sessionNavigator(@RequestParam String msisdn, @RequestParam String session, @RequestParam String input) {
        Session session_menu = new Session(msisdn, session, input);
        Map<String, Object> sessionData = new HashMap<>();
        try {
            sessionData = session_menu.retrieveData("sessions", session);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(DialogFlowController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<String, Object> nextScreenData = session_menu.screenNavigate(sessionData);
        return session_menu.displayText(nextScreenData);
    }

    @ResponseBody
    @PostMapping(path = "/screen/create", consumes = "application/json", produces = "application/json")
    public String createScreen(@RequestBody Object screen) {
        ScreenNode screenNode = new ScreenNode();
        try {
            if (!screenNode.buildScreen(screen).isEmpty()) {
                return screenNode.buildScreen(screen).toString();
            }
        } catch (JSONException jex) {
            return SETTINGS.getStatusResponse("500_STS_3", Utils.getCodelineNumber() + " >> " + jex.getMessage()).toString();
        }
        HashMap<String, Object> data = screenNode.prepareToRedis();
        return screenNode.saveRedisData(data).toString();
    }

    @ResponseBody
    @PostMapping(path = "/screen/bulk/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String bulkCreateScreen(@RequestBody Object screens) {
        ScreenNode screenNode = new ScreenNode();
        List<String> requiredScreens = new ArrayList<>();
        requiredScreens.add("start_page");
        JSONObject validation;
        try {
            validation = screenNode.isValidFlow(screens, requiredScreens);
        } catch (JSONException jex) {
            return SETTINGS.getStatusResponse("500_STS_3", Utils.getCodelineNumber() + " >> " + jex.getMessage()).toString();
        }
        if (!validation.isEmpty()) {
            return validation.toString();
        }
        screenNode.bulkSave();
        return SETTINGS.getStatusResponse("200_SCRN_1", screens).toString();
    }

    @ResponseBody
    @PostMapping(path = "/flow/validate", consumes = "application/json", produces = "application/json")
    public String validateFlow(@RequestBody Object screens) {
        ScreenNode screenNode = new ScreenNode();

        List<String> requiredScreens = new ArrayList<>();
        requiredScreens.add("start_page");
        JSONObject validation;
        try {
            validation = screenNode.isValidFlow(screens, requiredScreens);
        } catch (JSONException jex) {
            return SETTINGS.getStatusResponse("500_STS_3", Utils.getCodelineNumber() + " >> " + jex.getMessage()).toString();
        }

        if (!validation.isEmpty()) {
            return validation.toString();
        }
        return SETTINGS.getStatusResponse("200_SCRN", screens).toString();
    }

    @GetMapping(path = "/", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getFlow(@RequestParam String shortcode) {
        FlowGraph flow = new FlowGraph();
        return flow.getFlow(shortcode).toString();
    }

}
