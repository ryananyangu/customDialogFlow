package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Props;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import java.util.*;
import lombok.Data;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

@Data
public class ScreenNode {

    private static final Props SETTINGS = new Props();
    private final List<String> pendingScreens = new ArrayList<>();
    private Firestore database;
    private final static Logger LOGGER = Logger.getLogger(ScreenNode.class.getName());

    public ScreenNode() {
        database = FirestoreOptions.getDefaultInstance().getService();
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

        return SETTINGS.getStatusResponse("400_SCRN_1", new HashMap());

    }

    public JSONObject validateRawInput(JSONArray nodeOptions, JSONArray nodeItems) {
        if (!screenNext.isEmpty() && nodeOptions.isEmpty() && nodeItems.isEmpty()) {
            return new JSONObject();
        }

        return SETTINGS.getStatusResponse("400_SCRN_2", new HashMap());

    }

    public JSONObject validateOptions(JSONArray nodeOptions, JSONArray nodeItems) {
        if (!screenNext.isEmpty() && !nodeOptions.isEmpty() && nodeItems.isEmpty()) {
            for (int i = 0; i < nodeOptions.length(); i++) {
                this.nodeOptions.add(nodeOptions.getString(i));
            }
            return new JSONObject();

        }
        return SETTINGS.getStatusResponse("400_SCRN_3", new HashMap());
    }

    public HashMap prepareToRedis() {

        HashMap<String, Object> nodeData = new HashMap<>();
        nodeData.put("isScreenActive", isScreenActive);
        nodeData.put("screenNext", screenNext);
        nodeData.put("screenText", screenText);
        nodeData.put("screenType", screenType);
        nodeData.put("nodeOptions", nodeOptions);
        nodeData.put("nodeItems", nodeItems);
        nodeData.put("nodeExtraData", nodeExtraData);
        nodeData.put("shortCode", shortCode);

        return nodeData;
    }

    public JSONObject saveRedisData(Map<String, Object> data) {
        CollectionReference collectionReference = database.collection(shortCode);
  
        DocumentReference documentReference = collectionReference.document(nodeName);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;
        Map<String, Object> response_data = new HashMap<>();
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.log(Level.SEVERE, null, ex);

            return SETTINGS.getStatusResponse("500_STS_3", response_data);
        }

        if (!document.exists()) {
            // TODO: Check if collection exists
            documentReference.set(data);

            response_data.put("pending_screens", pendingScreens);
            return SETTINGS.getStatusResponse("200_SCRN", response_data);
        }
        
        

        return SETTINGS.getStatusResponse("400_SCRN_7", response_data);

    }

    public List<String> setPendingScreens(String screenName) {
        if (screenName.equalsIgnoreCase("quit")) {
            return pendingScreens;
        }
        pendingScreens.add(screenName);
        return pendingScreens;
    }

    public JSONObject validateItems(JSONArray nodeOptions, JSONArray nodeItems) {
        if (screenNext.isEmpty() && nodeOptions.isEmpty() && !nodeItems.isEmpty()) {
            for (int i = 0; i < nodeItems.length(); i++) {
                JSONObject jsonNodeItem = new JSONObject(nodeItems.get(i).toString());
                if (jsonNodeItem.has("nextScreen") && jsonNodeItem.has("displayText")) {
                    HashMap<String, String> nodeItem = new HashMap<>();
                    nodeItem.put("nextScreen", jsonNodeItem.getString("nextScreen"));
                    nodeItem.put("displayText", jsonNodeItem.getString("displayText"));
                    setPendingScreens(jsonNodeItem.getString("nextScreen"));
                    this.nodeItems.add(nodeItem);

                } else {
                    return SETTINGS.getStatusResponse("400_SCRN_5", new HashMap());
                }
            }
            return new JSONObject();
        }
        return SETTINGS.getStatusResponse("400_SCRN_4", new HashMap());

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
            setPendingScreens(getScreenNext());
            return validateRawInput(jsonNodeOptions, jsonNodeItems);
        } else if (getScreenType().equalsIgnoreCase("options")) {
            setPendingScreens(getScreenNext());
            return validateOptions(jsonNodeOptions, jsonNodeItems);
        } else if (getScreenType().equalsIgnoreCase("items")) {
            return validateItems(jsonNodeOptions, jsonNodeItems);
        } else {
            return SETTINGS.getStatusResponse("400_SCRN_6", new HashMap());
        }

    }

    public JSONObject deleteScreen(Object data) {
//        syncCommands.hdel(, ks);
        return null;
    }

    public JSONObject mapedMap() {
        JSONObject mapedMap = new JSONObject();
        for (Field field : this.getClass().getDeclaredFields()) {
            String val = new String();
            String varname = field.getName();
            if (varname.equalsIgnoreCase("SETTINGS") || varname.equalsIgnoreCase("database")) {
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
