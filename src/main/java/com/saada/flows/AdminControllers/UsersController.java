package com.saada.flows.AdminControllers;


import javax.validation.Valid;

import com.saada.flows.models.User;
import com.saada.flows.services.UserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/admin/user/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    @Autowired
    private UserDetailsService UserDetailsService;

    private final boolean isAdmin = true;


    @ResponseBody
    @DeleteMapping(path = "delete/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteFlow(@PathVariable String username) {
        return UserDetailsService.deleteUsers(username,isAdmin).toString();
    }

    @PutMapping("update/{username}")
    @ResponseBody
    public String updateUser(@PathVariable String username,@RequestBody @Valid User updatedDetails) {
        return UserDetailsService.updateUser(username,updatedDetails,isAdmin).toString();
    }

    @ResponseBody
    @GetMapping(path = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUsers() {
        return UserDetailsService.listUsers(isAdmin).toString();
    }
}