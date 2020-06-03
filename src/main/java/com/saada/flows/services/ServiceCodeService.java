package com.saada.flows.services;

import java.util.Calendar;
import java.util.List;

import com.saada.flows.models.ServiceCode;
import com.saada.flows.repositories.ServiceCodeRepository;
import com.saada.flows.utils.Props;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ServiceCodeService
 */
@Service
public class ServiceCodeService {

    // TODO:Delete

    @Autowired
    private ServiceCodeRepository serviceCodeRepository;

    @Autowired
    private Props props;

    @Autowired
    private OrganizationService organizationService;

    public JSONObject getAvailableCodes() {
        List<ServiceCode> codes = serviceCodeRepository.findByOrganization(organizationService.getLoggedInUserOrganization().getName(), false).collectList().block();
        return props.getStatusResponse("200_SCRN", codes);
    }

    public JSONObject getOwnedCodes() {
        List<ServiceCode> codes = serviceCodeRepository
                .findByOrganization(organizationService.getLoggedInUserOrganization().getName()).collectList().block();
        return props.getStatusResponse("200_SCRN", codes);
    }


    public JSONObject getAllCodes() {
        List<ServiceCode> codes = serviceCodeRepository.findAll().collectList().block();
        return props.getStatusResponse("200_SCRN", codes);
    }

    public JSONObject applyForCode(String shortCodeId){

        if(!serviceCodeRepository.existsById(shortCodeId).block()){
            return props.getStatusResponse("400_SCRN", "Invalid shortcode "+shortCodeId );
        }
        ServiceCode shortCode = serviceCodeRepository.findById(shortCodeId).block();
        shortCode.setActive(false);
        shortCode.setDateLastModified(Calendar.getInstance().getTime());
        shortCode.setOrganization(organizationService.getLoggedInUserOrganization().getName());
        serviceCodeRepository.save(shortCode).block();
        return props.getStatusResponse("200_SCRN", shortCode);
    }

    public JSONObject approveCode(String shortCodeId){
        if(!serviceCodeRepository.existsById(shortCodeId).block()){
            return props.getStatusResponse("400_SCRN", "Invalid shortcode "+shortCodeId );
        }
        ServiceCode shortCode = serviceCodeRepository.findById(shortCodeId).block();
        shortCode.setActive(true);
        shortCode.setDateLastModified(Calendar.getInstance().getTime());
        serviceCodeRepository.save(shortCode).block();
        return props.getStatusResponse("200_SCRN", shortCode);
    }

    public JSONObject updateCode(ServiceCode shortCodeId){
        if(!serviceCodeRepository.existsById(shortCodeId.getServiceCodeId()).block()){
            return props.getStatusResponse("400_SCRN", "Invalid shortcode "+shortCodeId );
        }
        ServiceCode shortCode = serviceCodeRepository.findById(shortCodeId.getServiceCodeId()).block();
        shortCode.setDateLastModified(Calendar.getInstance().getTime());
        serviceCodeRepository.save(shortCode).block();
        return props.getStatusResponse("200_SCRN", shortCode);
    }


    public ServiceCode getAll(String provider, String serviceCode){
        return serviceCodeRepository.findByProviderAndServiceCode(provider, serviceCode).collectList().block().get(0);
    }



}