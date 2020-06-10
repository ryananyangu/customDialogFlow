package com.custom.dialog.lab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.saada.flows.utils.Utils;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

public class AdhocTests {

    public static void main(String[] args) throws ClientProtocolException, IOException {
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");



        HashMap<String,List<HashMap<String,Object>>> payload = new HashMap<>();

        List<HashMap<String,Object>> items = new ArrayList<>();
        
        HashMap<String,Object> item = new HashMap<>();
        item.put("name", "\n Onions");
        item.put("quantity", 1);

        items.add(item);

        payload.put("items", items);
        

        Utils.postRequest("https://api.farmula.ng/ussd/get_order", new JSONObject(payload).toString(), headers);
        
    }

}