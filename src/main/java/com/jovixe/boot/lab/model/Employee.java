package com.jovixe.boot.lab.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * User
 */
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long EmployeeID;
    private String EmployeName;
    @OneToOne()
    private Department EmployeeDepartment;

    public void setEmployeName(String EmployeName) {
        this.EmployeName = EmployeName;
    }

    public void setEmployeeDepartment(Department EmployeeDepartment) {
        this.EmployeeDepartment = EmployeeDepartment;
    }

    public void setEmployeeID(long EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    public String getEmployeName() {
        return EmployeName;
    }

    public Department getEmployeeDepartment() {
        return EmployeeDepartment;
    }

    public long getEmployeeID() {
        return EmployeeID;
    }
}