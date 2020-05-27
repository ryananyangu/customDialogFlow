package com.saada.flows.repositories;

import com.saada.flows.models.Session;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface SessionRepository extends FirestoreReactiveRepository<Session> {
    
}