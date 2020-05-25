package com.custom.dialog.lab.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Props
 */
@Service
public class Props {

    private String statusCodes;
    private String flowErrors;
    private final static Logger LOGGER = Logger.getLogger(Props.class.getName());

    public Props() {
        SystemErrsetup();
        FlowErrsetup();
    }

    private void SystemErrsetup() {
        ClassPathResource resource = new ClassPathResource("statusCodes.json");
        try (Scanner s = new Scanner(resource.getInputStream()).useDelimiter("\\A")) {
            this.statusCodes = s.hasNext() ? s.next() : "";
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void FlowErrsetup() {
        ClassPathResource resource = new ClassPathResource("screenErrors.json");
        try (Scanner s = new Scanner(resource.getInputStream()).useDelimiter("\\A")) {
            this.flowErrors = s.hasNext() ? s.next() : "";
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public JSONObject getStatusResponse(String code, Object data) {

        HashMap<String, Object> returnCode = new HashMap<>();
        JSONObject statuses;
        try {
            statuses = new JSONObject(statusCodes);
        } catch (JSONException e) {

            returnCode.put("statusCode", "500_STS_2");
            returnCode.put("message", "Internal Error StatusCodes not set correctly in file");
            return new JSONObject(returnCode);
        }

        if (statuses.has(code)) {
            returnCode.put("statusCode", code);
            returnCode.put("message", statuses.getString(code));
            returnCode.put("data", data);
            return new JSONObject(returnCode);
        }
        returnCode.put("statusCode", "500_STS_1");
        returnCode.put("message", "Internal Error StatusCode not found in file");
        returnCode.put("data", data);
        return new JSONObject(returnCode);
    }

    public String getFlowError(String code) {
        JSONObject errors;
        try {
            errors = new JSONObject(flowErrors);
        } catch (JSONException e) {
            return "Internal System Error " + Utils.getCodelineNumber();
        }

        if (errors.has(code)) {
            return errors.getString(code);
        }
        return "Internal System Error " + Utils.getCodelineNumber();
    }

    public String getCurrentLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
