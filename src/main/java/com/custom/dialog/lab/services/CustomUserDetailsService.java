/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.custom.dialog.lab.models.CustomUser;
import com.custom.dialog.lab.repositories.OrganizationRepository;
import com.custom.dialog.lab.repositories.UserRepository;
import com.custom.dialog.lab.utils.JwtUtil;
import com.custom.dialog.lab.utils.Props;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author jovixe
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Props props;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomUser user = userRepository.findById(username).block();

        if (user.getUsername() == null || user.getPassword() == null) {
            throw new UsernameNotFoundException("Username invalid");
        }
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public JSONObject getToken(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            return props.getStatusResponse("401", e.getMessage());
        }

        final UserDetails userDetails = loadUserByUsername(username);

        final String jwt = jwtUtil.generateToken(userDetails);

        return props.getStatusResponse("200_SCRN", jwt);
    }

    public JSONObject updateUser(String username, CustomUser updatedDetails) {

        CustomUser user = userRepository.findById(username).block();

        if (!userRepository.existsById(username).block()
                || !organizationRepository.existsById(user.getOrganization()).block()) {
            return props.getStatusResponse("400_USR_1", username);
        }

        updatedDetails.setDateCreated(user.getDateCreated());
        updatedDetails.setDateLastModified(Calendar.getInstance().getTime());

        if (!username.equalsIgnoreCase(updatedDetails.getUsername())) {
            userRepository.delete(user).block();
        }

        userRepository.save(updatedDetails).block();

        return props.getStatusResponse("200_SCRN_1", updatedDetails);
    }

    public String passwordEncoder(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public JSONObject createUser(CustomUser user) {

        if (!organizationRepository.existsById(user.getOrganization()).block()) {
            return props.getStatusResponse("400_USR_1", user.getOrganization());
        }

        user.setPassword(passwordEncoder(user.getPassword()));
        user.setDateCreated(Calendar.getInstance().getTime());
        user.setDateLastModified(Calendar.getInstance().getTime());

        userRepository.save(user).block();

        return props.getStatusResponse("200_SCRN_1", user);
    }

    public JSONObject listUsers() {
        // filter by organization and do not show password
        List<CustomUser> users = userRepository.findAll().collectList().block();
        return props.getStatusResponse("200_SCRN_1", users);
    }

    public JSONObject deleteUsers(String username) {
        CustomUser user = userRepository.findById(username).block();
        userRepository.delete(user).block();
        return props.getStatusResponse("200_SCRN", username);

    }

}
