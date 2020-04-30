package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Props;
import com.custom.dialog.lab.utils.Utils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import java.util.*;
import lombok.Data;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Data
public class ScreenNode {

    private static final Props SETTINGS = new Props();
    private final static Logger LOGGER = Logger.getLogger(ScreenNode.class.getName());

    private boolean isScreenActive = true;
    private String screenNext = new String();
    private String screenText = new String();
    private String screenType = new String();
    private String nodeName = new String();
    private List<String> nodeOptions = new ArrayList<>();
    private List<HashMap<String, String>> nodeItems = new ArrayList<>();
    private HashMap<String, String> nodeExtraData = new HashMap<>();
    List<ApiFuture<WriteResult>> validatedScreens = new ArrayList<>();
    List<HashMap> screenData = new ArrayList<>();
    private String shortCode = new String();
    private final Database database = new Database();

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
        nodeData.put("nodeName", nodeName);

        return nodeData;
    }

    public JSONObject saveRedisData(Object data) {
        if (!buildScreen(data).isEmpty()) {
            return buildScreen(data);

        }

        boolean isSuccess = database.saveData(prepareToRedis(), shortCode, nodeName);
        if (isSuccess) {
            return SETTINGS.getStatusResponse("200_SCRN_1", data);
        }
        return SETTINGS.getStatusResponse("400_SCRN_7", data);

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

        try {
            setScreenActive(jsonNode.getBoolean("isScreenActive"));
            setScreenNext(jsonNode.getString("screenNext"));
            setNodeName(jsonNode.getString("nodeName"));
            setScreenText(jsonNode.getString("screenText"));
            setScreenType(jsonNode.getString("screenType"));
            setShortCode(jsonNode.getString("shortCode"));
        } catch (JSONException jex) {
            return SETTINGS.getStatusResponse("500_STS_3", Utils.getCodelineNumber() + " >> " + jex.getMessage());
        }

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
            return SETTINGS.getStatusResponse("400_SCRN_6", new HashMap());
        }

    }

    public JSONObject isValidFlow(Object screens) {

        HashMap<String, HashMap<String, Object>> screenBulk = (HashMap<String, HashMap<String, Object>>) screens;
        List<String> requiredScreens = new ArrayList<>();
        requiredScreens.add("start_page");

        while (!requiredScreens.isEmpty()) {
            String screen = requiredScreens.get(0);

            // check if screen is valid
            if (!screenBulk.containsKey(screen)) {
                return SETTINGS.getStatusResponse("404_SCRN_1", screen);
            }

            // structure validate and set vars for obj
            HashMap<String, Object> node = screenBulk.get(screen);
            node.put("nodeName", screen);
            if (!buildScreen(node).isEmpty()) {
                return buildScreen(node);
            }

            // preparation for bulk save
            screenData.add(prepareToRedis());

            HashMap<String, Object> currentScreen = screenBulk.get(screen);
            if (currentScreen.get("screenType").toString().equalsIgnoreCase("items")) {
                List<HashMap<String, String>> nodeItemsValid = (List<HashMap<String, String>>) currentScreen.get("nodeItems");
                nodeItemsValid.forEach((item) -> {
                    String nextScreen = item.get("nextScreen");
                    if (!"end".equalsIgnoreCase(nextScreen)) {
                        requiredScreens.add(item.get("nextScreen"));
                    }
                });
            } else {
                String nextScreen = currentScreen.get("screenNext").toString();
                if (!"end".equalsIgnoreCase(nextScreen)) {
                    requiredScreens.add(nextScreen);
                }

            }

            screenBulk.remove(screen);
            requiredScreens.remove(screen);
        }

        return new JSONObject();

    }

    public JSONObject bulkSave() {
        for (HashMap data : screenData) {
            String screen = data.get("nodeName").toString();
            data.remove("nodeName");
            boolean isSuccess = database.saveData(data, data.get("shortCode").toString(), screen);
            if (!isSuccess) {
                return SETTINGS.getStatusResponse("400_SCRN_7", data);
            }
        }

        return SETTINGS.getStatusResponse("200_SCRN_1", new HashMap());

    }

    public JSONObject updateScreen(Object data) {

        if (!buildScreen(data).isEmpty()) {
            return buildScreen(data);

        }
        boolean isSuccess = database.updateData(prepareToRedis(), shortCode, nodeName);
        if (isSuccess) {
            return SETTINGS.getStatusResponse("200_SCRN_1", data);
        }
        return SETTINGS.getStatusResponse("400_SCRN_7", data);

    }

    public JSONObject deleteData(Object data) {
        HashMap<String, String> request = (HashMap) data;
        if (!request.containsKey("shortCode") || !request.containsKey("nodeName")
                || request.keySet().size() != 2) {
            return SETTINGS.getStatusResponse("400", data);
        }
        setShortCode(request.get("shortCode"));
        setNodeName(request.get("nodeName"));
        boolean isSuccess = database.deleteData(shortCode, nodeName, true);
        if (!isSuccess) {
            return SETTINGS.getStatusResponse("400_SCRN_7", data);

        }

        HashMap<String, Object> flow = database.retrieveDataList(shortCode);
        if (flow.isEmpty()) {
            
            // deleted node was root node
            return SETTINGS.getStatusResponse("200_SCRN_1", flow);
        }

        HashMap<String, Object> orphanNodes = internalFlowValidation(flow);

        if (orphanNodes.isEmpty()) {
            return SETTINGS.getStatusResponse("200_SCRN_1", data);
        }

        // deleting extra orphaned nodes
        orphanNodes.keySet().forEach((key) -> {
            database.deleteData(shortCode, key, false);
        });
        return SETTINGS.getStatusResponse("200_SCRN_1", data);
    }

    public HashMap internalFlowValidation(HashMap<String, Object> flow) {
        List<String> requiredScreens = new ArrayList<>();
        requiredScreens.add("start_page");

        while (!requiredScreens.isEmpty()) {
            String screen = requiredScreens.get(0);

            // check if screen is valid
            if (!flow.containsKey(screen)) {
                requiredScreens.remove(screen);
                continue;
            }

            HashMap<String, Object> currentScreen = (HashMap<String, Object>) flow.get(screen);
            if (currentScreen.get("screenType").toString().equalsIgnoreCase("items")) {
                List<HashMap<String, String>> nodeItemsValid = (List<HashMap<String, String>>) currentScreen.get("nodeItems");
                nodeItemsValid.forEach((item) -> {
                    String nextScreen = item.get("nextScreen");
                    if (!"end".equalsIgnoreCase(nextScreen)) {
                        requiredScreens.add(item.get("nextScreen"));
                    }
                });
            } else {
                String nextScreen = currentScreen.get("screenNext").toString();
                if (!"end".equalsIgnoreCase(nextScreen)) {
                    requiredScreens.add(nextScreen);
                }

            }

            flow.remove(screen);
            requiredScreens.remove(screen);
        }
        return flow;

    }

    public JSONObject getFlow(String shortcode) {
        HashMap<String, Object> flow = database.retrieveDataList(shortcode);
        if (flow.isEmpty()) {
            return SETTINGS.getStatusResponse("404_FLW_1", flow);
        }
        return new JSONObject(flow);
    }

}
