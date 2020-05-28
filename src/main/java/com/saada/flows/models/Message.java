package com.saada.flows.models;

import java.util.HashMap;


public class Message {

    private String conversationId;

    private String createdDatetime;

    private String from;

    private String to;

    private String channelId;

    private HashMap<String,Object> content;

    private String updatedDatetime;

    private String direction;

    private String status;


    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public void setContent(HashMap<String, Object> content) {
        this.content = content;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setTo(String to) {
        this.to = to;
    }

    public void setUpdatedDatetime(String updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }

    public String getChannelId() {
        return channelId;
    }

    public HashMap<String, Object> getContent() {
        return content;
    }
    public String getConversationId() {
        return conversationId;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }
    public String getDirection() {
        return direction;
    }


    public String getFrom() {
        return from;
    }

    public String getStatus() {
        return status;
    }

    public String getTo() {
        return to;
    }
    public String getUpdatedDatetime() {
        return updatedDatetime;
    }

    
}
