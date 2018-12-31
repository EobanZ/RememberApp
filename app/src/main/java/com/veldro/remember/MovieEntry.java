package com.veldro.remember;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class MovieEntry {

    public String name;
    public int minutes;
    public Object TimestampCreation;
    public Object TimestampChanged;


    MovieEntry(){}

    MovieEntry(String name){
        this.name = name;
        this.minutes = 0;
        this.TimestampCreation = ServerValue.TIMESTAMP;
        this.TimestampChanged = TimestampCreation;
    }

    MovieEntry(String name, int minutes){
        this.name = name;
        this.minutes = minutes;
        this.TimestampCreation = ServerValue.TIMESTAMP;
        this.TimestampChanged = TimestampCreation;
    }

    public void setMinutes(int i){
        i= i<0?0:i;
        this.minutes = i;
        updateChangedTimestamp();
    }

    private void updateChangedTimestamp(){
        TimestampChanged = ServerValue.TIMESTAMP;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("minutes", minutes);

        return result;
    }
}
