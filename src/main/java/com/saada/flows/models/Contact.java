package com.saada.flows.models;


public class Contact {
    


    private String id;
    private String href;
    private String msisdn;
    private String firstName;
    private String lastName;


    private String createdDatetime;

    private String updatedDatetime;


    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setUpdatedDatetime(String updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }


    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getHref() {
        return href;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMsisdn() {
        return msisdn;
    }
    public String getUpdatedDatetime() {
        return updatedDatetime;
    }

}
