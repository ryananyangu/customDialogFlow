package com.saada.flows.controller;

import javax.validation.Valid;

import com.saada.flows.models.Organization;
import com.saada.flows.services.OrganizationService;
import com.saada.flows.utils.Props;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/client/organization/", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {


    private final boolean isAdmin = false;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private Props props;

    @GetMapping(path = "get")
    public String getOrganization(@RequestParam String organizationName){
        return organizationService.getOrganizationDetails(organizationName).toString();
    }



    @PutMapping(path = "update/{organizationName}")
    public String updateOrganization(@PathVariable String organizationName, @RequestBody @Valid Organization organization,BindingResult result){
        // result
        return organizationService.updateOrganization(organization,isAdmin).toString();
    }

    @PutMapping(path = "get")
    public String getOrganization(){
        return props.getStatusResponse("200_SCRN", organizationService.getLoggedInUserOrganization()).toString();

    }
    
}