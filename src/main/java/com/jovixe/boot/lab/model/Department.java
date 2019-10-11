package com.jovixe.boot.lab.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Department
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
public @Data class Department implements Serializable {

    private @Id @GeneratedValue(strategy = GenerationType.AUTO) long departmentID;
    private @CreatedDate LocalDateTime dateCreated;
    private @LastModifiedDate LocalDateTime lastModifiedDate;
    private String departmentName;
    private String departmentDesc;

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    
}