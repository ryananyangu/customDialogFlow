package com.custom.dialog.lab.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.google.cloud.firestore.annotation.DocumentId;

import org.springframework.cloud.gcp.data.firestore.Document;

@Document(collectionName = "sessions")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @DocumentId
    private String sessionId;

    private Journey journey;

    private Screen screen;

    private HashMap<String, Object> extraData;

    private Date dateCreated;

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

}