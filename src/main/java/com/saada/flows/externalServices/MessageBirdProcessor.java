package com.saada.flows.externalServices;

import java.util.HashMap;

import com.saada.flows.models.MessageBirdMessage;
import com.saada.flows.services.FlowService;
import com.saada.flows.services.SessionService;
import com.saada.flows.utils.Utils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class MessageBirdProcessor {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private FlowService flowService;

    @Value("${message.bird.baseurl}")
    private String conversationBaseUrl;

    @Value("${message.bird.accesskey}")
    private String accesskey;

    public void coreProcessor(MessageBirdMessage message) {

        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, Object> request = new HashMap<>();

        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.put("Authorization", "AccessKey " + accesskey);

        request.put("type", "text");

        String session = message.getConversation().getId();
        String input = message.getMessage().getContent().get("text").toString();
        if (message.getType().equalsIgnoreCase("message.created")
                && flowService.isAvailable(message.getMessage().getTo())) {

            String url = conversationBaseUrl + message.getConversation().getId() + "/messages";

            String response = sessionService.screenNavigate(session, input, message.getMessage().getTo(),
                    message.getContact().getMsisdn());
            HashMap<String, String> text = new HashMap<>();
            text.put("text", response.substring(3));

            request.put("content", text);

            try {

                if (response.startsWith("END")) {
                    String urlend = conversationBaseUrl + message.getConversation().getId();
                    Utils.patchRequest(urlend, headers, new JSONObject().put("status", "archived").toString());
                }
                Utils.postRequest(url, new JSONObject(request).toString(), headers);
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println(
                        ">>>>>>>>>>>>>>>>>>>>>>> " + e.getLocalizedMessage() + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        }
    }
}