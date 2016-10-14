package com.example.la.myclass;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.la.myclass.beans.MyDb;
import com.example.la.myclass.database.MyDatabase;
import com.example.la.myclass.utils.DateParser;
import com.example.la.myclass.utils.MyJSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gaminho on 23/08/2016.
 */
public class C {

    /**
     * DIVERS CONSTANTS
     */
    public static final String STRING_SEPARATOR = ";;;";

    /**
     * ACTIVITY RESULTS CODES
     */
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int RESULT_PIC_CROP = 2;

    /**
     * PATHS
     */
    public static final String PATH_DB_FOLDER = "/MyClass/databases/";
    public static final String PATH_PHOTOS_FOLDER = "/MyClass/pupils/photos/";

    /**
     * JSONFile for databases
     */
    public static final String JSONFILE_NAME = "databases.json";
    public static String convertStreamToString(File file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
        StringBuilder sb = new StringBuilder();
        String line = null;
        Boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if(firstLine){
                sb.append(line);
                firstLine = false;
            } else {
                sb.append("\n").append(line);
            }
        }
        reader.close();
        fin.close();
        return sb.toString();
    }

    /**
     * DATABASES
     */
    public static final String NO_DB = "Aucune base de données n'a été exportée";
    public static final String NAME_EXPORTED_DB = "myClassDB_";
    public static final String SYSTEM_PATH_TO_DATABASE = "/data/com.example.la.myclass/databases/";
    public static void exportDB(Context context){

        File sd = new File(Environment.getExternalStorageDirectory()  + C.PATH_DB_FOLDER);
        if(!sd.exists())
            sd.mkdirs();

        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = C.SYSTEM_PATH_TO_DATABASE + MyDatabase.DB_NAME;
        String backupDBPath = C.NAME_EXPORTED_DB + new DateParser().getReadableDate(new Date()) + ".sql";

        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "La base de données a bien été exportée.", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            Log.e("DEBUG", "Failure => " + e);
            e.printStackTrace();
        }
    }
    /**
     * Method to export the new database
     * @param context
     * @param name : name of the new database
     * @param comment : comment about the new database
     * @return
     */
    public static String exportDB2(Context context, String name, String comment){

        File sd = new File(Environment.getExternalStorageDirectory()  + C.PATH_DB_FOLDER);
        if(!sd.exists())
            sd.mkdirs();

        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = C.SYSTEM_PATH_TO_DATABASE + MyDatabase.DB_NAME;
        String backupDBPath = C.NAME_EXPORTED_DB + new DateParser().getReadableDate(new Date()) + ".sql";

        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);

        try {
            MyDb myDb = new MyDb();
            myDb.setFilePath(backupDBPath);
            myDb.setName(name);
            myDb.setCommentaire(comment);
            myDb.setFilePath(sd + "/" + backupDBPath);
            myDb.setSize(new File(data,currentDBPath).length());
            myDb.setDate(System.currentTimeMillis());
            myDb.setLastUpdate(System.currentTimeMillis());

            new MyJSONParser().addDatabaseToJSONFile(myDb);

            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();

            Toast.makeText(context, "La base de données a bien été exportée.", Toast.LENGTH_SHORT).show();
            return myDb.getFilePath();
        } catch(IOException e) {
            Log.e("DEBUG", "Failure => " + e);
            //e.printStackTrace();
        }
        return null;
    }
    public static boolean importDB(Context context, String nameDB, boolean keepOldDB){

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data  = Environment.getDataDirectory();

            if (sd.canWrite()) {
                //Le fichier à remplacer
                String currentDBPath = C.SYSTEM_PATH_TO_DATABASE + MyDatabase.DB_NAME;
                // Le nouveau fichier
                String backupDBPath  = C.PATH_DB_FOLDER + nameDB;

                File backupDB   = new File(data, currentDBPath);
                File currentDB  = new File(sd, backupDBPath);

                if(keepOldDB)
                    exportDB(context);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, "La base de données a été remplacée.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public static boolean importDB2(Context context, String filepath){

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data  = Environment.getDataDirectory();

            if (sd.canWrite()) {
                //Le fichier à remplacer
                String currentDBPath = C.SYSTEM_PATH_TO_DATABASE + MyDatabase.DB_NAME;

                File backupDB   = new File(data, currentDBPath);
                File currentDB  = new File(filepath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, "La base de données a été remplacée.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public static boolean updateDB(Context context){

        File sd = new File(Environment.getExternalStorageDirectory()  + C.PATH_DB_FOLDER);
        if(!sd.exists())
            sd.mkdirs();

        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(C.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        String backupDBPath = sharedPreferences.getString(C.CURRENT_DB, C.NO_DB);

        if(!new File(backupDBPath).exists()) {
            Toast.makeText(context, "La base de données est introuvable.", Toast.LENGTH_SHORT).show();
            return false;
        }

        String currentDBPath = C.SYSTEM_PATH_TO_DATABASE + MyDatabase.DB_NAME;

        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();

            new MyJSONParser().updateDatabaseInJSON(backupDBPath);

            Toast.makeText(context, "La base de données a été mise à jour.", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            Log.e("DEBUG", "Failure => " + e);
        }

        return true;
    }
    /**
     * HOURS CONSTANT
     */
    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;

    /**
     * SHARED PREFERENCES
     */
    public static final String SHARED_PREFERENCES_NAME = "mySharedPreferences";
    public static final String CURRENT_DB = "current_database";
    public static final String SP_NOTIF_COURSE_BEGIN = "course_begin";
    public static final String SP_NOTIF_COURSE_END = "course_end";
    public static final String SP_NOTIF_DEVOIR_BEGIN = "devoir_begin";
    public static final String SP_NOTIF_DEVOIR_END = "devoir_end";
    public static final String SP_NOTIF_DATABASE_UPDATE = "database_update";
    public static final String SP_UPDATE_DB_DELAY = "database_update_delay";

    /**
     * DATE
     */
    public static final String[] DAYS = {"","Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi"};
    public static final String[] MONTHS = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
    public static final int DATE_FORMAT_LONG_DAY = 0;
    public static final int DATE_FORMAT_SHORT_DAY = 1;
    public static final int DATE_FORMAT_LONG_MONTH = 2;
    public static final int DATE_FORMAT_SHORT_MONTH = 3;
    public static final int DATE_FORMAT_FULL_DATE = 4;
    public static final int DAY_DATE_D_MONTH_YEAR = 5;
    public static final int DAY_DATE_D_MONTH = 6;
    public static final int MONTH_YEAR = 7;
    public static final int DD_MM_YY = 8;
    public static final int HH_mm = 9;
    public static final int HH_mm_ss = 10;
    public static final int dd_HH_mm_ss = 11;
    public static String formatDate(long milliseconds, int format){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(milliseconds));

        switch(format){
            case DATE_FORMAT_LONG_DAY :
                return null;
            case DATE_FORMAT_SHORT_DAY :
                return null;
            case DATE_FORMAT_SHORT_MONTH :
                return String.format("%s %02d/%02d/%02d",
                        DAYS[calendar.get(Calendar.DAY_OF_WEEK)].substring(0,2)+".",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        (calendar.get(Calendar.MONTH) + 1),
                        (calendar.get(Calendar.YEAR) - 2000));
            case DATE_FORMAT_LONG_MONTH :
                return null;
            case DD_MM_YY :
                return String.format("%02d/%02d/%02d",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        (calendar.get(Calendar.MONTH) + 1),
                        (calendar.get(Calendar.YEAR) - 2000));
            case DATE_FORMAT_FULL_DATE :
                return String.format("%02d/%02d/%02d %02dh%02d:%02d",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        (calendar.get(Calendar.MONTH) + 1),
                        (calendar.get(Calendar.YEAR) - 2000),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND));
            case DAY_DATE_D_MONTH_YEAR:
                return String.format("%s %02d %s %d",
                        DAYS[calendar.get(Calendar.DAY_OF_WEEK)],
                        calendar.get(Calendar.DAY_OF_MONTH),
                        MONTHS[calendar.get(Calendar.MONTH)],
                        calendar.get(Calendar.YEAR));
            case DAY_DATE_D_MONTH:
                return String.format("%s %02d %s",
                        DAYS[calendar.get(Calendar.DAY_OF_WEEK)],
                        calendar.get(Calendar.DAY_OF_MONTH),
                        MONTHS[calendar.get(Calendar.MONTH)]);
            case MONTH_YEAR:
                return String.format("%s %d",
                        MONTHS[calendar.get(Calendar.MONTH)],
                        calendar.get(Calendar.YEAR));
            case HH_mm:
                return String.format("%02dh%02d",
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
            case HH_mm_ss:
                return String.format("%02dh%02d:%02d",
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND));
            case dd_HH_mm_ss:
                int jour = (int) (milliseconds / DAY);
                milliseconds -= jour*DAY;
                int hour = (int) (milliseconds/HOUR);
                milliseconds -= hour*HOUR;
                int minut = (int) (milliseconds/MINUTE);
                milliseconds -= minut*MINUTE;
                int second = (int) (milliseconds/SECOND);
                return String.format("%dj. %02dh%02d:%02d", jour,hour,minut,second);
        }
        return null;
    }

    /**
     * SIZE
     */
    public static final double SIZE_KILO_OCTET = 1024;
    public static final double SIZE_MEGA_OCTET = 1024 * SIZE_KILO_OCTET;
    public static String formatSize(double size){
        if(size > SIZE_MEGA_OCTET)
            return String.format("%.2f mo", size/SIZE_MEGA_OCTET);

        if(size > SIZE_KILO_OCTET)
            return String.format("%.2f ko", size/SIZE_KILO_OCTET);

        return String.format("%.2f o", size);
    }


    /**
     * FONT AWESOME
     */
    public static final String FA_COURSE_WAITING_FOR_VALIDATION = "{fa-hourglass-2}";
    public static final String FA_COURSE_VALIDATED = "{fa-check}";
    public static final String FA_COURSE_FORESSEN = "{fa-close}";
    public static final String FA_DEVOIR = "{fa-file-text-o}";
    public static final String FA_COURSE = "{fa-graduation-cap}";
    public static final String FA_USER = "{fa-user}";

    /**
     * PICTURE
     */
    public static String copyPicture(Bitmap selectedBitmap, int pupilID) throws IOException {

        File dst = new File(Environment.getExternalStorageDirectory()  + C.PATH_PHOTOS_FOLDER);
        if(!dst.exists())
            dst.mkdirs();
        String path = dst.getPath() + "/" + pupilID + ".png";

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap) {
        int widthLight = bitmap.getWidth();
        int heightLight = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paintColor = new Paint();
        paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

        RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

        canvas.drawRoundRect(rectF, widthLight / 2 ,heightLight / 2,paintColor);

        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0, 0, paintImage);

        return output;
    }

    /**
     * LOAD ACTIONBAR
     */
    public static ActionBar setUpActionBar(ActionBar actionBar, Activity activity){
        ViewGroup actionBarLayout = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.actionbar, null);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        actionBar.setCustomView(actionBarLayout, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        actionBarLayout.findViewById(R.id.icDrawer).setVisibility(View.GONE);
        actionBarLayout.findViewById(R.id.mainPix).setVisibility(View.GONE);
        Toolbar parent =(Toolbar) actionBarLayout.getParent();
        parent.setPadding(0, 0, 0, 0);
        parent.setContentInsetsAbsolute(0, 0);

        return actionBar;
    }








}
