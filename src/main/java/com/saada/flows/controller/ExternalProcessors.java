/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saada.flows.controller;

import com.saada.flows.models.MessageBirdMessage;
import com.saada.flows.services.SessionService;
import com.saada.flows.externalServices.MessageBirdProcessor;

import java.util.Map;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping(path = "/api/v1/public/", produces = "application/x-www-form-urlencoded;charset=UTF-8")
public class ExternalProcessors {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private MessageBirdProcessor messageBirdProcessor;
    

    @PostMapping(path = "atussd", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String sessionNavigator(@RequestParam Map<String, String> session) {

        String input = session.get("serviceCode");
        if (!session.get("text").isEmpty()) {
            String[] inputs = session.get("text").split("\\*");
            input = inputs[inputs.length - 1];
        }
        return sessionService.screenNavigate(session.get("sessionId"), input, session.get("serviceCode"),
                session.get("phoneNumber"));

    }

    @PostMapping(path = "msgbird", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String messageBirdWebhook(@RequestBody @Valid MessageBirdMessage message) {
        messageBirdProcessor.coreProcessor(message);
        return "";

    }

}
