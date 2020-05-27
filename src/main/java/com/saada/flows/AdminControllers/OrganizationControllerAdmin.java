package com.saada.flows.AdminControllers;



import javax.validation.Valid;

import com.saada.flows.models.Organization;
import com.saada.flows.services.OrganizationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/admin/organization/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationControllerAdmin {

    @Autowired
    private OrganizationService organizationService;

    private final boolean isAdmin = true;

    @PostMapping(path = "create")
    @ResponseBody
    public String createOrganization(@RequestBody @Valid Organization organization){
        return organizationService.createOrganization(organization).toString();
    }

    @ResponseBody
    @DeleteMapping(path = "delete/{organizationName}")
    public String deleteOrganization(@PathVariable String organizationName){
        return organizationService.deleteOrganization(organizationName).toString();
    }

    @ResponseBody
    @GetMapping(path = "list")
    public String listOrganizations(){
        return organizationService.listOrganizations().toString();
    }

    @PutMapping(path = "update")
    public String updateOrganization(@RequestBody @Valid Organization organization,BindingResult result){
        // result
        return organizationService.updateOrganization(organization,isAdmin).toString();
    }

    @GetMapping(path = "get")
    public String getOrganization(@RequestParam String organizationName){
        return organizationService.getOrganizationDetails(organizationName).toString();
    }
}