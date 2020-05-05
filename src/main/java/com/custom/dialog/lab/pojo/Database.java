/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.pojo;

import com.custom.dialog.lab.utils.Utils;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author jovixe
 */
@Component
public class Database {

    @Autowired
    Firestore firestore;

    private final static Logger LOGGER = Logger.getLogger(Database.class.getName());

    public boolean updateData(HashMap<String, Object> data, String collection, String documentId) {

        CollectionReference collectionReference = firestore.collection(collection);
        DocumentReference documentReference = collectionReference.document(documentId);

        Map docData = retrieveData(collection, documentId);
        if (!docData.isEmpty()) {
            documentReference.update(data);
            documentReference.get();
            return true;
        }

        return false;

    }

    // Bad function does 2 things save and update
    public boolean saveData(HashMap<String, Object> data, String collection, String documentId) {

        CollectionReference collectionReference = this.firestore.collection(collection);
        DocumentReference documentReference = collectionReference.document(documentId);

        Map docData = retrieveData(collection, documentId);

        if (docData.isEmpty()) {
            documentReference.set(data);
            documentReference.get();
            return true;
        }
        return updateData(data, collection, documentId);

    }

    public Map retrieveData(String collection, String documentId) {

        LOGGER.log(Level.INFO, Utils.prelogString("", Utils.getCodelineNumber(),
                "Data submitted to function :: collection = " + collection + " :: documentID = " + documentId));

        DocumentSnapshot document;
        try {

            CollectionReference collectionReference = this.firestore.collection(collection);
            DocumentReference documentReference = collectionReference.document(documentId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            document = future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            LOGGER.log(Level.SEVERE, Utils.prelogString("", Utils.getCodelineNumber(),
                    "Error retrieving data >> " + ex.getLocalizedMessage()));
            return new HashMap();
        }

        if (!document.exists()) {
            LOGGER.log(Level.INFO, Utils.prelogString("", Utils.getCodelineNumber(), "Document Does not exist"));
            return new HashMap<>();
        }

        return document.getData();
    }

    public HashMap retrieveDataList(String collection) {
        List<QueryDocumentSnapshot> documents;
        HashMap<String, Object> screenData = new HashMap<>();
        try {
            ApiFuture<QuerySnapshot> future = this.firestore.collection(collection).get();
            documents = future.get(10, TimeUnit.SECONDS).getDocuments();
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            LOGGER.log(Level.INFO, Utils.prelogString("", Utils.getCodelineNumber(), "Document Does not exist"));
            return new HashMap<>();
        }

        documents.forEach((document) -> {
            screenData.put(document.getId(), document.getData());
        });

        return screenData;

    }

    public boolean deleteData(String collection, String documentId, boolean isSync) {
        LOGGER.log(Level.INFO, Utils.prelogString("", Utils.getCodelineNumber(),
                "Data submitted to function :: collection = " + collection + " :: documentID = " + documentId));
        try {
            ApiFuture<WriteResult> writeResult = this.firestore.collection(collection).document(documentId).delete();
            if (isSync) {
                writeResult.get(10, TimeUnit.SECONDS).getUpdateTime();
            }
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            return false;
        }
        return true;
    }
}
