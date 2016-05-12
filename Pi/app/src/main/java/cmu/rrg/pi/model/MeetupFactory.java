package cmu.rrg.pi.model;

import java.util.ArrayList;

/**
 * Created by Yang on 4/30/16.
 */
public class MeetupFactory {
    private ArrayList<Meetup> meetups;
    private String category;

    public MeetupFactory(String category) {
        this.category = category;
        this.meetups = new ArrayList<>();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void add(Meetup meetup){
        meetups.add(meetup);
    }

    public int getCount(){
        return meetups.size();
    }

    public ArrayList<Meetup> getMeetups(){
        return this.meetups;
    }
}
