package com.custom.dialog.lab.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.custom.dialog.lab.models.Flow;
import com.custom.dialog.lab.models.Screen;
import com.custom.dialog.lab.repositories.FlowRepository;
import com.custom.dialog.lab.repositories.UserRepository;
import com.custom.dialog.lab.utils.Props;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowService {

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Props props;

    @Deprecated
    public Flow buildFlow(HashMap<String, Screen> screens) throws Exception {
        Flow flow = new Flow();
        flow.setScreens(screens);
        flow.setShortCode(screens.get("start_page").getShortCode());
        flow.setDateCreated(Calendar.getInstance().getTime());
        flow.setDateLastModified(Calendar.getInstance().getTime());
        if (props.getCurrentLoggedInUser().isEmpty()) {
            throw new Exception("Failed to retrieve logged in user info");
        }
        String organization = userRepository.findById(props.getCurrentLoggedInUser()).block().getOrganization();
        flow.setOrganization(organization);
        return flow;
    }

    public JSONObject createFlow(Flow flow) {

        try {
            flow.isValidFlow();
        } catch (Exception ex) {
            return props.getStatusResponse("400_SCRN", ex.getMessage());
        }

        if (flowRepository.existsById(flow.getShortCode()).block()) {
            return props.getStatusResponse("400_SCRN", "Flow already exists");
        }

        Flow savedFlow = flowRepository.save(flow).block();
        return props.getStatusResponse("200_SCRN", savedFlow);

    }

    public JSONObject getFlow(String shortCode) {
        if (flowRepository.existsById(shortCode).block()) {
            return props.getStatusResponse("200_SCRN", flowRepository.findById(shortCode).block());
        }
        return props.getStatusResponse("400_SCRN", "Flow does not exist");
    }

    public JSONObject deleteFlow(String shortcode) {
        if (flowRepository.existsById(shortcode).block()) {
            flowRepository.deleteById(shortcode).block();
            return props.getStatusResponse("200_SCRN", shortcode);
        }
        return props.getStatusResponse("400_SCRN", "Flow does not exist" +shortcode);
    }

    public JSONObject listFlows() {
        List<Flow> flows = flowRepository.findAll().collectList().block();
        HashMap<String, List<String>> configuredCodes = new HashMap<>();
        configuredCodes.put("ussd", new ArrayList<>());
        configuredCodes.put("whatsapp", new ArrayList<>());
        configuredCodes.put("other", new ArrayList<>());
        for (Flow flow : flows) {

            if (flow.getShortCode().startsWith("*") && flow.getShortCode().endsWith("#")) {
                configuredCodes.get("ussd").add(flow.getShortCode());

            } else if (flow.getShortCode().startsWith("+") || flow.getShortCode().startsWith("0")) {
                configuredCodes.get("whatsapp").add(flow.getShortCode());
            } else {
                configuredCodes.get("other").add(flow.getShortCode());
            }
        }
        return props.getStatusResponse("200_SCRN", configuredCodes);

    }

    public JSONObject updateFlows(String shortcode, Flow updated) {

        if (!flowRepository.existsById(shortcode).block()) {
            return props.getStatusResponse("400_SCRN", "Flow does not exist");
        }
        Flow savedFlow = flowRepository.findById(shortcode).block();
        if (!updated.getShortCode().equals(shortcode)
                || !savedFlow.getOrganization().equalsIgnoreCase(updated.getOrganization())) {
            return props.getStatusResponse("400_SCRN", "Cannot update shortcode or organization");
        }

        try {
            updated.isValidFlow();
        } catch (Exception ex) {
            return props.getStatusResponse("400_SCRN", ex.getMessage());
        }

        updated.setDateCreated(savedFlow.getDateCreated());
        updated.setDateLastModified(Calendar.getInstance().getTime());
        return props.getStatusResponse("200_SCRN", flowRepository.save(updated).block());

    }

    public Flow getFlowInstance(String shortcode) throws Exception{
        if(!flowRepository.existsById(shortcode).block()){
            throw new Exception("Flow Not Found");
        }
        return flowRepository.findById(shortcode).block();
    }

}