package com.custom.dialog.lab.repositories;


import java.util.List;

import com.custom.dialog.lab.models.CustomUser;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;


public interface UserRepository extends FirestoreReactiveRepository<CustomUser>{
    public List<CustomUser> findByOrganization(String organization);
}