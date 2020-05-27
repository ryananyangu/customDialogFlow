package com.saada.flows.controller;

import javax.validation.Valid;

import com.saada.flows.models.Flow;

import com.saada.flows.services.FlowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v2/flow/")
public class FlowController {

    @Autowired
    private FlowService flowService;

    @PostMapping(path = "create")
    public String createFlow(@RequestBody @Valid Flow flow){
        return flowService.createFlow(flow).toString();
    }

    @PutMapping(path = "update/{shortcode}")
    public String updateFlow(@PathVariable String shortcode, @RequestBody @Valid Flow flow){
        return flowService.updateFlows(shortcode, flow).toString();
    }

    @PostMapping(path = "validate")
    public String validateFlow(@RequestBody @Valid Flow flow){
        return flowService.createFlow(flow).toString();
    }
    
}