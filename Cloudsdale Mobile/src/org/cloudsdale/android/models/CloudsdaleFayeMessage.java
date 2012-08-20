package org.cloudsdale.android.models;

import com.b3rwynmobile.fayeclient.models.FayeData;

import org.cloudsdale.android.ISO8601;
import org.cloudsdale.android.models.api_models.User;

import java.text.ParseException;
import java.util.Calendar;

public class CloudsdaleFayeMessage extends FayeData {
    
    private User user;
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Calendar getTimestampAsCalendar() {
        try {
            return ISO8601.toCalendar(getTimestamp());
        } catch (ParseException e) {
            return Calendar.getInstance();
        }
    }
    
    public void setTimestampFromCalendar(Calendar calendar) {
        setTimestamp(ISO8601.fromCalendar(calendar));
    }

}
