package com.custom.dialog.lab.repositories;

import java.util.List;

import com.custom.dialog.lab.models.SessionHistory;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface SessionHistoryRepository extends FirestoreReactiveRepository<SessionHistory>{
    public List<SessionHistory> findByOrganization(String organization);
}