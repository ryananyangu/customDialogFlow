package com.custom.dialog.lab.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.cloud.Date;
import com.google.cloud.firestore.annotation.DocumentId;

import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collectionName = "serviceCodeTypes")
public class ServiceType {

    @DocumentId
    private String name;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLastModified;


    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }
    public Date getDateLastModified() {
        return dateLastModified;
    }
    public String getDescription() {
        return description;
    }
    public String getName() {
        return name;
    }

    

}