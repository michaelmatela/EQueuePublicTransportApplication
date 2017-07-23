package com.example.phmima.equeue;

/**
 * Created by phmima on 7/23/2017.
 */

public class Queue {
    private String terminal;
    private String destination;
    private String queue;
    private String time;
    private String status;

    public Queue(){

    }

    public String getTerminal(){
        return terminal;
    }

    public String getDestination(){
        return destination;
    }

    public String getQueue(){
        return queue;
    }

    public String getTime(){
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setTerminal(String terminal){
        this.terminal = terminal;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public void setQueue (String queue){
        this.queue = queue;
    }

    public void setTime (String time){
        this.time = time;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
