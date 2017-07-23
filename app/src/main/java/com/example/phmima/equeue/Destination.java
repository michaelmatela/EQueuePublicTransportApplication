package com.example.phmima.equeue;


public class Destination {
    private String terminal;
    private String destination;
    private String photo;

    public Destination(){

    }

    public String getTerminal(){
        return terminal;
    }

    public String getDestination(){
        return destination;
    }

    public String getPhoto(){
        return photo;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }
}
