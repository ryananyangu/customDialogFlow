package com.custom.dialog.lab.repositories;

import com.custom.dialog.lab.models.Organization;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface OrganizationRepository extends FirestoreReactiveRepository<Organization>{
    
}