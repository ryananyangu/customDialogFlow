package com.saada.flows.repositories;



import com.saada.flows.models.SessionHistory;


import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// import reactor.core.publisher.Flux;

public interface SessionHistoryRepository extends FirestoreReactiveRepository<SessionHistory>{
    // Flux<SessionHistory> findByOrganization(String organization, Pageable pageable);
    Page<SessionHistory> findAll(Pageable pageable);
    Page<SessionHistory> findByOrganization(String organization, Pageable pageable);
}