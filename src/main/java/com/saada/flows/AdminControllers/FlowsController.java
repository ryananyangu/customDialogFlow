package com.saada.flows.AdminControllers;

import com.saada.flows.services.FlowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/admin/flow/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class FlowsController {

    @Autowired
    private FlowService flowService;

    private final boolean isAdmin = true;


    @ResponseBody
    @GetMapping(path = "list")
    public String getShortCodes() {
        return flowService.listFlows(isAdmin).toString();
    }

    @ResponseBody
    @DeleteMapping(path = "delete/{shortcode}")
    public String deleteFlow(@PathVariable String shortcode) {
        return flowService.deleteFlow(shortcode,isAdmin).toString();
    }
}