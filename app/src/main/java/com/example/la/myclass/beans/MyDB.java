package com.example.la.myclass.beans;

import android.util.Log;
import android.widget.Toast;

import com.example.la.myclass.C;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Gaminho on 24/08/2016.
 */
public class MyDb {

    /**
     * Statics Variables
     */
    public static final String JSON_CREATION_DATE = "creationDate";
    public static final String JSON_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String JSON_SIZE = "size";
    public static final String JSON_NAME = "name";
    public static final String JSON_COMMENTAIRE = "comments";
    public static final String JSON_FILE_PATH = "filePath";
    /**
     * Variables
     */
    protected int id;
    protected long date, size, lastUpdate;
    protected String name, commentaire, filePath;


    public MyDb() {
    }

    public MyDb(File file){
        this.name = file.getName();
        Calendar calendar = Calendar.getInstance();
        int offset = this.name.indexOf(C.NAME_EXPORTED_DB) + C.NAME_EXPORTED_DB.length();
        calendar.set(Calendar.YEAR, Integer.parseInt(this.name.substring(offset, offset + 4)));
        offset+=4;
        calendar.set(Calendar.MONTH, (Integer.parseInt(this.name.substring(offset, offset+2))-1));
        offset+=2;
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(this.name.substring(offset, offset+2)));
        offset+=3;
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.name.substring(offset, offset+2)));
        offset +=2;
        calendar.set(Calendar.MINUTE, Integer.parseInt(this.name.substring(offset, offset+2)));
        offset +=2;
        calendar.set(Calendar.SECOND, Integer.parseInt(this.name.substring(offset, offset+2)));
        this.date = calendar.getTimeInMillis();

        this.size = file.length();
    }


    // Getters and setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "MyDb{" +
                "id=" + id +
                ", date=" + date +
                ", size=" + size +
                ", lastUpdate=" + lastUpdate +
                ", name='" + name + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    public JSONObject makeJSON(){
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put(JSON_NAME, this.name);
            jsonObject.put(JSON_COMMENTAIRE, this.commentaire);
            jsonObject.put(JSON_CREATION_DATE, this.date);
            jsonObject.put(JSON_FILE_PATH, this.filePath);
            jsonObject.put(JSON_LAST_UPDATE_DATE, this.lastUpdate);
            jsonObject.put(JSON_SIZE, this.size);
        } catch (JSONException e) {
            Log.e("MyDb", "Unable to make JSONObject from this database\n"+e.getMessage());
        }

        return jsonObject;
    }
}
