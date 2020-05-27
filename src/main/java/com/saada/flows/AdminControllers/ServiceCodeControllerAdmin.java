package com.saada.flows.AdminControllers;

import javax.validation.Valid;

import com.saada.flows.models.ServiceCode;
import com.saada.flows.services.ServiceCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/admin/shortcode/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceCodeControllerAdmin {

    @Autowired
    private ServiceCodeService serviceCodeService;

    @GetMapping(path = "get")
    @ResponseBody
    public String getAvailableShortCode(){
        return serviceCodeService.getAllCodes().toString();
    }
    @PostMapping(path = "approve")
    @ResponseBody
    public String approveServiceCode(@RequestBody String serviceCodeId){
        return serviceCodeService.approveCode(serviceCodeId).toString();
    }

    @PostMapping(path = "update")
    @ResponseBody
    public String updateServiceCode(@RequestBody @Valid ServiceCode serviceCodeId){
        return serviceCodeService.updateCode(serviceCodeId).toString();
    }
}