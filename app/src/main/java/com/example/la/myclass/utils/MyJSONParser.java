package com.example.la.myclass.utils;

import android.os.Environment;
import android.util.Log;

import com.example.la.myclass.C;
import com.example.la.myclass.beans.MyDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ariche on 11/10/2016.
 */

public class MyJSONParser {

    /**
     * Variables
     */
    protected JSONObject mJSONObject;
    protected JSONArray mJSONArray;
    protected String mJSONString;

    /**
     * Constructeur
     */
    public MyJSONParser() {
    }

    public MyJSONParser(String jsonString) {
        this.mJSONString = jsonString;
        try {
            this.mJSONArray = new JSONArray(jsonString);

        } catch (JSONException e) {
            Log.e("MyJSONParser", "Unable to create JSONArray from this string\n" + e.getMessage());
            //e.printStackTrace();
        }
        try {
            this.mJSONObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.e("MyJSONParser", "Unable to create JSONObject from this string\n" + e.getMessage());
            //e.printStackTrace();
        }

    }

    /**
     * Utils
     */
    public boolean addDatabaseToJSONFile(MyDb myDb){
        JSONObject dataBaseJSON = myDb.makeJSON();
        String jsonContent = "";
        JSONArray jsonArray = new JSONArray();

        File jsonFile = new File(Environment.getExternalStorageDirectory() + C.PATH_DB_FOLDER);
        if(!jsonFile.exists())
            jsonFile.mkdirs();

        jsonFile = new File(jsonFile, C.JSONFILE_NAME);
        if (!jsonFile.exists())
            try {
                jsonFile.createNewFile();
            } catch (IOException e) {
                Log.e("MyJSONParser", "Unable to create the file " + jsonFile
                        + "\n" + e.getMessage());
            }

        try {
            jsonContent = C.convertStreamToString(jsonFile);
            Log.e("MyJSONParser", "Content of JsonFile : " + jsonContent);
        } catch (IOException e) {
            Log.e("MyJSONParser", "Unable to create JSONArray from this string\n" + e.getMessage());
            return false;
        }

        try {
            jsonArray = new JSONArray(jsonContent);
        } catch (JSONException e) {
            Log.e("MyJSONParser", "Unable to create JSONArray from JsonFileContent\n" + e.getMessage());
            e.printStackTrace();
        }

        jsonArray.put(dataBaseJSON);

        Log.e("MyJSONParser", "JSON FILE : " + jsonArray);

        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(jsonFile));
            output.write(jsonArray.toString());
        } catch ( IOException e ) {
            Log.e("MyJSONParser", "Unable to write JSONArray in JsonFile\n" + e.getMessage());
            return false;
        } finally {
            if ( output != null ) {
                try {
                    output.close();
                } catch (IOException e) {
                    Log.e("MyJSONParser", "Unable to close writer in JsonFile\n" + e.getMessage());
                }
            }
        }

        return true;
    }

    public List<MyDb> getListDatabaseFromJsonFile() {
        List<MyDb> myDbList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        String pathJSON = Environment.getExternalStorageDirectory() + C.PATH_DB_FOLDER + C.JSONFILE_NAME;
        Log.e("MyJSONParser", "getListDatabaseFromJsonFile() x " + pathJSON);
        File jsonFile = new File(pathJSON);
        try {
            jsonArray = new JSONArray(C.convertStreamToString(jsonFile));
        } catch (JSONException e) {
            Log.e("MyJSONParser", "Unable to create JSONArray from JsonFile\n" + e.getMessage());
        } catch (IOException e) {
            Log.e("MyJSONParser", "Unable to find JsonFile\n" + e.getMessage());
        }

        Log.e("MyJSONParser", "Number of databases : " +  jsonArray.length());

        try {
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                MyDb myDb = new MyDb();
                myDb.setLastUpdate((Long) jsonObject.get(MyDb.JSON_LAST_UPDATE_DATE));
                myDb.setDate((Long) jsonObject.get(MyDb.JSON_CREATION_DATE));
                myDb.setFilePath((String) jsonObject.get(MyDb.JSON_FILE_PATH));
                myDb.setCommentaire((String) jsonObject.get(MyDb.JSON_COMMENTAIRE));
                myDb.setName((String) jsonObject.get(MyDb.JSON_NAME));
                myDb.setSize((Integer) jsonObject.get(MyDb.JSON_SIZE));
                myDbList.add(myDb);
                Log.e("MyJSONParser", i + ". MyDb : " + myDb);
            }
        } catch (Exception e) {
            Log.e("MyJSONParser", "Unable to get JSON child from JsonArray\n" + e.getMessage());
        }

        return myDbList;
    }

    public boolean updateDatabaseInJSON(String filePathDb){

        JSONArray jsonArray;
        String pathJSON = Environment.getExternalStorageDirectory() + C.PATH_DB_FOLDER + C.JSONFILE_NAME;
        File jsonFile = new File(pathJSON);
        try {
            jsonArray = new JSONArray(C.convertStreamToString(jsonFile));
        } catch (JSONException e) {
            Log.e("MyJSONParser", "Unable to create JSONArray from JsonFile\n" + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e("MyJSONParser", "Unable to find JsonFile\n" + e.getMessage());
            return false;
        }

        try {
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if(jsonObject.get(MyDb.JSON_FILE_PATH).equals(filePathDb)) {
                    MyDb myDb = new MyDb();
                    myDb.setLastUpdate(System.currentTimeMillis());
                    myDb.setDate((Long) jsonObject.get(MyDb.JSON_CREATION_DATE));
                    myDb.setFilePath((String) jsonObject.get(MyDb.JSON_FILE_PATH));
                    myDb.setCommentaire((String) jsonObject.get(MyDb.JSON_COMMENTAIRE));
                    myDb.setName((String) jsonObject.get(MyDb.JSON_NAME));
                    myDb.setSize((Integer) jsonObject.get(MyDb.JSON_SIZE));
                    jsonArray.remove(i);
                    jsonArray.put(i,myDb.makeJSON());
                }
            }
        } catch (Exception e) {
            Log.e("MyJSONParser", "Unable to get JSON child from JsonArray\n" + e.getMessage());
            return false;
        }

        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(jsonFile));
            output.write(jsonArray.toString());
        } catch ( IOException e ) {
            Log.e("MyJSONParser", "Unable to write JSONArray in JsonFile\n" + e.getMessage());
            return false;
        } finally {
            if ( output != null ) {
                try {
                    output.close();
                } catch (IOException e) {
                    Log.e("MyJSONParser", "Unable to close writer in JsonFile\n" + e.getMessage());
                }
            }
        }

        return true;
    }
}
