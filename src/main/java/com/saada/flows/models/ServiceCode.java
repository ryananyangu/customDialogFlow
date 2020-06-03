package com.saada.flows.models;


import java.util.Date;
import java.util.HashMap;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.cloud.firestore.annotation.DocumentId;

import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collectionName = "serviceCodes")
public class ServiceCode {

    @DocumentId
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String serviceCodeId;

    
    private String serviceCode;
    private ServiceType serviceType;
    private String provider;
    
    private boolean active;
    private String organization;
    private HashMap<String,Object> extraData;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLastModified;


    



    public void setActive(boolean active) {
        this.active = active;
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
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public Date getDateLastModified() {
        return dateLastModified;
    }
    public String getOrganization() {
        return organization;
    }
    public String getProvider() {
        return provider;
    }
    public String getServiceCode() {
        return serviceCode;
    }

    public boolean isActive() {
        return active;
    }  
    public HashMap<String, Object> getExtraData() {
        return extraData;
    }
    public void setExtraData(HashMap<String, Object> extraData) {
        this.extraData = extraData;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceCodeId() {
        return serviceCodeId;
    }
    public void setServiceCodeId(String serviceCodeId) {
        this.serviceCodeId = serviceCodeId;
    }
    

}