package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Utils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session {

    private final static Logger LOGGER = Logger.getLogger(Session.class.getName());

    private final String phoneNumber;
    private final String sessionId;
    private final String input;
    Firestore database;

    public Session(String phoneNumber, String sessionId, String Input) {
        database = FirestoreOptions.getDefaultInstance().getService();
        this.phoneNumber = phoneNumber;
        this.sessionId = sessionId;
        this.input = Input;
    }

    public Map retrieveData(String collection, String documentId) {

        LOGGER.log(Level.INFO,
                Utils.prelogString(sessionId,
                        Utils.getCodelineNumber(), "Data submitted to function :: collection = " + collection + " :: documentID = " + documentId));

        DocumentSnapshot document;
        try {

            CollectionReference collectionReference = database.collection(collection);
            DocumentReference documentReference = collectionReference.document(documentId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            document = future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return new HashMap();
        }

        if (!document.exists()) {
            LOGGER.log(Level.INFO,
                    Utils.prelogString(sessionId,
                            Utils.getCodelineNumber(), "Document Does not exist"));
            return new HashMap<>();
        }

        return document.getData();
    }

    public Map screenNavigate(Map<String, Object> sessionData) {
        LOGGER.log(Level.INFO,
                Utils.prelogString(sessionId,
                        Utils.getCodelineNumber(), "Data submitted to function :: " + sessionData),
                sessionData);
        // means it is the first screen
        if (sessionData.isEmpty()) {

            return getNextScreenDetails(input, "start_page");

        }

        HashMap<String, Object> currentScreenData = (HashMap) sessionData.get("screenData");
        String screenType = String.valueOf(currentScreenData.get("screenType"));
        String shortCode = String.valueOf(sessionData.get("shortCode"));

        // session already existed
        if ("raw_input".equalsIgnoreCase(screenType) || "options".equalsIgnoreCase(screenType)) {
            String screenNext = String.valueOf(currentScreenData.get("nextScreen"));

            return getNextScreenDetails(shortCode, screenNext);

        } else if ("items".equalsIgnoreCase(screenType)) {
            List<HashMap> nodeItems = (List) currentScreenData.get("nodeItems");
            String nextScreen = String.valueOf(nodeItems.get(Integer.parseInt(input) - 1).get("nextScreen"));
            return getNextScreenDetails(shortCode, nextScreen);

        } else {
            return new HashMap();
        }
    }

    public String displayText(Map<String, Object> screenData) {
        LOGGER.log(Level.INFO,
                Utils.prelogString(sessionId,
                        Utils.getCodelineNumber(), "Data submitted to function :: " + screenData),
                screenData);
        if (screenData.isEmpty()) {
            LOGGER.log(Level.SEVERE,
                    Utils.prelogString(sessionId,
                            Utils.getCodelineNumber(), "failed database transaction or invalid screen type"),
                    screenData);
            return "Service Temporarily unavailable";
        }

        String screenType = String.valueOf(screenData.get("screenType"));
        String screenText = String.valueOf(screenData.get("screenText"));
        if ("raw_input".equalsIgnoreCase(screenType)) {
            return screenText;
        } else if ("items".equalsIgnoreCase(screenType)) {

            List<HashMap> nodeItems = (List) screenData.get("nodeItems");
            int count = 1;
            for (HashMap item : nodeItems) {
                screenText += "\n" + count + ". " + String.valueOf(item.get("displayText"));
                count++;
            }
            return screenText;
        } else {
            int count = 1;
            List<String> nodeOptions = (List) screenData.get("nodeOptions");

            for (String opt : nodeOptions) {
                screenText += "\n" + count + ". " + opt;
                count++;
            }

            return screenText;
        }

    }

    public Map getNextScreenDetails(String shortCode, String screenNext) {
        LOGGER.log(Level.INFO,
                Utils.prelogString(sessionId,
                        Utils.getCodelineNumber(), "Data submitted to function :: shortCode = " + shortCode + " :: screenNext = " + screenNext));
        HashMap<String, Object> data = new HashMap<>();
        data.put("shortCode", shortCode);
        data.put("currentPage", screenNext);
        data.put("ExtraData", "");

        Map<String, Object> nextScreenData = retrieveData(shortCode, screenNext);
        if (nextScreenData.isEmpty()) {
            // document/screen does not exist
            return new HashMap();
        }
        data.put("screenData", nextScreenData);
        boolean isSuccess;
        if ("start_page".equalsIgnoreCase(screenNext)) {
            isSuccess = saveData(data, "sessions", sessionId);
        } else {
            isSuccess = updateData(data, "sessions", sessionId);
        }

        if (isSuccess) {
            return nextScreenData;
        }

        // else session screen already associated to sessionID
        // found exception or Failed saving in database
        return new HashMap();
    }

    public boolean saveData(Map<String, Object> data, String collection, String documentId) {

        CollectionReference collectionReference = database.collection(collection);
        DocumentReference documentReference = collectionReference.document(documentId);

        Map docData = retrieveData(collection, documentId);

        if (docData.isEmpty()) {
            documentReference.set(data);
            return true;
        }

        return false;

    }

    private boolean updateData(HashMap<String, Object> data, String collection, String documentId) {

        CollectionReference collectionReference = database.collection(collection);
        DocumentReference documentReference = collectionReference.document(documentId);

        Map docData = retrieveData(collection, documentId);

        if (docData.isEmpty()) {
            documentReference.update(data);
            return true;
        }

        return false;

    }

}
