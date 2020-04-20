package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Props;
import com.custom.dialog.lab.utils.Utils;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class Session {

    private static final Props SETTINGS = new Props();
    private RedisCommands<String, String> syncCommands;

    private String phoneNumber;
    private String sessionId;
    private String input;
//    private 

    public Session(String phoneNumber, String sessionId, String Input) {

        this.phoneNumber = phoneNumber;
        this.sessionId = sessionId;
        this.input = Input;

        RedisURI redisURI = RedisURI.Builder.
                redis(SETTINGS.getRedis_host(), SETTINGS.getRedis_port())
                .withPassword(SETTINGS.getRedis_password()).build();
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        syncCommands = connection.sync();
    }

    public JSONObject getNodeData() {
        Map<String, String> sessionData = syncCommands.hgetall(sessionId);
        String shortCode = new String();
        String currentNode = new String();

        if (sessionData.isEmpty()) {
            Map<String, String> flowData = syncCommands.hgetall(input);
            if (flowData.isEmpty()) {
                return SETTINGS.getStatusResponse("404_ND");
            }
            // set session tied to shortcode // should always have one item
            syncCommands.hset(sessionId, input, "start_page");
            return new JSONObject(flowData.get("start_page"));
        }

        for (Map.Entry<String, String> entry : sessionData.entrySet()) {
             shortCode = entry.getKey();
             currentNode = entry.getValue();
             break;
        }
        
        Map<String, String> flowData = syncCommands.hgetall(shortCode);
        
        JSONObject currentPageData = new JSONObject(flowData.get(currentNode));
        
        String type =currentPageData.getString("nodeType");
        
        if(type.equalsIgnoreCase("raw_input")){
            
            String nextPage = currentPageData.getString("nextScreen");
            JSONObject nextPageData = new JSONObject(flowData.get(nextPage));
            syncCommands.hset(sessionId, shortCode, nextPage);
            
            return nextPageData;
            
        }else if (type.equalsIgnoreCase("options")){
            String nextPage = currentPageData.getString("nextScreen");
            JSONObject nextPageData = new JSONObject(flowData.get(nextPage));
            syncCommands.hset(sessionId, shortCode, nextPage);
            
            return nextPageData;
            
        }else if (type.equalsIgnoreCase("menu_items")){
            
            
            JSONArray nextPageObj = currentPageData.getJSONArray("menuItems");
            
            String nextPage = nextPageObj.getJSONObject(Integer.parseInt(input)).getString("nextScreen");
            JSONObject nextPageData = new JSONObject(flowData.get(nextPage));
            syncCommands.hset(sessionId, shortCode, nextPage);
            
            return nextPageData;
        }
        
        return new JSONObject();
        
        

    }

}
