package com.custom.dialog.lab.controller;

import com.custom.dialog.lab.pojo.FlowGraph;
import com.custom.dialog.lab.pojo.ScreenNode;
import com.custom.dialog.lab.pojo.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DialogFlowController {
    
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
        
        if (!screenNode.buildScreen(screen).isEmpty()) {
            return screenNode.buildScreen(screen).toString();
        }
        HashMap<String, Object> data = screenNode.prepareToRedis();
        return screenNode.saveRedisData(data).toString();
    }
    
    @GetMapping(path = "/", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getFlow(@RequestParam String shortcode) {
        FlowGraph flow = new FlowGraph();
        return flow.getFlow(shortcode).toString();
    }
        
}
