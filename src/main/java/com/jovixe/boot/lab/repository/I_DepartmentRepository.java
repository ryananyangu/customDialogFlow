package com.jovixe.boot.lab.repository;

import com.jovixe.boot.lab.model.Department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * I_DepartmentRepository
 */
@Repository
public interface I_DepartmentRepository extends JpaRepository<Department,Long> {

    
}