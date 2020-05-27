package com.saada.flows.repositories;


import java.util.List;

import com.saada.flows.models.User;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;


public interface UserRepository extends FirestoreReactiveRepository<User>{
    public List<User> findByOrganization(String organization);
}