package com.saada.flows.controller;

import com.saada.flows.services.ServiceCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/client/shortcode/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceCodeController {


    @Autowired
    private ServiceCodeService serviceCodeService;

    @GetMapping(path = "get/available")
    @ResponseBody
    public String getAvailableShortCode(){
        return serviceCodeService.getAvailableCodes().toString();
    }
    @GetMapping(path = "get/owned")
    @ResponseBody
    public String getOwnedShortCode(){
        return serviceCodeService.getOwnedCodes().toString();
    }


    
}