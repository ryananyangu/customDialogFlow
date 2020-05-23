package com.custom.dialog.lab.repositories;

import com.custom.dialog.lab.models.Session;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface SessionRepository extends FirestoreReactiveRepository<Session> {
    
}