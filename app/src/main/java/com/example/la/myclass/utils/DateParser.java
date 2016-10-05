package com.example.la.myclass.utils;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gaminho on 22/05/2016.
 */
public class DateParser {

    // Variables de classe
    Calendar mCalendar;


    public DateParser(){
        mCalendar = Calendar.getInstance();
    }

    public String getWeekLabelWithIndex(double timeInMilis){
        Log.e("DEBUGWEEK", "La date " + new Date((long) timeInMilis).toString());
        mCalendar.setTimeInMillis((long) timeInMilis);
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int beginDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int beginMont = mCalendar.get(Calendar.MONTH) + 1;
        int beginYear = mCalendar.get(Calendar.YEAR) - 2000;
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        int endDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int endMont = mCalendar.get(Calendar.MONTH) + 1;
        int endYear = mCalendar.get(Calendar.YEAR) - 2000;

        return String.format("Lu. %02d/%02d/%02d\nDi. %02d/%02d/%02d", beginDay, beginMont, beginYear, endDay, endMont, endYear);

    }

    public String getWeekLabelWithIndex2(double indexOfWeek){
        Log.e("DEBUGWEEK2", "La semaine max est "+indexOfWeek);
        mCalendar.set(Calendar.WEEK_OF_YEAR, (int) indexOfWeek + 1);
        Log.e("DEBUGWEEK2", "La semaine max est " + mCalendar.getTime().toString());
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.e("DEBUGWEEK2", "Le lundi max est " + mCalendar.getTime().toString());
        int beginDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int beginMont = mCalendar.get(Calendar.MONTH) + 1;
        int beginYear = mCalendar.get(Calendar.YEAR) - 2000;
        Log.e("DEBUGWEEK2", String.format("Lu. %02d/%02d/%02d",beginDay,beginMont,beginYear));

        mCalendar.set(Calendar.WEEK_OF_YEAR, (int) indexOfWeek);
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        int endDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int endMont = mCalendar.get(Calendar.MONTH) + 1;
        int endYear = mCalendar.get(Calendar.YEAR) - 2000;
        Log.e("DEBUGWEEK2", String.format("Di. %02d/%02d/%02d",endDay,endMont,endYear));

        return String.format("Lu. %02d/%02d/%02d\nDi. %02d/%02d/%02d", beginDay, beginMont, beginYear, endDay, endMont, endYear);
    }

    public String getMonthLabelWithIndex(double timeInMilis){
        String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        mCalendar.setTimeInMillis((long) timeInMilis);
        //mCalendar.set(Calendar.MONTH, (int) indexOfMonth);
        int year = mCalendar.get(Calendar.YEAR);

        return String.format("%s %d", months[mCalendar.get(Calendar.MONTH)], year);
    }

    public String getReadableDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.format("%d%02d%02d_%02d%02d%02d",
                calendar.get(Calendar.YEAR),
                (calendar.get(Calendar.MONTH)+1),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }


}
