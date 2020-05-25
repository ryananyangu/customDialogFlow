package com.custom.dialog.lab.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Screen {
    private boolean isScreenActive = true;
    private String screenNext = new String();
    private String screenText = new String();
    private String screenType = new String();
    private String nodeName = new String();
    private List<String> nodeOptions = new ArrayList<>();
    private List<HashMap<String, String>> nodeItems = new ArrayList<>();
    private HashMap<String, String> nodeExtraData = new HashMap<>();

    // TODO: ShortCode should be remove in future versions
    private String shortCode;

    public void validate() throws Exception {
        if (getScreenType().isEmpty() || getNodeName().isEmpty() || getScreenText().isEmpty()) {
            throw new Exception("Validation failed, Mandatory fields left empty");
        }
    }

    public void validateRawInput() throws Exception {
        if (getScreenNext().isEmpty() || !getNodeOptions().isEmpty() || !getNodeItems().isEmpty()) {
            throw new Exception("Validation failed, Raw input has to define screen Next and not have option and items");
        }
    }

    public void validateOptions() throws Exception {
        if (!getScreenNext().isEmpty() && !getNodeOptions().isEmpty() && getNodeItems().isEmpty()) {
            return;
        }
        throw new Exception("Validation failed, options input has to define screen Next and have option and not items");
    }

    public void validateItems() throws Exception {
        if (getScreenNext().isEmpty() && getNodeOptions().isEmpty() && !getNodeItems().isEmpty()) {
            for (HashMap<String, String> item : getNodeItems()) {
                if (!item.containsKey("nextScreen") || !item.containsKey("displayText")
                        || item.get("displayText").isEmpty() || item.get("nextScreen").isEmpty()) {
                    throw new Exception("Validation failed, items should contain both nextScreen and displayText key");

                }
            }
            return;
        }
        throw new Exception("Validation failed, items input has to define screen Next and have items and not options");
    }

    public HashMap<String, String> getNodeExtraData() {
        return nodeExtraData;
    }

    public List<HashMap<String, String>> getNodeItems() {
        return nodeItems;
    }

    public String getNodeName() {
        return nodeName;
    }

    public List<String> getNodeOptions() {
        return nodeOptions;
    }

    public String getScreenNext() {
        return screenNext;
    }

    public String getScreenText() {
        return screenText;
    }

    public String getScreenType() {
        return screenType;
    }
    public String getShortCode() {
        return shortCode;
    }

    public void setNodeExtraData(HashMap<String, String> nodeExtraData) {
        this.nodeExtraData = nodeExtraData;
    }

    public void setNodeItems(List<HashMap<String, String>> nodeItems) {
        this.nodeItems = nodeItems;
    }
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setNodeOptions(List<String> nodeOptions) {
        this.nodeOptions = nodeOptions;
    }

    public void setScreenActive(boolean isScreenActive) {
        this.isScreenActive = isScreenActive;
    }

    public void setScreenNext(String screenNext) {
        this.screenNext = screenNext;
    }

    public void setScreenText(String screenText) {
        this.screenText = screenText;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public boolean isActive() {
        return isScreenActive;
    }

    public Screen validateScreen() throws Exception {

        validate();

        if (getScreenType().equalsIgnoreCase("raw_input")) {
            validateRawInput();
        } else if (getScreenType().equalsIgnoreCase("options")) {
            validateOptions();

        } else if (getScreenType().equalsIgnoreCase("items")) {
            validateItems();
        } else {
            throw new Exception("Undefined screentType >> " + getScreenType());
        }
        return this;
    }

    public void endScreenValidator() throws Exception{
        if ("items".equalsIgnoreCase(getScreenType())) {

            for (HashMap<String, String> item : getNodeItems()) {
                String nextScreen = item.get("nextScreen");
                if ("end".equalsIgnoreCase(nextScreen)) {

                    if (!getNodeExtraData().containsKey("exit_message")) {
                        throw new Exception("exit_message >> " + getNodeName());
                    }

                }
            }
        } else {
            if ("end".equalsIgnoreCase(getScreenNext())) {

                if (!getNodeExtraData().containsKey("exit_message")) {
                    throw new Exception("exit_message >> " + getNodeName());
                }
            }

        }
    }
}