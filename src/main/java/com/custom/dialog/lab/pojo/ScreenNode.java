package com.custom.dialog.lab.pojo;

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

    public static ScreenNode buildScreen(Object node) {

        ScreenNode screen = new ScreenNode();
        JSONObject jsonNode = new JSONObject((HashMap) node);

        screen.setScreenActive(jsonNode.getBoolean("isScreenActive"));
        screen.setScreenNext(jsonNode.getString("screenNext"));
        screen.setNodeName(jsonNode.getString("nodeName"));
        screen.setScreenText(jsonNode.getString("screenText"));
        screen.setScreenType(jsonNode.getString("type"));
        screen.setShortCode(jsonNode.getString("shortCode"));
        

        JSONArray jSONnodeOptions = jsonNode.getJSONArray("nodeOptions");
        List<String> nodeOptions = new ArrayList<>();

        for (int i = 0; i < jSONnodeOptions.length(); i++) {
            nodeOptions.add(jSONnodeOptions.getString(i));
        }

        screen.setNodeOptions(nodeOptions);

        JSONArray jsonNodeItems = jsonNode.getJSONArray("nodeItems");
        List<HashMap<String,String>> nodeItems = new ArrayList<>();
                
        for (int i = 0; i < jsonNodeItems.length(); i++) {
            JSONObject jsonNodeItem = jSONnodeOptions.getJSONObject(i);
            HashMap<String,String> nodeItem = new HashMap<>();
            nodeItem.put("nextScreen", jsonNodeItem.getString("nextScreen"));
            nodeItem.put("screenText", jsonNodeItem.getString("screenText"));
            nodeItems.add(nodeItem);
        }

        return screen;
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
