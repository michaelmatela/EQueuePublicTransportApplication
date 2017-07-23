package com.example.phmima.equeue;

/**
 * Created by phmima on 7/23/2017.
 */

public class PUV {
    private String terminal;
    private String destination;
    private String plateNumber;
    private String seat;
    private String available;

    public PUV(){

    }

    public String getTerminal(){
        return terminal;
    }

    public String getDestination(){
        return destination;
    }

    public String getPlateNumber(){
        return plateNumber;
    }

    public String getSeat(){
        return seat;
    }

    public String getAvailable(){
        return available;
    }

    public void setTerminal(String terminal){
        this.terminal = terminal;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public void setPlateNumber(String plateNumber){
        this.plateNumber = plateNumber;
    }

    public void setSeat(String seat){
        this.seat = seat;
    }

    public void setAvailable(String available){
        this.available = available;
    }
}
