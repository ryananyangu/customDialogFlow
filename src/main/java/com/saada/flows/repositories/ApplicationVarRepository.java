package com.saada.flows.repositories;

import com.saada.flows.models.ApplicationVars;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface ApplicationVarRepository extends FirestoreReactiveRepository<ApplicationVars>{
    
}