package com.saada.flows.models;

import javax.validation.constraints.NotEmpty;

import org.json.JSONObject;
import org.springframework.lang.NonNull;

public class MessageBirdMessage {

    @NonNull
    @NotEmpty
    private String type;

    private Conversation conversation;

    private Message message;

    private Contact contact;



    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
    public void setMessage(Message message) {
        this.message = message;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Conversation getConversation() {
        return conversation;
    }
    public Message getMessage() {
        return message;
    }
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

    public Contact getContact() {
        return contact;
    }
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    
}