package com.jovixe.boot.lab.controller;

import java.util.Map;
import com.jovixe.boot.lab.model.Employee;
import com.jovixe.boot.lab.repository.I_EmployeeRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 */
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
     I_EmployeeRepository userRepository;
    
     @PostMapping("/saveUser")
     public ResponseEntity<?> save(@RequestBody Map<String,Object> payload){
         JSONObject userPayload =new JSONObject(payload);
         String name =userPayload.getString("name");
         Employee user =new Employee();
         user.setEmployeName(name);
         return ResponseEntity.ok(userRepository.save(user));             
     }   
}