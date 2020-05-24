package com.custom.dialog.lab.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.cloud.firestore.annotation.DocumentId;

import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collectionName = "sessionHistory")
public class SessionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @DocumentId
    private String sessionId;

    private String phoneNumber;

    private String serviceCode;

    private String status;

    private List<Session> sessions;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public String getStatus() {
        return status;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}