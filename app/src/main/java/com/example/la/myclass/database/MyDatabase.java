package com.example.la.myclass.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

/**
 * Created by Léa on 27/09/2015.
 */
public class MyDatabase extends SQLiteOpenHelper {

    public static final int VERSION_BDD = 26;
    public static final String DB_NAME = "myclassdb";
    private static final String TABLE_PUPILS = "pupils_table";
    private static final String TABLE_PUPILS_2 = "pupils_table_2";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_SEX = "SEX";
    private static final String COL_CLASS = "CLASS";
    private static final String COL_TYPE = "TYPE";
    private static final String COL_FREQUENCE = "FREQUENCE";
    private static final String COL_ADRESS = "ADRESS";
    private static final String COL_PRICE = "PRICE";
    private static final String COL_DATE_SINCE = "SINCE";
    private static final String COL_TEL_1 = "TEL1";
    private static final String COL_TEL_2 = "TEL2";
    private static final String COL_IMG_PATH = "IMG_PATH";


    private static final String TABLE_COURSES = "courses_table";
    private static final String TABLE_COURSES_2 = "courses_table_2";
    private static final String COL_PUPIL_NAME = "PUPIL_NAME";
    private static final String COL_DATE = "DATE";
    private static final String COL_DURATION = "DURATION";
    private static final String COL_STATE = "STATE";
    private static final String COL_MONEY = "MONEY";
    private static final String COL_THEME = "THEME";
    private static final String COL_MEMO = "MEMO";
    private static final String COL_PUPIL_ID = "PUPIL_ID";

    private static final String TABLE_DEVOIRS = "devoirs_table";
    private static final String TABLE_DEVOIRS_2 = "devoirs_table_2";
    private static final String COL_NOTE = "NOTE";
    private static final String COL_COMMENTAIRE = "COMMENTAIRE";
    private static final String COL_BAREM = "BAREM";


    private static final String CREATE_PUPILS_BDD = "CREATE TABLE " + TABLE_PUPILS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT NOT NULL, "
            + COL_SEX + " INTEGER, " + COL_CLASS + " TEXT NOT NULL, "
            + COL_TYPE + " INTEGER, " + COL_FREQUENCE + " INTEGER, "
            + COL_ADRESS + " TEXT NOT NULL, " + COL_PRICE + " DOUBLE, "
            + COL_DATE_SINCE + " LONG DEFAULT " + new Date().getTime() + ", "
            + COL_TEL_1 + " LONG DEFAULT 0, " + COL_TEL_2 + " LONG DEFAULT 0, "
            + COL_IMG_PATH + " TEXT );";

    private static final String CREATE_PUPILS_BDD_2 = "CREATE TABLE " + TABLE_PUPILS_2 + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT NOT NULL, "
            + COL_SEX + " INTEGER, " + COL_CLASS + " TEXT NOT NULL, "
            + COL_TYPE + " INTEGER, " + COL_FREQUENCE + " INTEGER, "
            + COL_ADRESS + " TEXT NOT NULL, " + COL_PRICE + " DOUBLE, "
            + COL_DATE_SINCE + " LONG DEFAULT " + new Date().getTime() + ", "
            + COL_TEL_1 + " LONG DEFAULT 0, " + COL_TEL_2 + " LONG DEFAULT 0, "
            + COL_IMG_PATH + " TEXT );";

    private static final String CREATE_COURSES_BDD = "CREATE TABLE " + TABLE_COURSES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_PUPIL_NAME + " TEXT NOT NULL, "
            + COL_DATE + " LONG, " + COL_DURATION + " INTEGER, "
            + COL_STATE + " INTEGER, " + COL_MONEY + " DOUBLE, "
            + COL_THEME + " TEXT, " + COL_MEMO + " TEXT, "
            + COL_PUPIL_ID + " INTEGER );";

    private static final String CREATE_COURSES_BDD_2 = "CREATE TABLE " + TABLE_COURSES_2 + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_DATE + " LONG, " + COL_DURATION + " INTEGER, "
            + COL_STATE + " INTEGER, " + COL_MONEY + " DOUBLE, "
            + COL_THEME + " TEXT, " + COL_MEMO + " TEXT, "
            + COL_PUPIL_ID + " INTEGER );";

    private static final String CREATE_DEVOIRS_BDD = "CREATE TABLE " + TABLE_DEVOIRS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_THEME + " TEXT NOT NULL, "
            + COL_DATE + " LONG, " + COL_NOTE + " DOUBLE, " + COL_COMMENTAIRE + " TEXT, "
            + COL_STATE + " INTEGER DEFAULT 0, " + COL_PUPIL_ID + " INTEGER, "
            + COL_BAREM + " INTEGER DEFAULT 20, " + COL_PUPIL_ID + " INTEGER DEFAULT 0 );";

    private static final String CREATE_DEVOIRS_BDD_2 = "CREATE TABLE " + TABLE_DEVOIRS_2 + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_THEME + " TEXT NOT NULL, "
            + COL_DATE + " LONG, " + COL_NOTE + " DOUBLE, " + COL_COMMENTAIRE + " TEXT, "
            + COL_STATE + " INTEGER DEFAULT 0, " + COL_PUPIL_ID + " INTEGER );";

    public MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PUPILS_BDD);
        db.execSQL(CREATE_COURSES_BDD);
        db.execSQL(CREATE_DEVOIRS_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE " + TABLE_PUPILS + ";");
        //if you need to add a new column
        if(newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " + TABLE_DEVOIRS + " ADD COLUMN " + COL_BAREM + " INT DEFAULT 20");
            db.execSQL("ALTER TABLE " + TABLE_DEVOIRS + " ADD COLUMN " + COL_TYPE + " INT DEFAULT 0");
            //db.execSQL("ALTER TABLE " + TABLE_PUPILS + " ADD COLUMN " + COL_TEL_1 + " LONG DEFAULT 0");
            //db.execSQL("ALTER TABLE " + TABLE_PUPILS + " ADD COLUMN " + COL_TEL_2 + " LONG DEFAULT 0");
            //db.execSQL("ALTER TABLE " + TABLE_PUPILS + " ADD COLUMN " + COL_NB_COURSES + " INTEGER DEFAULT 0");

            //db.execSQL(CREATE_COURSES_BDD);
            Log.e("BDD", "UPGRADE_DB");


            /**
             * Algorithme pour supprimer une colonne
             */

            /*
            db.execSQL(CREATE_DEVOIRS_BDD_2);
            db.execSQL("INSERT INTO " + TABLE_DEVOIRS_2 + " SELECT " + COL_ID + "," + COL_THEME + "," + COL_DATE + ","
                    + COL_NOTE + "," + COL_COMMENTAIRE + "," + COL_STATE + "," + COL_PUPIL_ID + " FROM " + TABLE_DEVOIRS);
            db.execSQL("DROP TABLE " + TABLE_DEVOIRS);
            db.execSQL(CREATE_DEVOIRS_BDD);
            db.execSQL("INSERT INTO " + TABLE_DEVOIRS + " SELECT " + COL_ID + "," + COL_THEME + "," + COL_DATE + ","
                    + COL_NOTE + "," + COL_COMMENTAIRE + "," + COL_STATE + "," + COL_PUPIL_ID + " FROM " + TABLE_DEVOIRS_2);
            db.execSQL("DROP TABLE " + TABLE_DEVOIRS_2);
            Log.e("BDD", "OK");
            */

        }
        //onCreate(db);
    }

}
