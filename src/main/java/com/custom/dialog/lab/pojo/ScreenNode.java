package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Data;
import java.lang.reflect.Field;
import org.json.JSONArray;
import org.json.JSONObject;

@Data
public class ScreenNode {

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

        return Utils.responseDisplay("400_SCRN_1", "Validation failed, Mandatory fields left empty");

    }

    public JSONObject validateRawInput(JSONArray nodeOptions, JSONArray nodeItems) {
        if (!screenNext.isEmpty() && nodeOptions.isEmpty() && nodeItems.isEmpty()) {
            return new JSONObject();
        }

        return Utils.responseDisplay("400_SCRN_2", "Validation failed, Raw input has to define screen Next and not have option and items");

    }

    public JSONObject validateOptions(JSONArray nodeOptions, JSONArray nodeItems) {
        if (!screenNext.isEmpty() && !nodeOptions.isEmpty() && nodeItems.isEmpty()) {
            for (int i = 0; i < nodeOptions.length(); i++) {
                this.nodeOptions.add(nodeOptions.getString(i));
            }
            return new JSONObject();

        }
        return Utils.responseDisplay("400_SCRN_3", "Validation failed, options input has to define screen Next and have option and not items");
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
                    return Utils.responseDisplay("400_SCRN_5", "Validation failed, items should contain both nextScreen and displayText key");
                }     
            }
            return new JSONObject();   
        }
        return Utils.responseDisplay("400_SCRN_4", "Validation failed, items input has to define screen Next and have items and not options");

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
            return Utils.responseDisplay("400_SCRN_6", "Uknown Page type submitted -> " + getScreenType());
        }

    }

    public JSONObject mapedMap() {
        JSONObject mapedMap = new JSONObject();
        for (Field field : this.getClass().getDeclaredFields()) {
            String val = new String();
            String varname = field.getName();
            try {
                val = field.get(this) == null ? new String() : field.get(this) + "";
                mapedMap.put(varname, val);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if (val.isEmpty()) {
                throw new IllegalArgumentException(varname);
            }

        }
        return mapedMap;
    }
}
