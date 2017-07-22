package com.example.phmima.equeue;


public class Profile {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String terminal;

    public Profile (){

    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getTerminal(){
        return terminal;
    }

    public void setTerminal(String terminal){
        this.terminal = terminal;
    }
}
