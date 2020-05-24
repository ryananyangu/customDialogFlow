package com.custom.dialog.lab.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.cloud.firestore.annotation.DocumentId;

import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collectionName = "menus")
public class Flow implements Serializable{

    private static final long serialVersionUID = 1L;

    @DocumentId
    private String shortCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLastModified;


    private HashMap<String,Screen> screens;

    private Organization organization;



    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public Organization getOrganization() {
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
}