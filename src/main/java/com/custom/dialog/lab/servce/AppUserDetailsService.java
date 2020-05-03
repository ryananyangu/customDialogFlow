///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.custom.dialog.lab.servce;
//
//import com.custom.dialog.lab.models.AppUserDetails;
//import com.google.api.core.ApiFuture;
//import com.google.cloud.firestore.CollectionReference;
//import com.google.cloud.firestore.DocumentReference;
//import com.google.cloud.firestore.DocumentSnapshot;
//import com.google.cloud.firestore.Firestore;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
///**
// *
// * @author jovixe
// */
//public class AppUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private Firestore database;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        CollectionReference collectionReference = database.collection("users");
//        DocumentReference documentReference = collectionReference.document(email);
//
//        DocumentSnapshot document;
//        try {
//            ApiFuture<DocumentSnapshot> future = documentReference.get();
//            document = future.get(10, TimeUnit.SECONDS);
//
//        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
//            throw new UsernameNotFoundException("Error getting user data found >> " + ex.getLocalizedMessage());
//        }
//
//        Map<String, Object> data = document.getData();
//        if (data.isEmpty()) {
//            throw new UsernameNotFoundException("Error user data not found");
//        }
//
//        String username = data.get("email").toString();
//        String password = data.get("password").toString();
//        List<String> authorities = (List<String>) document.get("authorities");
//        return new AppUserDetails(username, password, authorities.toArray(new String[authorities.size()]));
//    }
//
//}
