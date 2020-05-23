package com.custom.dialog.lab.models;

import java.io.Serializable;
import java.util.Date;

import com.google.cloud.firestore.annotation.DocumentId;

import org.springframework.cloud.gcp.data.firestore.Document;

@Document(collectionName = "organizations")
public class Organization implements Serializable{

    private static final long serialVersionUID = 1L;

    @DocumentId
    private String name;

    private String phoneNumber;

    private String email;

    private Date dateCreated;

    private Date dateLastModified;


    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    
}