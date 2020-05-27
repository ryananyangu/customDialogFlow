/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saada.flows.controller;

import com.saada.flows.models.User;
import com.saada.flows.services.UserDetailsService;
import java.util.HashMap;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping(path = "/api/v1/client/user/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserDetailsService UserDetailsService;
    private final boolean isAdmin = false;

    @PostMapping(path = "token", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getToken(@RequestBody HashMap<String, String> auth) {

        return UserDetailsService.getToken(auth.get("username"), auth.get("password")).toString();
    }

    @PostMapping(path = "signup", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String createUser(@RequestBody @Valid User user) {
        return UserDetailsService.createUser(user).toString();
    }

    @PutMapping("update/{username}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String updateUser(@PathVariable String username,@RequestBody @Valid User updatedDetails) {
        return UserDetailsService.updateUser(username,updatedDetails,isAdmin).toString();
    }

    @ResponseBody
    @GetMapping(path = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUsers() {
        return UserDetailsService.listUsers(isAdmin).toString();
    }


}
