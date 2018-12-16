package com.veldro.remember;

public class SeriesEntry {
    private String m_name;
    private int m_episode;
    private int m_season;
    private int m_minutes;

    SeriesEntry(){}

    SeriesEntry(String name){
        m_name = name;
        m_episode = 0;
        m_season = 0;
        m_minutes = 0;
    }

    SeriesEntry(String name, int episode, int season){
        m_name = name;
        m_episode = episode;
        m_season = season;
        m_minutes = 0;
    }

    SeriesEntry(String name, int episode, int season, int minutes){
        m_name = name;
        m_episode = episode;
        m_season = season;
        m_minutes = minutes;
    }

    public String getName(){
        return m_name;
    }

    public int getEpisode(){
        return m_episode;
    }

    public int getSeason(){
        return m_season;
    }

    public int getMinutes(){
        return m_minutes;
    }

    public void incSeason(){
        m_season++;
    }

    public void incEpisode(){
        m_episode++;
    }

    public void decSeason(){
        m_season--;
        m_season = m_season < 0? 0 : m_season;
    }

    public void decEpisode(){
        m_episode--;
        m_episode = m_episode < 0? 0 : m_episode;
    }
}
