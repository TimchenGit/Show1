package com.example.show.model;

public class MessageEvent {
    public int state;
    public String incomingNumber;

    public MessageEvent(int state,String incomingNumber){
        this.state = state;
        this.incomingNumber = incomingNumber;
    }
}
