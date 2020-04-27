package com.custom.dialog.lab.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

/**
 * Props
 */
@Data
public class Props {

    private String statusCodes;
    private final static Logger LOGGER = Logger.getLogger(Props.class.getName());

    public Props() {
        setup();
    }

    private void setup() {
        ClassPathResource resource = new ClassPathResource("statusCodes.json");
        try {
            this.statusCodes = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            LOGGER.log(Level.INFO,
                Utils.prelogString("",
                        Utils.getCodelineNumber(), "Loaded status codes  " + statusCodes));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public JSONObject getStatusResponse(String code, Object data) {

        HashMap<String, Object> returnCode = new HashMap<>();
        JSONObject statuses = new JSONObject();
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
}
