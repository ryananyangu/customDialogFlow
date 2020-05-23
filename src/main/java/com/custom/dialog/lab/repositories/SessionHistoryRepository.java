package com.custom.dialog.lab.repositories;

import com.custom.dialog.lab.models.SessionHistory;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface SessionHistoryRepository extends FirestoreReactiveRepository<SessionHistory>{
    
}