package com.custom.dialog.lab.controller;

import javax.validation.Valid;

import com.custom.dialog.lab.models.Organization;
import com.custom.dialog.lab.services.OrganizationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/organization/", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping(path = "create")
    public String createOrganization(@RequestBody @Valid Organization organization){
        return organizationService.createOrganization(organization).toString();
    }

    @GetMapping(path = "get")
    public String getOrganization(@RequestParam String organizationName){
        return organizationService.getOrganizationDetails(organizationName).toString();
    }

    @DeleteMapping(path = "delete/{organizationName}")
    public String deleteOrganization(@PathVariable String organizationName){
        return organizationService.deleteOrganization(organizationName).toString();
    }

    @GetMapping(path = "list")
    public String listOrganizations(){
        return organizationService.listOrganizations().toString();
    }

    @PutMapping(path = "update/{organizationName}")
    public String updateOrganization(@PathVariable String organizationName, @RequestBody Organization organization){
        return organizationService.updateOrganization(organizationName,organization).toString();
    }

    
}