package com.saada.flows.externalServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saada.flows.models.Screen;
import com.saada.flows.models.Session;

import com.saada.flows.utils.Props;
import com.saada.flows.utils.Utils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalRequestProcessor {

    private Screen screen;

    private String phoneNumber;

    private Session session;

    @Autowired
    private Props props;

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String processRequest(String payload, String url) throws Exception {
        if (url.isEmpty()) {
            url = screen.getNodeExtraData().get("url");
        }
        // TODO: Make generic
        String processed = new String();
        try {
            if ("POST".equalsIgnoreCase(screen.getNodeExtraData().get("method"))) {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                processed = Utils.postRequest(url, payload, headers);
            } else if ("GET".equalsIgnoreCase(screen.getNodeExtraData().get("method"))) {
                processed = Utils.getRequest(url, new HashMap<>());
            } else {
                throw new Exception(props.getFlowError("6") + ">> <<" + Utils.getCodelineNumber());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getLocalizedMessage() + ">> <<" + Utils.getCodelineNumber());
        }

        return processed;
    }

    public String preparePayload(String sessionId) throws Exception {
        if (sessionId.isEmpty()) {
            return new String();

        }

        String[] items = session.getExtraData().get("start_page").toString().split(",");
        String[] quantity = session.getExtraData().get("SelectProceed").toString().split(",");
        HashMap<String, List<HashMap<String, Object>>> formattedItems = new HashMap<>();
        formattedItems.put("items", new ArrayList<>());
        for (int i = 0; i < items.length; i++) {

            HashMap<String, Object> itemObj = new HashMap<>();

            itemObj.put("name", Utils.rmNewlineTab(items[i]).split("-")[0].trim());

            try {
                itemObj.put("quantity", Integer.parseInt(quantity[i]));
            } catch (Exception ex) {
                break;
            }
            formattedItems.get("items").add(itemObj);

        }
        return new JSONObject(formattedItems).toString();
    }

    public Screen processResponse() throws Exception {
        if (screen.getNodeName().equalsIgnoreCase("start_page")) {
            Map<String, Object> response = new JSONObject(processRequest(preparePayload(""), "")).toMap();
            List<HashMap<String, Object>> items = (List<HashMap<String, Object>>) response.get("items");
            List<String> options = new ArrayList<>();
            for (HashMap<String, Object> item : items) {
                options.add(item.get("Crop").toString() + " - N" + item.get("Price").toString() + " per 50Kg Sack");
            }
            screen.setNodeOptions(options);
            screen.setScreenType("options");
            screen.validateOptions();
        } else if ("Complete_FinalSummary".equalsIgnoreCase(screen.getNodeName())) {

            List<HashMap<String, String>> screen_items = new ArrayList<>();
            HashMap<String, String> continue_next = new HashMap<>();
            continue_next.put("displayText", "Continue");
            continue_next.put("nextScreen", screen.getScreenNext());

            screen_items.add(continue_next);

            HashMap<String, String> cancel_next = new HashMap<>();
            cancel_next.put("displayText", "Cancel");
            cancel_next.put("nextScreen", "start_page");
            screen_items.add(cancel_next);

            String payload = preparePayload(session.getSessionId());
            Map<String, Object> response = new JSONObject(processRequest(payload, "")).toMap();
            List<HashMap<String, Object>> items = (List<HashMap<String, Object>>) response.get("items");

            screen.setScreenText(screen.getScreenText() + processResponse(items));
            screen.setNodeItems(screen_items);
            screen.setScreenNext("");
            screen.setScreenType("items");
            screen.validateItems();
            return screen;

        } else if ("Order_complete".equalsIgnoreCase(screen.getNodeName())) {

            String payload = preparePayload(session.getSessionId());

            String url = "https://api.farmula.ng/ussd/get_order";
            Map<String, Object> response1 = new JSONObject(processRequest(payload, url)).toMap();

            


            List<HashMap<String, Object>> items = (List<HashMap<String, Object>>) response1.get("items");
            for (HashMap<String, Object> item : items) {
                if (item.containsKey("GrandTotal")) {
                    item.put("contact", phoneNumber);
                    break;
                }

            }

            response1.put("items", items);
            response1.remove("message");
            response1.remove("status");
            System.out.println(response1+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
            Map<String, Object> finalreponse = new JSONObject(processRequest(new JSONObject(response1).toString(), ""))
                    .toMap();
            System.out.println(finalreponse+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            String orderno = finalreponse.get("OrderNumber").toString();

            HashMap<String, String> extraData = screen.getNodeExtraData();
            
            List<HashMap<String, String>> screen_items = new ArrayList<>();
            HashMap<String, String> continue_next = new HashMap<>();
            continue_next.put("displayText", "Exit");
            continue_next.put("nextScreen", "end");

            screen_items.add(continue_next);

            HashMap<String, String> cancel_next = new HashMap<>();
            cancel_next.put("displayText", "Add order");
            cancel_next.put("nextScreen", "start_page");
            screen_items.add(cancel_next);

            String text = screen.getScreenText()+" " + orderno + " of " + processResponse(items);
            screen.setScreenText(text);
            screen.setNodeItems(screen_items);
            screen.setScreenNext("");
            screen.setScreenType("items");
            screen.validateItems();
            screen.setNodeExtraData(extraData);
            return screen;

        }

        return screen;
    }

    public String processResponse(List<HashMap<String, Object>> items) {
        String summary = new String();

        String total = new String();

        for (HashMap<String, Object> item : items) {
            if (item.containsKey("Crop")) {
                summary += "\n" + item.get("Crop") + " : " + item.get("Qty") + " * " + item.get("UnitPrice") + " = "
                        + item.get("TotalCost");
            } else {
                total = "\n ------------------- \n Grand Total " + item.get("GrandTotal");
            }

        }

        return summary + total;
    }

}