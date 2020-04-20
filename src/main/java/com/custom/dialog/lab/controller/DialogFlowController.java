package com.custom.dialog.lab.controller;

import com.custom.dialog.lab.pojo.ScreenNode;
import com.custom.dialog.lab.pojo.Session;
import com.custom.dialog.lab.utils.Props;
import org.json.JSONObject;
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
    private static final Props SETTINGS = new Props();

    @GetMapping(path = "/get/atussd/flow", consumes = "application/json", produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String sessionNavigator(@RequestParam String msisdn, @RequestParam String session, @RequestParam String input) {
        Session session_menu = new Session(msisdn, session, input);
        return new JSONObject(session_menu.getNodeData()).toString();
    }

    @ResponseBody
    @PostMapping(path = "/screen/create", consumes = "application/json", produces = "application/json")
    public String createScreen(@RequestBody Object screen) {
        ScreenNode screenNode = new ScreenNode();

        if (!screenNode.buildScreen(screen).isEmpty()) {
            return screenNode.buildScreen(screen).toString();
        }
        String data = screenNode.prepareToRedis();
        return screenNode.saveRedisData(data).toString();
    }
}
