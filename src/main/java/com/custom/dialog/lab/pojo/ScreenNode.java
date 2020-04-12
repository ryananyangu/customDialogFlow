package com.custom.dialog.lab.pojo;

import java.util.HashMap;
import lombok.Data;

@Data
public class ScreenNode {
    
    private int NodePages = 1;
    private int NodeCurrentPage = 1;
    private int NodeId; // Has to be set and validated
    private boolean NodeActiveStatus = true;

    private String NodeHeader = new String();
    private String NodeName = new String(); // default is start page hence validate only one start page should exist
    
    /**
     * Can only either be node item or node options but not both
     */
    
    private HashMap<String,String> NodeOptions = new HashMap<>(); // go to the has next nodes
    private HashMap<String,String> NodeItems = new HashMap<>(); // regardles of input next node is const
    
    private HashMap<String,String> NodeExtraData = new HashMap<>();
      
    
    /**
     * @param nodeCurrentPage the nodeCurrentPage to set
     */
    public ScreenNode setNodeCurrentPage(int nodeCurrentPage) {
        NodeCurrentPage = nodeCurrentPage;
        return this;
    }

    /**
     * @param nodeExtraData the nodeExtraData to set
     */
    public ScreenNode setNodeExtraData(HashMap<String, String> nodeExtraData) {
        NodeExtraData = nodeExtraData;
        return this;
    }

    /**
     * @param nodeHeader the nodeHeader to set
     */
    public ScreenNode setNodeHeader(String nodeHeader) {
        NodeHeader = nodeHeader;
        return this;
    }

    /**
     * @param nodeId the nodeId to set
     */
    public ScreenNode setNodeId(int nodeId) {
        NodeId = nodeId;
        return this;
    }


    /**
     * @param nodeItems the nodeItems to set
     */
    public ScreenNode setNodeItems(HashMap<String, String> nodeItems) {
        NodeItems = nodeItems;
        return this;
    }

    /**
     * @param nodeActiveStatus the nodeActiveStatus to set
     */
    public ScreenNode setNodeActiveStatus(boolean nodeActiveStatus) {
        NodeActiveStatus = nodeActiveStatus;
        return this;
    }

    /**
     * @param nodeName the nodeName to set
     */
    public ScreenNode setNodeName(String nodeName) {
        NodeName = nodeName;
        return this;
    }

    /**
     * @param nodeOptions the nodeOptions to set
     */
    public ScreenNode setNodeOptions(HashMap<String, String> nodeOptions) {
        NodeOptions = nodeOptions;
        return this;
    }

    /**
     * @param nodePages the nodePages to set
     */
    public ScreenNode setNodePages(int nodePages) {
        NodePages = nodePages;
        return this;
    }

    
    
    
    public static ScreenNode builder(){
        ScreenNode screen = new ScreenNode();
        return screen;
    }
}