package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Props;
import com.custom.dialog.lab.utils.Utils;
import com.google.api.core.ApiFuture;
import java.util.HashMap;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

@Data
public class FlowGraph {

    private static final Props SETTINGS = new Props();
    private Firestore database;
    private final static Logger LOGGER = Logger.getLogger(FlowGraph.class.getName());

    private String shortcode;

    public FlowGraph() {
        Firestore db_instance = FirestoreOptions.getDefaultInstance().getService();
        database = db_instance;
    }

    public JSONObject getFlow(String shortcode) {
        ApiFuture<QuerySnapshot> future = database.collection(shortcode).get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(FlowGraph.class.getName()).log(Level.SEVERE, null, ex);
            return new JSONObject();
        }
        Map<String, Object> screenData = new HashMap<>();
        documents.forEach((document) -> {
            screenData.put(document.getId(), document.getData());
        });
        return new JSONObject(screenData);

    }
}
