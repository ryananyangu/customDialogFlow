/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.controller;

import com.custom.dialog.lab.models.CustomUser;
import com.custom.dialog.lab.services.CustomUserDetailsService;
import com.custom.dialog.lab.utils.JwtUtil;
import com.custom.dialog.lab.utils.Props;
import com.google.cloud.firestore.DocumentReference;

import com.google.cloud.firestore.Firestore;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jovixe
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/user/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    Firestore firestore;

    private static final Props SETTINGS = new Props();

    @PostMapping(
            path = "token",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String saveUser(@RequestBody HashMap<String, String> auth) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.get("username"), auth.get("password"))
            );
        } catch (AuthenticationException e) {
            return SETTINGS.getStatusResponse("401", e.getMessage()).toString();

        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(auth.get("username"));

        final String jwt = jwtUtil.generateToken(userDetails);

        return SETTINGS.getStatusResponse("200_SCRN", jwt).toString();
    }

    @PostMapping(
            path = "signup",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String createUser(@RequestBody HashMap<String, String> request) throws InterruptedException, ExecutionException {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(request.get("password"));
        CustomUser user = new CustomUser(request.get("username"), hashedPassword);

        DocumentReference reference = firestore.collection("users").document(user.getEmail());
        if (reference.get().get().exists()) {
            return SETTINGS.getStatusResponse("400_USR_1", request.get("username")).toString();
        }
        reference.set(user).get();
        return SETTINGS.getStatusResponse("200_SCRN_1", request.get("username")).toString();
    }
}
