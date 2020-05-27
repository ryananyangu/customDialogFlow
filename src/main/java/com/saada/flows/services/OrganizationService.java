package com.saada.flows.services;

import java.util.Calendar;
import java.util.List;

import com.saada.flows.models.Organization;
import com.saada.flows.repositories.OrganizationRepository;
import com.saada.flows.utils.Props;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    @Autowired
    private Props props;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OrganizationRepository organizationRepository;

    public JSONObject createOrganization(Organization organization) {

        organization.setDateCreated(Calendar.getInstance().getTime());
        organization.setDateLastModified(Calendar.getInstance().getTime());
        Organization savedOrganization = organizationRepository.save(organization).block();

        return props.getStatusResponse("200_SCRN", savedOrganization);

    }

    public JSONObject updateOrganization(String organizationName, Organization organization) {



        if (!organizationRepository.existsById(organizationName).block()) {
            return props.getStatusResponse("400_SCRN", organization);
        }

        Organization organization2 = organizationRepository.findById(organizationName).block();
        organization.setDateCreated(organization2.getDateCreated());
        organization.setDateLastModified(Calendar.getInstance().getTime());

        if(!organizationName.equalsIgnoreCase(organization.getName())){
            organizationRepository.delete(organization2).block();
        }

        Organization savedOrganization = organizationRepository.save(organization).block();

        return props.getStatusResponse("200_SCRN", savedOrganization);

    }


    public JSONObject deleteOrganization(String organizationName){
        if(!organizationRepository.existsById(organizationName).block()){
            return props.getStatusResponse("400_SCRN", organizationName);
        }
        Organization organization = organizationRepository.findById(organizationName).block();
        organizationRepository.delete(organization).block();
        return props.getStatusResponse("200_SCRN", organization);
    }

    public JSONObject listOrganizations(){
        List<Organization> organizations = organizationRepository.findAll().collectList().block();
        return props.getStatusResponse("200_SCRN", organizations);
    }


    public JSONObject getOrganizationDetails(String organizationName){
        if(!organizationRepository.existsById(organizationName).block()){
            return props.getStatusResponse("400_SCRN", organizationName);
        }
        Organization organization = organizationRepository.findById(organizationName).block();
        return props.getStatusResponse("200_SCRN", organization);
    }

    public Organization getLoggedInUserOrganization(){
        String organization = userDetailsService.getCurrentLoggedInUser().getOrganization();
        return organizationRepository.findById(organization).block();
    }

}