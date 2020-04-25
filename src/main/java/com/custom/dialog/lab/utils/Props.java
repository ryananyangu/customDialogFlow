package com.custom.dialog.lab.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Props
 */
@Data
public class Props {

    private Properties core;

    private String statusCodes;
    private String redis_host;
    private int redis_port;
    private String redis_password;

    public Props() {
        setProps();
        setup();
    }

    private void setProps() {
        File file = new File(System.getProperty("user.dir") + "/configs/app.properties");
        core = new Properties();
        try {
            core.load(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setup() {
        this.statusCodes = Utils.readFile(core.getProperty("custom.status.codes"));
        this.redis_host = core.getProperty("redis.host");
        this.redis_port = Integer.parseInt(core.getProperty("redis.port"));
        this.redis_password = core.getProperty("redis.password");
    }

//    public String getFlowLoc(){
//        return flowloc;
//	}
//	
    /**
     * @return the redis_host
     */
    public String getRedis_host() {
        return redis_host;
    }

    public JSONObject getStatusResponse(String code, Object data) {

        HashMap<String,Object> returnCode = new HashMap<>();
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
        returnCode.put("data",data);
        return new JSONObject(returnCode);
    }

    /**
     * @return the redis_port
     */
    public int getRedis_port() {
        return redis_port;
    }

    /**
     * @return the redis_password
     */
    public String getRedis_password() {
        return redis_password;
    }
}
