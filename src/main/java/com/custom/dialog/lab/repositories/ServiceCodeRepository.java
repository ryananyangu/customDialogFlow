package com.custom.dialog.lab.repositories;

import com.custom.dialog.lab.models.ServiceCode;
import com.custom.dialog.lab.models.ServiceType;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

import reactor.core.publisher.Flux;

public interface ServiceCodeRepository extends FirestoreReactiveRepository<ServiceCode> {
    public Flux<ServiceCode> findByOrganization(String organization);
    public Flux<ServiceCode> findByServiceType(ServiceType serviceType);
    public Flux<ServiceCode> findByProvider(String provider);
    
    public Flux<ServiceCode> findByActive(boolean active);
}