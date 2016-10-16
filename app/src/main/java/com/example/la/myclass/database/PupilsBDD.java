package com.example.la.myclass.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Pupil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by L�a on 27/09/2015.
 */

public class PupilsBDD {

    private static final String NOM_BDD = MyDatabase.DB_NAME;

    public static final String TABLE_PUPILS = "pupils_table";
    public static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    public static final String COL_NAME = "NAME";
    private static final int NUM_COL_NAME = 1;
    public static final String COL_SEX = "SEX";
    private static final int NUM_COL_SEX = 2;
    public static final String COL_CLASS = "CLASS";
    private static final int NUM_COL_CLASS = 3;
    public static final String COL_TYPE = "TYPE";
    private static final int NUM_COL_TYPE = 4;
    public static final String COL_FREQUENCE = "FREQUENCE";
    private static final int NUM_COL_FREQUENCE = 5;
    public static final String COL_ADRESS = "ADRESS";
    private static final int NUM_COL_ADRESS = 6;
    public static final String COL_PRICE = "PRICE";
    private static final int NUM_COL_PRICE = 7;
    public static final String COL_DATE_SINCE = "SINCE";
    private static final int NUM_COL_DATE_SINCE = 8;
    public static final String COL_TEL_1 = "TEL1";
    private static final int NUM_COL_TEL_1 = 9;
    public static final String COL_TEL_2 = "TEL2";
    private static final int NUM_COL_TEL_2 = 10;
    public static final String COL_IMG_PATH = "IMG_PATH";
    private static final int NUM_COL_IMG_PATH = 11;
    public static final String COL_STATE = "STATE";
    private static final int NUM_COL_STATE = 12;

    public static final String[] PUPILS_FIELDS = new String[]{COL_ID, COL_NAME, COL_SEX, COL_CLASS,
                    COL_TYPE, COL_FREQUENCE, COL_ADRESS,COL_PRICE, COL_DATE_SINCE, COL_TEL_1,
                    COL_TEL_2, COL_IMG_PATH, COL_STATE};

    private SQLiteDatabase bdd;

    private MyDatabase maBaseSQLite;

    public PupilsBDD(Context context) {
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

    public long insertPupils(Pupil pupil) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, pupil.getName());
        values.put(COL_SEX, pupil.getSex());
        values.put(COL_CLASS, pupil.getLevel());
        values.put(COL_TYPE, pupil.getType());
        values.put(COL_FREQUENCE, pupil.getFrequency());
        values.put(COL_ADRESS, pupil.getAdress());
        values.put(COL_PRICE, pupil.getPrice());
        values.put(COL_DATE_SINCE, new Date().getTime());
        values.put(COL_TEL_1, pupil.getTel1());
        values.put(COL_TEL_2, pupil.getTel2());
        values.put(COL_IMG_PATH, pupil.getImgPath());
        values.put(COL_STATE, pupil.getState());

        return bdd.insert(TABLE_PUPILS, null, values);
    }

    public int updatePupil(int id, Pupil pupil){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, pupil.getName());
        values.put(COL_SEX, pupil.getSex());
        values.put(COL_CLASS, pupil.getLevel());
        values.put(COL_TEL_1, pupil.getTel1());
        values.put(COL_TEL_2, pupil.getTel2());
        values.put(COL_ADRESS, pupil.getAdress());
        values.put(COL_PRICE, pupil.getPrice());
        values.put(COL_TYPE, pupil.getType());
        values.put(COL_FREQUENCE, pupil.getFrequency());
        values.put(COL_DATE_SINCE, pupil.getSinceDate());
        values.put(COL_IMG_PATH, pupil.getImgPath());
        values.put(COL_STATE, pupil.getState());

        return bdd.update(TABLE_PUPILS, values, COL_ID + " = " + id, null);
    }

    public int removePupilWithID(int id) {
        return bdd.delete(TABLE_PUPILS, COL_ID + " = " + id, null);
    }

    public int removePupilWithName(String name){
        return bdd.delete(TABLE_PUPILS, COL_NAME + " LIKE \"" + name + "\"", null);
    }

    public Pupil getPupilWithId(int id) {
        Cursor c = bdd.query(TABLE_PUPILS, PUPILS_FIELDS, COL_ID + " = " + id, null, null, null, null);
        return cursorToPupil(c);
    }

    public static Pupil getPupileWithId(Context context, int pupilID){
        PupilsBDD pupilsBDD = new PupilsBDD(context);
        pupilsBDD.open();
        Pupil pupil = pupilsBDD.getPupilWithId(pupilID);
        pupilsBDD.close();
        return pupil;
    }

    public List<Pupil> getPupilsWithCriteria(String criteria) {
        Cursor c = bdd.query(TABLE_PUPILS, PUPILS_FIELDS, criteria, null, null, null, COL_CLASS);
        return cursorToListPupils(c);
    }

    private List<Pupil> cursorToListPupils(Cursor c) {

        List<Pupil> list = new ArrayList<>();
        if (c.getCount() == 0)
            return list;

        c.moveToFirst();
        while (!c.isAfterLast()) {
            Pupil pupil = new Pupil();
            pupil.setId(c.getInt(NUM_COL_ID));
            pupil.setName(c.getString(NUM_COL_NAME));
            pupil.setSex(c.getInt(NUM_COL_SEX));
            pupil.setLevel(c.getInt(NUM_COL_CLASS));
            pupil.setType(c.getInt(NUM_COL_TYPE));
            pupil.setFrequency(c.getInt(NUM_COL_FREQUENCE));
            pupil.setAdress(c.getString(NUM_COL_ADRESS));
            pupil.setPrice(c.getDouble(NUM_COL_PRICE));
            pupil.setSinceDate(c.getLong(NUM_COL_DATE_SINCE));
            pupil.setTel1(c.getLong(NUM_COL_TEL_1));
            pupil.setTel2(c.getLong(NUM_COL_TEL_2));
            pupil.setImgPath(c.getString(NUM_COL_IMG_PATH));
            pupil.setState(c.getInt(NUM_COL_STATE));

            list.add(pupil);
            c.moveToNext();
        }

        c.close();
        return list;
    }

    public static Pupil cursorToPupil(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Pupil pupil = new Pupil();
        pupil.setId(c.getInt(NUM_COL_ID));
        pupil.setName(c.getString(NUM_COL_NAME));
        pupil.setSex(c.getInt(NUM_COL_SEX));
        pupil.setLevel(c.getInt(NUM_COL_CLASS));
        pupil.setType(c.getInt(NUM_COL_TYPE));
        pupil.setFrequency(c.getInt(NUM_COL_FREQUENCE));
        pupil.setAdress(c.getString(NUM_COL_ADRESS));
        pupil.setPrice(c.getDouble(NUM_COL_PRICE));
        pupil.setSinceDate(c.getLong(NUM_COL_DATE_SINCE));
        pupil.setTel1(c.getLong(NUM_COL_TEL_1));
        pupil.setTel2(c.getLong(NUM_COL_TEL_2));
        pupil.setImgPath(c.getString(NUM_COL_IMG_PATH));
        pupil.setState(c.getInt(NUM_COL_STATE));

        c.close();

        return pupil;
    }

    public List<Pupil> getAllPupils(){
        Cursor c = bdd.query(TABLE_PUPILS, PUPILS_FIELDS, null, null, null, null, COL_CLASS);
        return cursorToListPupils(c);
    }

    public List<Pupil> getActivePupils(){
        Cursor c = bdd.query(TABLE_PUPILS, PUPILS_FIELDS, PupilsBDD.COL_STATE + " != " + Pupil.DESACTIVE, null, null, null, COL_CLASS);
        return cursorToListPupils(c);
    }

    public int getNumberOfPupils() {
        Cursor c = bdd.rawQuery("SELECT * FROM " + TABLE_PUPILS, null);
        return c.getCount();
    }

    public double getMoneyWithPupilID(int pupilID) {
        Cursor c = bdd.query(CoursesBDD.TABLE_COURSES, CoursesBDD.COURSES_FIELDS, CoursesBDD.COL_PUPIL_ID + " = " + pupilID, null, null, null, null);
        double money = 0;
        if (c.getCount() == 0)
            return money;

        c.moveToFirst();
        while (!c.isAfterLast()) {
            if(c.getInt(CoursesBDD.NUM_COL_STATE) == Course.VALIDATED)
                money += c.getDouble(CoursesBDD.NUM_COL_MONEY);
            c.moveToNext();
        }
        c.close();
        return money;
    }



}