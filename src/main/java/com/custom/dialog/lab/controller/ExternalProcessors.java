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
import com.google.cloud.firestore.Firestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @PostMapping(path = "/atussd", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String sessionNavigator(@RequestParam Map<String,String> session) {
//        System.out.println(session);
//        return "END Thanks";
        // phoneNumber=%2B254702079491&serviceCode=%2A384%2A11469%23&text=&sessionId=ATUid_1c1e09269ff2b366033529d495e083e5&networkCode=99999
        
        String input = session.get("serviceCode");
        if(!session.get("text").isEmpty()){
            input = session.get("text");
        }
        
//        new HashMap<>()
        AfricaStalkingProcessor session_menu = new AfricaStalkingProcessor(session.get("phoneNumber"), session.get("sessionId"), input, new HashMap<>());

        Map data = session_menu.screenNavigate();

        if (data.isEmpty()) {
            return session_menu.getErrors().get(0);
        }

        Map data2 = session_menu.getNextScreenDetails(
                data.get("shortCode").toString(),
                data.get("nextScreen").toString());

        return session_menu.displayText(data2);
    }

}
