package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Props;
import com.custom.dialog.lab.utils.Utils;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.util.*;
import lombok.Data;
import java.lang.reflect.Field;
import org.json.JSONArray;
import org.json.JSONObject;

@Data
public class ScreenNode {
    
    private static final Props SETTINGS = new Props();
    private RedisCommands<String, String> syncCommands;
    
    public ScreenNode(){
                RedisURI redisURI = RedisURI.Builder.
                redis(SETTINGS.getRedis_host(), SETTINGS.getRedis_port())
                .withPassword(SETTINGS.getRedis_password()).build();
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        syncCommands = connection.sync();
    }

    private boolean isScreenActive = true;
    private String screenNext = new String();
    private String screenText = new String();
    private String screenType = new String();
    private String nodeName = new String();
    private List<String> nodeOptions = new ArrayList<>();
    private List<HashMap<String, String>> nodeItems = new ArrayList<>();
    private HashMap<String, String> nodeExtraData = new HashMap<>();
    private String shortCode = new String();

    public JSONObject validate() {
        /**
         * Validate mandatory fields
         */
        if (!getScreenType().isEmpty() && !getNodeName().isEmpty()
                && !getShortCode().isEmpty() && !getScreenText().isEmpty()) {
            return new JSONObject();

        }

        return SETTINGS.getStatusResponse("400_SCRN_1");

    }

    public JSONObject validateRawInput(JSONArray nodeOptions, JSONArray nodeItems) {
        if (!screenNext.isEmpty() && nodeOptions.isEmpty() && nodeItems.isEmpty()) {
            return new JSONObject();
        }

        return SETTINGS.getStatusResponse("400_SCRN_2");

    }

    public JSONObject validateOptions(JSONArray nodeOptions, JSONArray nodeItems) {
        if (!screenNext.isEmpty() && !nodeOptions.isEmpty() && nodeItems.isEmpty()) {
            for (int i = 0; i < nodeOptions.length(); i++) {
                this.nodeOptions.add(nodeOptions.getString(i));
            }
            return new JSONObject();

        }
        return  SETTINGS.getStatusResponse("400_SCRN_3");
    }
    
    public String prepareToRedis(){
        
        JSONObject nodeData = new JSONObject();
//        nodeData.put("nodeName", nodeName);
        nodeData.put("isScreenActive", isScreenActive);
        nodeData.put("screenNext", screenNext);
        nodeData.put("screenText", screenText);
        nodeData.put("screenType", screenType);  
        nodeData.put("nodeOptions", new JSONArray(nodeOptions).toString());                
        nodeData.put("nodeItems", new JSONArray(nodeItems).toString());
        nodeData.put("nodeExtraData", new JSONObject(nodeExtraData).toString());                        
        nodeData.put("shortCode", shortCode);  
        
        return nodeData.toString();
    }
    
    public JSONObject saveRedisData(String data){
        
        Map<String,String> codePages =   syncCommands.hgetall(shortCode);
        if(codePages.isEmpty())
        {
            syncCommands.hset(shortCode, "start_page",data);
            return SETTINGS.getStatusResponse("200_SCRN");
        }
        
        if(!codePages.containsKey(nodeName)){
            syncCommands.hset(shortCode, nodeName,data);
            return SETTINGS.getStatusResponse("200_SCRN");
        }
        
        return SETTINGS.getStatusResponse("400_SCRN_7");
        
        
    }

    public JSONObject validateItems(JSONArray nodeOptions, JSONArray nodeItems) {
        if (screenNext.isEmpty() && nodeOptions.isEmpty() && !nodeItems.isEmpty()) {
            for (int i = 0; i < nodeItems.length(); i++) {
                JSONObject jsonNodeItem = new JSONObject(nodeItems.get(i).toString());
                if (jsonNodeItem.has("nextScreen") && jsonNodeItem.has("displayText")) {
                    HashMap<String, String> nodeItem = new HashMap<>();
                    nodeItem.put("nextScreen", jsonNodeItem.getString("nextScreen"));
                    nodeItem.put("displayText", jsonNodeItem.getString("displayText"));
                    this.nodeItems.add(nodeItem);
                    
                    
                }else{
                    return SETTINGS.getStatusResponse("400_SCRN_5");
                }     
            }
            return new JSONObject();   
        }
        return SETTINGS.getStatusResponse("400_SCRN_4");

    }

    public JSONObject buildScreen(Object node) {

        JSONObject jsonNode = new JSONObject((HashMap) node);
        JSONArray jsonNodeOptions = jsonNode.getJSONArray("nodeOptions");
        JSONArray jsonNodeItems = jsonNode.getJSONArray("nodeItems");

        setScreenActive(jsonNode.getBoolean("isScreenActive"));
        setScreenNext(jsonNode.getString("screenNext"));
        setNodeName(jsonNode.getString("nodeName"));
        setScreenText(jsonNode.getString("screenText"));
        setScreenType(jsonNode.getString("type"));
        setShortCode(jsonNode.getString("shortCode"));

        // validate mandatory
        if (!validate().isEmpty()) {
            return validate();
        }

        if (getScreenType().equalsIgnoreCase("raw_input")) {
            return validateRawInput(jsonNodeOptions, jsonNodeItems);
        } else if (getScreenType().equalsIgnoreCase("options")) {
            return validateOptions(jsonNodeOptions, jsonNodeItems);
        } else if (getScreenType().equalsIgnoreCase("items")) {
            return validateItems(jsonNodeOptions, jsonNodeItems);
        } else {
            return SETTINGS.getStatusResponse("400_SCRN_6");
        }

    }

    public JSONObject mapedMap() {
        JSONObject mapedMap = new JSONObject();
        for (Field field : this.getClass().getDeclaredFields()) {
            String val = new String();
            String varname = field.getName();
            if(varname.equalsIgnoreCase("SETTINGS") || varname.equalsIgnoreCase("syncCommands")){
                continue;
            }
            try {
                val = field.get(this) == null ? new String() : field.get(this) + "";
                mapedMap.put(varname, val);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return mapedMap;
    }
}
