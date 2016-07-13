package com.acukanov.hivet.events;


public class ChatMessageSent {
    private long id;

    public ChatMessageSent() {

    }

    public ChatMessageSent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
