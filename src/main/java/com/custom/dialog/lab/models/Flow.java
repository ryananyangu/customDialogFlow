package com.custom.dialog.lab.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.cloud.firestore.annotation.DocumentId;

import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collectionName = "menus")
public class Flow implements Serializable {

    private static final long serialVersionUID = 1L;

    @DocumentId
    private String shortCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLastModified;

    private HashMap<String, Screen> screens;

    private String organization;

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public String getOrganization() {
        return organization;
    }

    public HashMap<String, Screen> getScreens() {
        return screens;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setScreens(HashMap<String, Screen> screens) {
        this.screens = screens;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public void isValidFlow() throws Exception {

        List<String> requiredScreens = new ArrayList<>();
        List<String> validatedScreens = new ArrayList<>();
        requiredScreens.add("start_page");

        while (!requiredScreens.isEmpty()) {
            String screen = requiredScreens.get(0);

            // Check in processed and unprocessed for screen
            if (!getScreens().containsKey(screen) && !validatedScreens.contains(screen)) {
                throw new Exception(screen + " >> undefined");
            }

            // Screen already processed
            if (validatedScreens.contains(screen)) {
                requiredScreens.remove(screen);
                continue;
            }

            Screen node = screens.get(screen);
            node.setNodeName(screen);
            node.validate();
            node.endScreenValidator().forEach(item -> {
                requiredScreens.add(item);
            });

            validatedScreens.add(node.getNodeName());
            requiredScreens.remove(screen);

        }

    }
}