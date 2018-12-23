package com.veldro.remember;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class SeriesEntry {
    public String name;
    public int episode;
    public int season;

    SeriesEntry(){}

    SeriesEntry(String name){
        this.name = name;
        this.episode = 0;
        this.season = 0;
    }

    SeriesEntry(String name, int season, int episode){
        this.name = name;
        this.episode = episode;
        this.season = season;

    }


    public void incSeason(){
        season++;
    }

    public void incEpisode(){
        episode++;
    }

    public void decSeason(){
        season--;
        season = season < 0? 0 : season;
    }

    public void decEpisode(){
        episode--;
        episode = episode < 0? 0 : episode;
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
