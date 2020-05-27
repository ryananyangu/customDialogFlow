package com.saada.flows.repositories;


import com.saada.flows.models.User;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

import reactor.core.publisher.Flux;


public interface UserRepository extends FirestoreReactiveRepository<User>{
    public Flux<User> findByOrganization(String organization);
}