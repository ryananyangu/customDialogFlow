package com.saada.flows.repositories;

import com.saada.flows.models.ServiceCode;
import com.saada.flows.models.ServiceType;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

import reactor.core.publisher.Flux;

public interface ServiceCodeRepository extends FirestoreReactiveRepository<ServiceCode> {
    public Flux<ServiceCode> findByOrganization(String organization);
    public Flux<ServiceCode> findByServiceType(ServiceType serviceType);
    public Flux<ServiceCode> findByProvider(String provider);
    public Flux<ServiceCode> findByActive(boolean active);
    public Flux<ServiceCode> findByOrganization(String organization,boolean active);
}