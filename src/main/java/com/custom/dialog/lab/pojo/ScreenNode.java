package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Props;
import java.io.Serializable;
import java.util.*;


public class ScreenNode implements Serializable{

    private static final Props SETTINGS = new Props();
//    private final static Logger LOGGER = Logger.getLogger(ScreenNode.class.getName());

    private boolean isScreenActive = true;
    private String screenNext = new String();
    private String screenText = new String();
    private String screenType = new String();
    private String nodeName = new String();
    private List<String> nodeOptions = new ArrayList<>();
    private List<HashMap<String, String>> nodeItems = new ArrayList<>();
    private HashMap<String, String> nodeExtraData = new HashMap<>();
    
    private List<Object> errors = new ArrayList<>();

    private String shortCode = new String();

    public boolean validate() {

        return (!getScreenType().isEmpty() && !getNodeName().isEmpty() && !getShortCode().isEmpty()
                && !getScreenText().isEmpty());
    }

    public boolean validateRawInput() {
        return (!screenNext.isEmpty() && nodeOptions.isEmpty() && nodeItems.isEmpty());

    }

    public boolean validateOptions() {
        return (!screenNext.isEmpty() && !nodeOptions.isEmpty() && nodeItems.isEmpty());
    }

    public boolean validateItems() {
        if (screenNext.isEmpty() && nodeOptions.isEmpty() && !nodeItems.isEmpty()) {
            for (HashMap<String, String> item : nodeItems) {
                if (!item.containsKey("nextScreen") || !item.containsKey("displayText")
                        || item.get("displayText").isEmpty() || item.get("nextScreen").isEmpty()) {
                    errors.add(SETTINGS.getStatusResponse("400_SCRN_5", nodeItems));
                    return false;
                }
            }
            return true;
        }
        errors.add(SETTINGS.getStatusResponse("400_SCRN_4", nodeName));
        return false;

    }

    public List<Object> getErrors() {
        return errors;
    }
    

    /**
     * @param nodeExtraData the nodeExtraData to set
     */
    public void setNodeExtraData(HashMap<String, String> nodeExtraData) {
        this.nodeExtraData = nodeExtraData;
    }

    /**
     * @param nodeItems the nodeItems to set
     */
    public void setNodeItems(List<HashMap<String, String>> nodeItems) {
        this.nodeItems = nodeItems;
    }

    /**
     * @param nodeName the nodeName to set
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @param nodeOptions the nodeOptions to set
     */
    public void setNodeOptions(List<String> nodeOptions) {
        this.nodeOptions = nodeOptions;
    }

    /**
     * @param isScreenActive the isScreenActive to set
     */
    public void setScreenActive(boolean isScreenActive) {
        this.isScreenActive = isScreenActive;
    }
    

    /**
     * @param screenData the screenData to set
     */
    /**
     * @param screenNext the screenNext to set
     */
    public void setScreenNext(String screenNext) {
        this.screenNext = screenNext;
    }

    /**
     * @param screenText the screenText to set
     */
    public void setScreenText(String screenText) {
        this.screenText = screenText;
    }

    /**
     * @param screenType the screenType to set
     */
    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    /**
     * @param shortCode the shortCode to set
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * @return the screenNext
     */
    public String getScreenNext() {
        return screenNext;
    }

    /**
     * @return the shortCode
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * @return the screenType
     */
    public String getScreenType() {
        return screenType;
    }

    /**
     * @return the nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @return the screenText
     */
    public String getScreenText() {
        return screenText;
    }

    public HashMap<String, String> getNodeExtraData() {
        return nodeExtraData;
    }

    public List<HashMap<String, String>> getNodeItems() {
        return nodeItems;
    }

    public List<String> getNodeOptions() {
        return nodeOptions;
    }

    public boolean isActive(){
        return isScreenActive;
    }

}
