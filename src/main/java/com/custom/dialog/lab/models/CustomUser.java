/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.models;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *
 * @author jovixe
 */
import com.google.cloud.firestore.annotation.DocumentId;

// import java.io.Serializable;
import java.util.*;

// import org.springframework.util.Assert;
import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collectionName = "users")
public class CustomUser implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = -755490062669394115L;

    @DocumentId
    private String email;

    private String password;

    private String organization;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateLastModified;

    private List<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public CustomUser() {

    }

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, 
            List<GrantedAuthority> authorities,
            String organization) {

        this.organization = organization;
        this.email = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;

    }



    // private static List<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
    //     Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
    //     // Ensure array iteration order is predictable (as per
    //     // UserDetails.getAuthorities() contract and SEC-717)
    //     ArrayList<GrantedAuthority> sortedAuthorities = new ArrayList<>(new AuthorityComparator());

    //     for (GrantedAuthority grantedAuthority : authorities) {
    //         Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
    //         sortedAuthorities.add(grantedAuthority);
    //     }

    //     return sortedAuthorities;
    // }

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

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganization() {
        return organization;
    }

    @Override
    public void eraseCredentials() {
        password = null;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities;
    }

    @Override
    public String getPassword() {

        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {

        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {

        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {

        return this.enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
    //     private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    //     public int compare(GrantedAuthority g1, GrantedAuthority g2) {
    //         // Neither should ever be null as each entry is checked before adding it to
    //         // the set.
    //         // If the authority is null, it is a custom authority and should precede
    //         // others.
    //         if (g2.getAuthority() == null) {
    //             return -1;
    //         }

    //         if (g1.getAuthority() == null) {
    //             return 1;
    //         }

    //         return g1.getAuthority().compareTo(g2.getAuthority());
    //     }
    // }

    @Override
    public String getUsername() {

        return this.email;
    }

}
