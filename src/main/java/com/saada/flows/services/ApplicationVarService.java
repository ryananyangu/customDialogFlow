package com.saada.flows.services;

import com.saada.flows.models.ApplicationVars;
import com.saada.flows.repositories.ApplicationVarRepository;
import com.saada.flows.utils.Props;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationVarService {

    @Autowired
    private ApplicationVarRepository applicationVarRepository;

    @Autowired
    private Props props;


    public JSONObject create(ApplicationVars vars){
        applicationVarRepository.save(vars).block();
        return props.getStatusResponse("200_SCRN", vars);
    }

    public JSONObject list(){
        return props.getStatusResponse("200_SCRN", applicationVarRepository.findAll().collectList().block());
    }

    public JSONObject update(ApplicationVars vars){
        if(!applicationVarRepository.existsById(vars.getKey()).block()){
            return props.getStatusResponse("400", "variable not found");
        }
        applicationVarRepository.save(vars).block();
        return props.getStatusResponse("200_SCRN", vars);
    }
    
}