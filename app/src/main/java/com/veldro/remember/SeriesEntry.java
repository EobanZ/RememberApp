package com.veldro.remember;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class SeriesEntry {
    public String name;
    public int episode;
    public int season;
    public Object TimestampCreation;
    public Object TimestampChanged;


    SeriesEntry(){}

    SeriesEntry(String name){
        this.name = name;
        this.episode = 0;
        this.season = 0;
        this.TimestampCreation = ServerValue.TIMESTAMP;
        this.TimestampChanged = TimestampCreation;
    }

    SeriesEntry(String name, int season, int episode){
        this.name = name;
        this.episode = episode;
        this.season = season;
        this.TimestampCreation = ServerValue.TIMESTAMP;
        this.TimestampChanged = TimestampCreation;
    }


    public void incSeason(){
        season++;
        updateChangedTimestamp();
    }

    public void incEpisode(){
        episode++;
        updateChangedTimestamp();
    }

    public void decSeason(){
        season--;
        season = season < 0? 0 : season;
        updateChangedTimestamp();
    }

    public void decEpisode(){
        episode--;
        episode = episode < 0? 0 : episode;
        updateChangedTimestamp();
    }

    private void updateChangedTimestamp(){
        this.TimestampChanged = ServerValue.TIMESTAMP;
    }


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("episode", episode);
        result.put("season", season);

        return result;
    }
}
