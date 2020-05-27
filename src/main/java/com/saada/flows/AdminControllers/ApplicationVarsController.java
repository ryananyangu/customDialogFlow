package com.saada.flows.AdminControllers;

import javax.validation.Valid;

import com.saada.flows.models.ApplicationVars;
import com.saada.flows.services.ApplicationVarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ApplicationVarsController
 */
@RestController
@RequestMapping(path = "/api/v1/admin/appconfigs/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationVarsController {

    @Autowired
    private ApplicationVarService applicationVarService;

    @GetMapping(path = "list")
    @ResponseBody
    public String getConfigs(){
        return applicationVarService.list().toString();
    }

    @PutMapping(path = "update")
    @ResponseBody
    public String update(@RequestBody @Valid ApplicationVars vars){
        return applicationVarService.update(vars).toString();
    }

    @PostMapping(path = "post")
    @ResponseBody
    public String create(@RequestBody @Valid ApplicationVars vars){
        return applicationVarService.create(vars).toString();
    }
    
}