package cmu.rrg.pi.model;

import java.io.Serializable;

/**
 * Created by Yang on 4/30/16.
 */
public class User implements Serializable {
    private int id;
    private String username;
    private String name;
    private String phoneNumber;
    private String password;
    //private String avatar;

    //for register
    public User(String username, String name, String phoneNumber, String password) {
        this.username = username;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        //this.avatar = avatar;
    }

    //for login
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String name, String phoneNumber, String password){
        this.id = id;
        this.username = username;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /*public String getAvatar() {
        return avatar;
    }*/

    /*public void setAvatar(String avatar) {
        this.avatar = avatar;
    }*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
