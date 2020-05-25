package com.custom.dialog.lab.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.cloud.firestore.annotation.DocumentId;

import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collectionName = "sessions")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @DocumentId
    private String sessionId;

    private Journey journey;

    private Screen screen;

    private HashMap<String, Object> extraData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLastModified;

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public HashMap<String, Object> getExtraData() {
        return extraData;
    }

    public Journey getJourney() {
        return journey;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public void setExtraData(HashMap<String, Object> extraData) {
        this.extraData = extraData;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public HashMap<String,Object> extraDataBuilder(String key, Object value) {
        getExtraData().put(key, value);
        return extraData;
    }

}