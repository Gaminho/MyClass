package com.example.la.myclass.beans;

import com.example.la.myclass.C;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Gaminho on 24/08/2016.
 */
public class MyDB {

    protected long date;
    protected String name;
    protected double size;



    public MyDB(File file){
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

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "MyDB{" +
                "date=" + date +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
