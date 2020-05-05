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
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jovixe
 */

@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private static Props props;

    @PostMapping(
            path = "/authenticate",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String saveUser(HashMap<String, String> auth) throws Exception {
        CustomUser user = new CustomUser(auth.get("username"), auth.get("password"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);

        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails);

        return props.getStatusResponse("200_SCRN", jwt).toString();
    }
}
