package com.custom.dialog.lab.repositories;

import com.custom.dialog.lab.models.Flow;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface FlowRepository extends FirestoreReactiveRepository<Flow>{
    
}