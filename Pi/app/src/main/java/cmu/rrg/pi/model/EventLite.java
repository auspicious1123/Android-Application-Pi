package cmu.rrg.pi.model;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by zhangyihe on 4/4/16.
 * this class is for list showing event
 * most of the time the app do not need to request the whole information of an event
 * another table in backend is required for this class
 */
public class EventLite {

    private String eventTitle;
    //private String hostName;
    private int hostInfoId;
    //private String eventTime;
    //private String eventLoc;
    public EventLite(String eventTitle, int hostInfoId) {
        this.eventTitle = eventTitle;
        //this.hostName = hostName;
        this.hostInfoId = hostInfoId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    /*public String getHostName() {
        return hostName;
    }*/

    public int getHostInfoId() {
        return hostInfoId;
    }

}

