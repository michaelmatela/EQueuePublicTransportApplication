package com.example.phmima.equeue;

/**
 * Created by phmima on 8/28/2017.
 */

public class News {
    private String id;
    private String timestamp;
    private String body;

    public News(){}

    public String getId (){
        return id;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getBody(){
        return body;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public void setBody(String body){
        this.body = body;
    }
}
