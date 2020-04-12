package com.custom.dialog.lab.controller;

import com.custom.dialog.lab.pojo.Session;
import java.util.HashMap;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DialogFlowController{

    @GetMapping(path = "/get/flow", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String getMenu(@RequestParam String msisdn, @RequestParam String session, @RequestParam String input){
        Session session_menu = new Session();
        return new JSONObject( session_menu.testRedis()).toString(); //;
    }
    
}