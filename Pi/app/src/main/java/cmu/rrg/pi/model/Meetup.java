package cmu.rrg.pi.model;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zhangyihe on 4/10/16.
 */
public class Meetup implements Serializable {
    private int id;
    private String name;
    private String content;
    private String avatar;
    private String category;

    private String address;
    private double latitude;
    private double longitude;

    private int status;

    private String startTime;     //time stored in milliseconds from 1970
    private String endTime;       //time stored in milliseconds from 1970

    private ArrayList<Integer> members; //signup user id


    public Meetup(int id, String name, String content, String category, String address, double latitude,
                  double longitude, int status, String startTime, String endTime) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;

        this.members = new ArrayList<>();
    }

    public Meetup(String name, String content, String category, String address, double latitude,
                  double longitude, int status, String startTime, String endTime) {
        this.id = 0;
        this.name = name;
        this.content = content;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;

        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setMembers(ArrayList<Integer> members) {
        this.members = members;
    }

    public void getMembers(){
        this.members = MeetupADO.getMembers(this.id);
    }

    /*
         * member list manipulation
         */
    public boolean addMember(int userId){
        ArrayList<Integer> res = MeetupADO.signupMeetup(this.id, userId);
        if(res == null){
            return false;
        }else{
            this.members = res;
            return true;
        }
    }

    public void delMember(int userId){
        this.members.remove(userId);
    }

    public int getCount(){
        return this.members.size();
    }

    public boolean userPartice(User user) {
        if (members.contains(user.getId())) {
            return true;
        } else {
            return false;
        }

    }

}
