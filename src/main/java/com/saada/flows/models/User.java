/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saada.flows.models;


import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *
 * @author jovixe
 */
import com.google.cloud.firestore.annotation.DocumentId;

import java.io.Serializable;
import java.security.Principal;
import java.util.*;

import javax.validation.constraints.NotEmpty;
import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collectionName = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @NotEmpty
    @DocumentId
    private String email;


    @NotEmpty
    private String username;

    private String password;

    @NotEmpty
    private String organization;

    @LastModifiedBy
    private Principal modified;

    @CreatedBy
    private Principal created;
    

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLastModified;

    private List<String> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public User() {

    }

    public User(String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, 
            List<String> authorities,
            String organization) {

        this.organization = organization;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.email = username;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;

    }



    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }


    public Principal getCreated() {
        return created;
    }

    public Principal getModified() {
        return modified;
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

    public void setCreated(Principal created) {
        this.created = created;
    }
    public void setModified(Principal modified) {
        this.modified = modified;
    }


    public String getOrganization() {
        return organization;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<String> getAuthorities() {
        return authorities;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    public boolean isEnabled() {
        return enabled;
    }

}
