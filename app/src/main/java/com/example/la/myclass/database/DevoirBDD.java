package com.example.la.myclass.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Léa on 02/10/2015.
 */
public class DevoirBDD {

    private static final String NOM_BDD = MyDatabase.DB_NAME;

    private static final String TABLE_DEVOIRS = "devoirs_table";
    public static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    public static final String COL_THEME = "THEME";
    private static final int NUM_COL_THEME = 1;
    public static final String COL_DATE = "DATE";
    private static final int NUM_COL_DATE = 2;
    public static final String COL_NOTE = "NOTE";
    private static final int NUM_COL_NOTE = 3;
    public static final String COL_COMMENTAIRE = "COMMENTAIRE";
    private static final int NUM_COL_COMMENTAIRE  = 4;
    public static final String COL_STATE = "STATE";
    private static final int NUM_COL_STATE  = 5;
    public static final String COL_PUPIL_ID = "PUPIL_ID";
    private static final int NUM_COL_PUPIL_ID  = 6;
    public static final String COL_TYPE = "TYPE";
    private static final int NUM_COL_TYPE  = 7;
    public static final String COL_BAREM = "BAREM";
    private static final int NUM_COL_BAREM  = 8;
    public static final String[] DEVOIR_FIELDS = {COL_ID, COL_THEME, COL_DATE, COL_NOTE, COL_COMMENTAIRE, COL_STATE, COL_PUPIL_ID,COL_TYPE,COL_BAREM};



    private SQLiteDatabase bdd;

    private MyDatabase maBaseSQLite;

    public DevoirBDD(Context context) {
        maBaseSQLite = new MyDatabase(context, NOM_BDD, null, MyDatabase.VERSION_BDD);
    }

    public void open() {
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        bdd.close();
    }

    public SQLiteDatabase getBDD() {
        return bdd;
    }

    public long insertDevoir(Devoir devoir) {
        ContentValues values = new ContentValues();
        values.put(COL_THEME, devoir.getTheme());
        values.put(COL_DATE, devoir.getDate());
        values.put(COL_NOTE, devoir.getNote());
        values.put(COL_COMMENTAIRE, devoir.getCommentaire());
        values.put(COL_STATE, devoir.getState());
        values.put(COL_PUPIL_ID, devoir.getPupilID());
        values.put(COL_TYPE, devoir.getType());
        values.put(COL_BAREM, devoir.getBarem());

        return bdd.insert(TABLE_DEVOIRS, null, values);
    }

    public int updateDevoir(int id, Devoir devoir){
        ContentValues values = new ContentValues();
        values.put(COL_THEME, devoir.getTheme());
        values.put(COL_DATE, devoir.getDate());
        values.put(COL_NOTE, devoir.getNote());
        values.put(COL_COMMENTAIRE, devoir.getCommentaire());
        values.put(COL_STATE, devoir.getState());
        values.put(COL_PUPIL_ID, devoir.getPupilID());
        values.put(COL_TYPE, devoir.getType());
        values.put(COL_BAREM, devoir.getBarem());

        return bdd.update(TABLE_DEVOIRS, values, COL_ID + " = " + id, null);
    }

    public int removeDevoirWithID(int id) {
        return bdd.delete(TABLE_DEVOIRS, COL_ID + " = " + id, null);
    }

    public Devoir getDevoirWithId(int id) {
        Cursor c = bdd.query(TABLE_DEVOIRS, DEVOIR_FIELDS, COL_ID + " = " + id, null, null, null, null);
        return cursorToDevoir(c);
    }

    public List<Devoir> getDevoirWithCriteria(String criteria) {
        Cursor c = bdd.query(TABLE_DEVOIRS, DEVOIR_FIELDS, criteria, null, null, null, COL_DATE + " DESC");
        return cursorToListDevoirs(c);
    }

    private Devoir cursorToDevoir(Cursor c) {

        if (c.getCount() == 0)
            return null;

        c.moveToFirst();

        Devoir devoir = new Devoir();
        devoir.setId(c.getInt(NUM_COL_ID));
        devoir.setTheme(c.getString(NUM_COL_THEME));
        devoir.setDate(c.getLong(NUM_COL_DATE));
        devoir.setNote(c.getDouble(NUM_COL_NOTE));
        devoir.setCommentaire(c.getString(NUM_COL_COMMENTAIRE));
        devoir.setState(c.getInt(NUM_COL_STATE));
        devoir.setPupilID(c.getInt(NUM_COL_PUPIL_ID));
        Cursor c0 = bdd.query(PupilsBDD.TABLE_PUPILS, PupilsBDD.PUPILS_FIELDS, COL_ID + " = " + c.getInt(NUM_COL_PUPIL_ID), null, null, null, null);
        devoir.setPupil(PupilsBDD.cursorToPupil(c0));
        devoir.setBarem(c.getInt(NUM_COL_BAREM));
        devoir.setType(c.getInt(NUM_COL_TYPE));
        c0.close();

        c.close();

        return devoir;
    }

    private List<Devoir> cursorToListDevoirs(Cursor c) {
        List<Devoir> list = new ArrayList<>();
        if (c.getCount() == 0)
            return list;

        c.moveToFirst();

        while (!c.isAfterLast()) {
            Devoir devoir = new Devoir();
            devoir.setId(c.getInt(NUM_COL_ID));
            devoir.setTheme(c.getString(NUM_COL_THEME));
            devoir.setDate(c.getLong(NUM_COL_DATE));
            devoir.setNote(c.getDouble(NUM_COL_NOTE));
            devoir.setCommentaire(c.getString(NUM_COL_COMMENTAIRE));
            devoir.setState(c.getInt(NUM_COL_STATE));
            devoir.setPupilID(c.getInt(NUM_COL_PUPIL_ID));
            Cursor c0 = bdd.query(PupilsBDD.TABLE_PUPILS, PupilsBDD.PUPILS_FIELDS, COL_ID + " = " + c.getInt(NUM_COL_PUPIL_ID), null, null, null, null);
            devoir.setPupil(PupilsBDD.cursorToPupil(c0));
            devoir.setBarem(c.getInt(NUM_COL_BAREM));
            devoir.setType(c.getInt(NUM_COL_TYPE));
            c0.close();
            list.add(devoir);
            c.moveToNext();
        }

        c.close();

        return list;
    }

    public List<Devoir> getAllDevoirs(){
        Cursor c = bdd.query(TABLE_DEVOIRS, DEVOIR_FIELDS, null, null, null, null, COL_DATE + " DESC");
        return cursorToListDevoirs(c);
    }

    public List<Devoir> getActiveDevoirs(){
        Cursor c = bdd.query(TABLE_DEVOIRS, DEVOIR_FIELDS, DevoirBDD.COL_STATE + " != " + Devoir.STATE_CANCELED + " AND " + DevoirBDD.COL_STATE + " != " + Devoir.STATE_PREPARATING, null, null, null, COL_DATE + " DESC");
        return cursorToListDevoirs(c);
    }

    public int getNumberOfDevoirs() {
        Cursor c = bdd.rawQuery("SELECT * FROM " + TABLE_DEVOIRS, null);
        return c.getCount();
    }

    public List<Devoir> getDevoirsWithState(int state) {
        Cursor c = bdd.query(TABLE_DEVOIRS, DEVOIR_FIELDS, COL_STATE + " = " + state, null, null, null, COL_DATE + " DESC");
        return cursorToListDevoirs(c);
    }

    public List<Devoir> getDevoirsWithPupilID(int pupilID) {
        Cursor c = bdd.query(TABLE_DEVOIRS, DEVOIR_FIELDS, COL_PUPIL_ID + " = " + pupilID + " AND " + COL_STATE + " = " + Devoir.STATE_DONE, null, null, null, COL_DATE + " ASC");
        return cursorToListDevoirs(c);
    }

    public List<Devoir> getDevoirsBetweenTwoDates(long start, long end){

        Cursor c = bdd.query(TABLE_DEVOIRS, DEVOIR_FIELDS, COL_STATE + " != " + Devoir.STATE_CANCELED, null, null, null, null);
        List<Devoir> list = new ArrayList<>();
        if (c.getCount() == 0)
            return list;

        c.moveToFirst();

        while (!c.isAfterLast()) {
            long date = c.getLong(NUM_COL_DATE);
            if(date > start && date < end) {
                Devoir devoir = new Devoir();
                devoir.setId(c.getInt(NUM_COL_ID));
                devoir.setTheme(c.getString(NUM_COL_THEME));
                devoir.setDate(date);
                devoir.setNote(c.getDouble(NUM_COL_NOTE));
                devoir.setCommentaire(c.getString(NUM_COL_COMMENTAIRE));
                devoir.setState(c.getInt(NUM_COL_STATE));
                devoir.setPupilID(c.getInt(NUM_COL_PUPIL_ID));
                Cursor c0 = bdd.query(PupilsBDD.TABLE_PUPILS, PupilsBDD.PUPILS_FIELDS, COL_ID + " = " + c.getInt(NUM_COL_PUPIL_ID), null, null, null, null);
                devoir.setPupil(PupilsBDD.cursorToPupil(c0));
                devoir.setBarem(c.getInt(NUM_COL_BAREM));
                devoir.setType(c.getInt(NUM_COL_TYPE));
                c0.close();
                list.add(devoir);
            }
            c.moveToNext();
        }

        c.close();

        return list;
    }

    public Devoir getNextDevoir(){
        Cursor c = bdd.query(TABLE_DEVOIRS, DEVOIR_FIELDS, COL_STATE + " = " + Devoir.STATE_PREPARATING, null, null, null, COL_DATE, "1");
        return cursorToDevoir(c);
    }

    public static Devoir getNextDevoir(Context context){
        DevoirBDD devoirBDD = new DevoirBDD(context);
        devoirBDD.open();
        Devoir devoir = devoirBDD.getNextDevoir();
        devoirBDD.close();

        return devoir;
    }

    public static List<Devoir> getForeseenDevoir(Context context){
        DevoirBDD devoirBDD = new DevoirBDD(context);
        devoirBDD.open();
        List<Devoir> devoirList = devoirBDD.getDevoirsWithState(Devoir.STATE_PREPARATING);
        devoirBDD.close();

        return devoirList;
    }

    public static Devoir getDevoirWithId(Context context, int devoirID){
        DevoirBDD devoirBDD = new DevoirBDD(context);
        devoirBDD.open();
        Devoir devoir = devoirBDD.getDevoirWithId(devoirID);
        devoirBDD.close();
        return devoir;
    }

    public static void changeDevoirState(Context context, Devoir devoir, int newState){
        DevoirBDD devoirBDD = new DevoirBDD(context);
        devoirBDD.open();
        devoir.setState(newState);
        devoirBDD.updateDevoir(devoir.getId(), devoir);
        devoirBDD.close();
    }

    public List<Devoir> getListDevoirForADay(long timestampOfTheDay){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampOfTheDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long begin = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long end = calendar.getTimeInMillis();

        String criteria = " 1 AND " + COL_DATE + " > " + begin
                + " AND " + COL_DATE + " < " + end + " AND "
                + COL_STATE + " != " + Devoir.STATE_CANCELED;
        return getDevoirWithCriteria(criteria);
    }

    public List<Devoir> getListDevoirForAWeek(long timestampOfTheDay){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampOfTheDay);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long begin = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_YEAR, 6);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long end = calendar.getTimeInMillis();

        String criteria = " 1 AND " + COL_DATE + " > " + begin
                + " AND " + COL_DATE + " < " + end + " AND "
                + COL_STATE + " != " + Devoir.STATE_CANCELED;
        return getDevoirWithCriteria(criteria);
    }

    public List<Devoir> getListDevoirForAMonth(long timestampOfTheDay){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampOfTheDay);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long begin = calendar.getTimeInMillis();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long end = calendar.getTimeInMillis();

        String criteria = " 1 AND " + COL_DATE + " > " + begin
                + " AND " + COL_DATE + " < " + end + " AND "
                + COL_STATE + " != " + Devoir.STATE_CANCELED;
        return getDevoirWithCriteria(criteria);
    }
}
//259
