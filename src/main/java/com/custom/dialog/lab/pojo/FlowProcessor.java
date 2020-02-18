package com.custom.dialog.lab.pojo;

import java.util.Hashtable;
import com.custom.dialog.lab.utils.Props;

import com.custom.dialog.lab.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FlowProcessor
 */
public class FlowProcessor {

    private String input;

    private String session;

    private String msisdn;

    private Props settings = new Props();

    private Hashtable<String,ScreenNode> activeSessions = new Hashtable<>();

    public FlowProcessor(String input, String session, String msisdn){
        this.input = input;
        this.session = session;
        this.msisdn = msisdn;
    }

    public JSONArray loadFlow() {
        String jsonString = "";
        try {
            jsonString = Utils.readFile(settings.getFlowLoc()+input+".json");    
        } catch (Exception e) {
            //TODO: Invalid short code log
            return new JSONArray();
        }
        
        JSONArray map = new JSONArray(jsonString);
        return map;
    }

    public String navigation(){
        // navigation check based on redis
        if(activeSessions.containsKey(session)){
            JSONObject obj = new JSONObject();

            obj.append("Message", "Functionality Not set");
            obj.append("Code", "404");
            // TODO: compute the next node based on input
            // call function to move to witin nodes
            return obj.toString();
        }
        JSONArray flw = loadFlow();
        if(flw.isEmpty()){
            JSONObject obj = new JSONObject();

            obj.append("Message", "Invalid ShortCode Dialed ");
            obj.append("Code", "403");
            return obj.toString();
        }
        // load session to redis
        return loadFlow().toString();
    }




}