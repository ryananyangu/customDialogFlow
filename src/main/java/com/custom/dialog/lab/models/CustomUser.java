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
import java.util.Collection;
import java.util.Date;

import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Document(collectionName = "users")
public class CustomUser extends User {

    /**
     *
     */
    private static final long serialVersionUID = -755490062669394115L;

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
            Organization organization) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.email = username;
        this.organization = organization;

    }

    @DocumentId
    private String email;

    private Organization organization;

    private Date dateCreated;

    private Date dateLastModified;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

}
