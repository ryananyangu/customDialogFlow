/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.models;

/**
 *
 * @author jovixe
 */
import com.google.cloud.firestore.annotation.DocumentId;
import org.springframework.cloud.gcp.data.firestore.Document;

@Document(collectionName = "users")
public class CustomUser {

    @DocumentId
    private String email;

    private String password;

    private String organization;

    public CustomUser() {
    }

    public CustomUser(String username, String password) {
        this.email = username;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganization() {
        return organization;
    }

    public String getPassword() {
        return password;
    }

}
