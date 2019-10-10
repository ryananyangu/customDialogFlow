package com.jovixe.boot.lab.controller;

import java.util.Map;
import java.util.Optional;

import com.jovixe.boot.lab.model.Department;
import com.jovixe.boot.lab.repository.I_DepartmentRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * DepartmentController
 */
@RestController
@RequestMapping("/api/v1")
public class DepartmentController {


    @Autowired
    I_DepartmentRepository departmentRepository;    

    @PostMapping("/saveDepartment")
    public HttpStatus save(@RequestBody Map<String,Object> payload){
        Department department  = new Department();
        String departmentName =new JSONObject(payload).getString("department");
        department.setDepartmentName(departmentName);
        departmentRepository.save(department);
        return HttpStatus.OK;
        
    }
    @GetMapping("/getDepartment/{id}")
    public Optional<Department> gDepartment(@RequestParam String param) {
        return departmentRepository.findById(Long.parseLong(param));
    }
    


    
}