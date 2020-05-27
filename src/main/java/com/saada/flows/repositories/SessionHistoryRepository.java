package com.saada.flows.repositories;



import com.saada.flows.models.SessionHistory;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

import reactor.core.publisher.Flux;

public interface SessionHistoryRepository extends FirestoreReactiveRepository<SessionHistory>{
    public Flux<SessionHistory> findByOrganization(String organization);
}