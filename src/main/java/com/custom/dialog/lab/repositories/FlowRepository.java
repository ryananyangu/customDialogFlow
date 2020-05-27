package com.custom.dialog.lab.repositories;

import com.custom.dialog.lab.models.Flow;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

import reactor.core.publisher.Flux;

public interface FlowRepository extends FirestoreReactiveRepository<Flow>{

    public Flux<Flow> findByOrganization(String organization);
    
}