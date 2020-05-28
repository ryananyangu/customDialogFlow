package com.saada.flows.models;

public class Conversation {

    private String createdDatetime;


    private String id;


    private String contactId;

    private String lastReceivedDatetime;

    private String updatedDatetime;

    private String status;


    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastReceivedDatetime(String lastReceivedDatetime) {
        this.lastReceivedDatetime = lastReceivedDatetime;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setUpdatedDatetime(String updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }
    public String getContactId() {
        return contactId;
    }
    public String getCreatedDatetime() {
        return createdDatetime;
    }
    public String getId() {
        return id;
    }

    public String getLastReceivedDatetime() {
        return lastReceivedDatetime;
    }
    public String getStatus() {
        return status;
    }
    public String getUpdatedDatetime() {
        return updatedDatetime;
    }

}
