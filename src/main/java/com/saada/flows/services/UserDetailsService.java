/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saada.flows.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.saada.flows.models.User;
import com.saada.flows.repositories.UserRepository;
import com.saada.flows.utils.JwtUtil;
import com.saada.flows.utils.Props;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author jovixe
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Props props;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findById(username).block();

        if (user.getUsername() == null || user.getPassword() == null) {
            throw new UsernameNotFoundException("Username invalid");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getAuthorities()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
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

    public JSONObject updateUser(String username,User updatedDetails,boolean isAdmin) {


        if(!isAdmin && !updatedDetails.getOrganization().equalsIgnoreCase(getCurrentLoggedInUser().getOrganization())){
            return props.getStatusResponse("400_USR_1", "user not authorised to use service");
        }
        User user = userRepository.findById(username).block();

        if (!userRepository.existsById(username).block()
                || getCurrentLoggedInUser().getOrganization().isEmpty()) {
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

    public JSONObject createUser(User user) {

        if (getCurrentLoggedInUser().getOrganization().isEmpty()) {
            return props.getStatusResponse("400_USR_1", user.getOrganization());
        }


        if (userRepository.existsById(user.getEmail()).block()) {
            return props.getStatusResponse("400_USR_1", "User already exists");
        }
        List<String>  authorities = user.getAuthorities();
        authorities.add("ROLE_USER");
        user.setAuthorities(authorities);
        user.setPassword(passwordEncoder(user.getPassword()));
        user.setDateCreated(Calendar.getInstance().getTime());
        user.setDateLastModified(Calendar.getInstance().getTime());

        userRepository.save(user).block();

        return props.getStatusResponse("200_SCRN_1", user);
    }

    public JSONObject listUsers(boolean isAdmin) {
        // filter by organization and do not show password
        List<User> users;
        if(isAdmin){
            users = userRepository.findAll().collectList().block();
        }else{
            users = userRepository.findByOrganization(getCurrentLoggedInUser().getOrganization()).collectList().block();
        }
        users.forEach(user ->{
            user.setPassword("");
        });
        
        return props.getStatusResponse("200_SCRN_1", users);
    }

    public JSONObject deleteUsers(String username,boolean isAdmin) {
        User user = userRepository.findById(username).block();
        if(!isAdmin && !getCurrentLoggedInUser().getOrganization().equalsIgnoreCase(user.getOrganization())){
            return props.getStatusResponse("400_USR_1", "user not permitted to us this service");

        }
        userRepository.delete(user).block();
        return props.getStatusResponse("200_SCRN", username);

    }

    

    public User getCurrentLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
            
        }
        return userRepository.findById(username).block();
    }

}
