package com.veldro.remember;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class BookEntry {
    public String name;
    public int page;
    public Object TimestampCreation;
    public Object TimestampChanged;

    BookEntry(){}

    BookEntry(String name){
        this.name = name;
        this.page = 0;
        this.TimestampCreation = this.TimestampChanged = ServerValue.TIMESTAMP;
    }

    BookEntry(String name, int page){
        this.name = name;
        this.page = page;
        this.TimestampCreation = this.TimestampChanged = ServerValue.TIMESTAMP;
    }

    public void setPage(int i){
        i= i<0?0:i;
        this.page = i;
        updateChangedTimestamp();
    }

    private void updateChangedTimestamp() {
        this.TimestampChanged = ServerValue.TIMESTAMP;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("page", page);
        result.put("TimestapChanged", TimestampChanged);
        result.put("TimestapCreation", TimestampCreation);

        return result;
    }
}
