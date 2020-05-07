/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.services;

import org.springframework.beans.factory.annotation.Autowired;
import com.custom.dialog.lab.models.CustomUser;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author jovixe
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    Firestore firestore;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApiFuture<DocumentSnapshot> documentFuture
                = this.firestore.document("users/" + username).get();

        CustomUser user;
        try {
            user = documentFuture.get().toObject(CustomUser.class);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(CustomUserDetailsService.class.getName()).log(Level.SEVERE, null, ex);
            throw new UsernameNotFoundException("Error in processing authentication" + ex.getLocalizedMessage());
        }

        if (user.getEmail() == null || user.getPassword() == null) {
            throw new UsernameNotFoundException("Username invalid");
        }
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

}
