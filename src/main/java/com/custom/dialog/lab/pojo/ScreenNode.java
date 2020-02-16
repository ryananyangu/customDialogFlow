package com.custom.dialog.lab.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ScreenNode {

    /*
    - NodePages(int, default:1) 
	- NodeHeader(txt, default:empty)
	- NodeName(str, required:true)  
	- NodeData (list:str->HeaderName(s),reuired:true)
	- NodeCurrentPage (int, default:1)
	- NodeId(int, required:true)
	- NodeActiveStatus(bool, default:true)
	- NodeType(enum:{IM:Input Mode,SM: Selection Mode, EC: External Call, RT: Routed From Process response(Validation)},default:IM)
    - NodeExtraData(object, empty:true) // For EC must have SID
    */
    
    private int NodePages = 1;
    private int NodeCurrentPage = 1;
    private int NodeId; // Has to be set and validated
    private boolean NodeActiveStatus = true;

    private String NodeHeader = "";
    private String NodeName = "start page"; // default is start page hence validate only one start page should exist
    private List<String> NodeData = new ArrayList<>();
    
    private HashMap<String,String> NodeExtraData = new HashMap<>();
    
    public ScreenNode(String NodeName, int NodeId, boolean NodeActiveStatus, 
            String NodeHeader, List<String> NodeData,HashMap<String,String> NodeExtraData, String NodeType)
    {
        this.NodeName = NodeName;
        this.NodeId = NodeId;
        this.NodeActiveStatus = NodeActiveStatus;
        this.NodeHeader = NodeHeader;
        this.NodeData = NodeData;
        this.NodeExtraData = NodeExtraData;

    }
    

    public enum NodeType {
        IM,SM,EC,RT
    }

    /**
     * @return the nodePages
     */
    public int getNodePages() {
        return NodePages;
    }

    /**
     * @return the nodeCurrentPage
     */
    public int getNodeCurrentPage() {
        return NodeCurrentPage;
    }

    /**
     * @return the nodeId
     */
    public int getNodeId() {
        return NodeId;
    }


    /**
     * @return the nodeName
     */
    public String getNodeName() {
        return NodeName;
    }

    public boolean getNodeActiveStatus(){
        return NodeActiveStatus;
    }

    /**
     * @return the nodeHeader
     */
    public String getNodeHeader() {
        return NodeHeader;
    }


    /**
     * @return the nodeData
     */
    public List<String> getNodeData() {
        return NodeData;
    }

    /**
     * @return the nodeExtraData
     */
    public Object getNodeExtraData() {
        return NodeExtraData;
    }
    
    


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return this.NodeName;
    }
    
}