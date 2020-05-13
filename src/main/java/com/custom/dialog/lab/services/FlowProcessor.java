/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.services;

import com.custom.dialog.lab.utils.Props;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author jovixe
 */
public class FlowProcessor {

    private static final Props SETTINGS = new Props();
    private List<Object> errors = new ArrayList<>();
    private String shortcode = new String();
    private Map<String, Map> screenData = new HashMap<>();

    public boolean isValidFlow(Object screens) {

        HashMap<String, HashMap<String, Object>> screenBulk = (HashMap<String, HashMap<String, Object>>) screens;
        List<String> requiredScreens = new ArrayList<>();
        requiredScreens.add("start_page");

        while (!requiredScreens.isEmpty()) {
            String screen = requiredScreens.get(0);

            // check if screen is valid
            if (!screenBulk.containsKey(screen)) {
                errors.add(SETTINGS.getStatusResponse("404_SCRN_1", screen));
                return false;
            }

            // structure validate and set vars for obj
            HashMap<String, Object> node = screenBulk.get(screen);
            node.put("nodeName", screen);
            ScreenNode screenNode = buildScreen(node);

            errors = screenNode.getErrors();
            if (buildScreen(node).getNodeName().isEmpty()) {
                errors.add(SETTINGS.getStatusResponse("400_SCRN_1", screen));
                return false;
            }

            if ("items".equalsIgnoreCase(screenNode.getScreenType())) {

                screenNode.getNodeItems().forEach((item) -> {
                    String nextScreen = item.get("nextScreen");
                    if (!"end".equalsIgnoreCase(nextScreen)) {
                        requiredScreens.add(item.get("nextScreen"));
                    }
                });
            } else {
                if (!"end".equalsIgnoreCase(screenNode.getScreenNext())) {
                    requiredScreens.add(screenNode.getScreenNext());
                }

            }
            shortcode = screenNode.getShortCode();
            screenData.put(screenNode.getNodeName(), prepareScreen(screenNode));

            screenBulk.remove(screen);
            requiredScreens.remove(screen);
        }

        return true;

    }

    public Map prepareScreen(ScreenNode screenNode) {

        Map nodeData = new HashMap<>();
        nodeData.put("isScreenActive", screenNode.isActive());
        nodeData.put("screenNext", screenNode.getScreenNext());
        nodeData.put("screenText", screenNode.getScreenText());
        nodeData.put("screenType", screenNode.getScreenType());
        nodeData.put("nodeOptions", screenNode.getNodeOptions());
        nodeData.put("nodeItems", screenNode.getNodeItems());
        nodeData.put("nodeExtraData", screenNode.getNodeExtraData());
        nodeData.put("shortCode", shortcode);
        nodeData.put("nodeName", screenNode.getNodeName());
        return nodeData;
    }

    public ScreenNode buildScreen(Object node) {
        ScreenNode screenNode = new ScreenNode();

        HashMap<String, Object> nodeMap = (HashMap<String, Object>) node;
        screenNode.setNodeOptions((List<String>) nodeMap.get("nodeOptions"));
        screenNode.setNodeItems((List<HashMap<String, String>>) nodeMap.get("nodeItems"));

        screenNode.setScreenActive((boolean) nodeMap.get("isScreenActive"));
        screenNode.setScreenNext(nodeMap.get("screenNext").toString());
        screenNode.setNodeName(nodeMap.get("nodeName").toString());
        if (!shortcode.isEmpty() && !nodeMap.get("shortCode").toString().equalsIgnoreCase(shortcode)) {
            return new ScreenNode();
        }
        screenNode.setScreenText(nodeMap.get("screenText").toString());
        screenNode.setScreenType(nodeMap.get("screenType").toString());
        screenNode.setShortCode(nodeMap.get("shortCode").toString());

        if (!screenNode.validate()) {
            return new ScreenNode();
        }

        if (screenNode.getScreenType().equalsIgnoreCase("raw_input")
                && screenNode.validateRawInput()) {

            return screenNode;
        } else if (screenNode.validateOptions()
                && screenNode.getScreenType().equalsIgnoreCase("options")) {

            return screenNode;
        } else if (screenNode.validateItems()
                && screenNode.getScreenType().equalsIgnoreCase("items")) {
            return screenNode;
        } else {
            return new ScreenNode();
        }

    }

    public Map<String, Map> getScreenData() {
        return screenData;
    }

    public String getShortcode() {
        return shortcode;
    }

    public List<Object> getErrors() {
        return errors;
    }

}
