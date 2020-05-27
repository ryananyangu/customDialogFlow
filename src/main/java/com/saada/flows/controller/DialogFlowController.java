package com.saada.flows.controller;

import com.saada.flows.models.Flow;
import com.saada.flows.models.Screen;
import com.saada.flows.services.FlowService;
import com.saada.flows.services.SessionService;
import com.saada.flows.utils.Props;

import java.util.HashMap;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/flow/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class DialogFlowController {

    @Autowired
    private Props props;

    @Autowired
    private FlowService flowService;

    @Autowired
    private SessionService sessionService;



    @GetMapping(path = "get/screen", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String sessionNavigator(@RequestParam String msisdn, @RequestParam String session,
            @RequestParam String input) {
                return sessionService.screenNavigate(session, input, input, msisdn);
    }

    @ResponseBody
    @PostMapping(path = "create")
    public String bulkCreateScreen(@RequestBody @Valid HashMap<String, Screen> screens) {
        try {
            Flow flow = flowService.buildFlow(screens);
            return flowService.createFlow(flow).toString();
        } catch (Exception ex) {
            return props.getStatusResponse("400_SCRN", ex.getMessage()).toString();
        }
    }

    @ResponseBody
    @PostMapping(path = "validate")
    public String validateFlow(@RequestBody @Valid HashMap<String, Screen> screens) {

        try {
            Flow flow = flowService.buildFlow(screens);
            flow.isValidFlow();
        } catch (Exception ex) {
            return props.getStatusResponse("400_SCRN", ex.getMessage()).toString();
        }
        return props.getStatusResponse("200_SCRN", screens).toString();
    }

    @GetMapping(path = "get")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getFlow(@RequestParam String shortcode) {
        return flowService.getFlow(shortcode).toString();
    }

    @ResponseBody
    @DeleteMapping(path = "delete/{shortcode}")
    public String deleteFlow(@PathVariable String shortcode) {
        return flowService.deleteFlow(shortcode).toString();
    }

    @ResponseBody
    @GetMapping(path = "list")
    public String getShortCodes() {
        return flowService.listFlows().toString();
    }

    @PutMapping("update/{shortcode}")
    public String update(@PathVariable String shortcode, @RequestBody @Valid HashMap<String, Screen> flow) {
        try {
            Flow builtFlow = flowService.buildFlow(flow);
            return flowService.updateFlows(shortcode, builtFlow).toString();
        } catch (Exception ex) {
            return props.getStatusResponse("400_SCRN", ex.getMessage()).toString();
        }
    }

    @GetMapping(path = "sessions")
    public String getSessions(){
        return sessionService.listSessions().toString();
    }
}
