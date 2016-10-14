package com.example.la.myclass.utils;

import android.os.Environment;
import android.util.Log;

import com.example.la.myclass.C;
import com.example.la.myclass.beans.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public boolean addDatabaseToJSONFile(Database database){
        JSONObject dataBaseJSON = database.makeJSON();
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

    public List<Database> getListDatabaseFromJsonFile() {
        List<Database> databaseList = new ArrayList<>();
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
                Database database = new Database();
                database.setLastUpdate((Long) jsonObject.get(Database.JSON_LAST_UPDATE_DATE));
                database.setDate((Long) jsonObject.get(Database.JSON_CREATION_DATE));
                database.setFilePath((String) jsonObject.get(Database.JSON_FILE_PATH));
                database.setCommentaire((String) jsonObject.get(Database.JSON_COMMENTAIRE));
                database.setName((String) jsonObject.get(Database.JSON_NAME));
                database.setSize((Integer) jsonObject.get(Database.JSON_SIZE));
                databaseList.add(database);
                Log.e("MyJSONParser", i + ". Database : " + database);
            }
        } catch (Exception e) {
            Log.e("MyJSONParser", "Unable to get JSON child from JsonArray\n" + e.getMessage());
        }

        return databaseList;
    }

    public Database getDatabaseFromJsonFile(String filePath){
        JSONArray jsonArray = new JSONArray();
        String pathJSON = Environment.getExternalStorageDirectory() + C.PATH_DB_FOLDER + C.JSONFILE_NAME;
        Log.e("MyJSONParser", "getDatabaseFromJsonFile() x " + pathJSON);
        File jsonFile = new File(pathJSON);
        try {
            jsonArray = new JSONArray(C.convertStreamToString(jsonFile));
        } catch (JSONException e) {
            Log.e("MyJSONParser", "Unable to create JSONArray from JsonFile\n" + e.getMessage());
        } catch (IOException e) {
            Log.e("MyJSONParser", "Unable to find JsonFile\n" + e.getMessage());
        }

        try {
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if(jsonObject.get(Database.JSON_FILE_PATH).equals(filePath)) {
                    Database database = new Database();
                    database.setLastUpdate((Long) jsonObject.get(Database.JSON_LAST_UPDATE_DATE));
                    database.setDate((Long) jsonObject.get(Database.JSON_CREATION_DATE));
                    database.setFilePath((String) jsonObject.get(Database.JSON_FILE_PATH));
                    database.setCommentaire((String) jsonObject.get(Database.JSON_COMMENTAIRE));
                    database.setName((String) jsonObject.get(Database.JSON_NAME));
                    database.setSize((Integer) jsonObject.get(Database.JSON_SIZE));
                    Log.e("MyJSONParser", i + ". Database : " + database);
                    return database;
                }
            }
        } catch (Exception e) {
            Log.e("MyJSONParser", "Unable to get JSON child from JsonArray\n" + e.getMessage());
        }

        return null;
    }

    public boolean updateDatabaseInJSON(String filePathDb){

        JSONArray jsonArray;
        Log.e("MyJSONParser", "updateDatabaseInJSON 1x " + filePathDb);
        String pathJSON = Environment.getExternalStorageDirectory() + C.PATH_DB_FOLDER + C.JSONFILE_NAME;
        Log.e("MyJSONParser", "updateDatabaseInJSON 2x " + pathJSON);
        File jsonFile = new File(pathJSON);
        try {
            jsonArray = new JSONArray(C.convertStreamToString(jsonFile));
            Log.e("MyJSONParser", "updateDatabaseInJSON 3x " + jsonArray);
        } catch (JSONException e) {
            Log.e("MyJSONParser", "Unable to create JSONArray from JsonFile\n" + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e("MyJSONParser", "Unable to find JsonFile\n" + e.getMessage());
            return false;
        }

        try {
            Log.e("MyJSONParser", "updateDatabaseInJSON 4x " + jsonArray.length());
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if(jsonObject.get(Database.JSON_FILE_PATH).equals(filePathDb)) {
                    Database database = new Database();
                    database.setLastUpdate(System.currentTimeMillis());
                    database.setDate(jsonObject.getLong(Database.JSON_CREATION_DATE));
                    database.setFilePath(jsonObject.getString(Database.JSON_FILE_PATH));
                    database.setCommentaire(jsonObject.getString(Database.JSON_COMMENTAIRE));
                    database.setName(jsonObject.getString(Database.JSON_NAME));
                    database.setSize(jsonObject.getLong(Database.JSON_SIZE));
                    jsonArray.remove(i);
                    //jsonArray.put(i,database.makeJSON());
                    jsonArray.put(database.makeJSON());
                }

                Log.e("MyJSONParser", "updateDatabaseInJSON 5x " + jsonArray);
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
