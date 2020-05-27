package com.saada.flows.repositories;

import com.saada.flows.models.Organization;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface OrganizationRepository extends FirestoreReactiveRepository<Organization>{
    
}