package com.me.plan.picme.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Alon on 04/03/2018.
 */

@Entity(tableName = "pictures")
public class Picture {
    @PrimaryKey
    @NonNull
    public String id;

    public String title;
    public String date;
    public String url;
    public String user;
    public float lastUpdated;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public float getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(float lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    HashMap<String,Object> toJson(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("date", date);
        result.put("url", url);
        result.put("lastUpdated", lastUpdated);
        return result;
    }
}
