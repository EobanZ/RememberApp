package com.veldro.remember;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class AudioBookEntry {
    public String name;
    public int chapter;
    public Object TimestampCreation;
    public Object TimestampChanged;

    public AudioBookEntry(){}

    public AudioBookEntry(String name, int chapter){
        this.name = name;
        this.chapter = chapter;
        TimestampCreation = TimestampChanged = ServerValue.TIMESTAMP;
    }

    public AudioBookEntry(String name){
        this(name,0);
    }

    public void incChapter(){
        chapter++;
        updateChangedTimestamp();
    }

    public void decChapter(){
        chapter--;
        chapter = chapter < 0? 0 : chapter;
        updateChangedTimestamp();
    }

    private void updateChangedTimestamp() {
        this.TimestampChanged = ServerValue.TIMESTAMP;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("chapter", chapter);
        result.put("TimestapChanged", TimestampChanged);
        result.put("TimestapCreation", TimestampCreation);

        return result;
    }
}
