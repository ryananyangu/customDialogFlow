package com.saada.flows.repositories;

import java.util.List;

import com.saada.flows.models.SessionHistory;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface SessionHistoryRepository extends FirestoreReactiveRepository<SessionHistory>{
    public List<SessionHistory> findByOrganization(String organization);
}