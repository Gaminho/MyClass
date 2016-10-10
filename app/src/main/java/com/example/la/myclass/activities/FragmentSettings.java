package com.example.la.myclass.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.adapters.AdapterListViewDB;
import com.example.la.myclass.beans.MyDB;
import com.example.la.myclass.database.MyDatabase;
import com.example.la.myclass.utils.DateParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Gaminho on 22/05/2016.
 */
public class FragmentSettings extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    // Variables de classe
    SharedPreferences mSharedPreferences;
    MyDB mCurrentDB;

    //Views
    protected TextView mTVCurrentDBName, mTVCurrentDBVersion, mTVCurrentDBDate, mTVCurrentDBSize;
    protected LinearLayout mLLExportDB, mLLImportDB;
    protected Switch mSCourseBegin, mSCourseEnd, mSDevoirBegin, mSDevoirEnd;

    // Fragment life cycle
    public static FragmentSettings newInstance() {
        return new FragmentSettings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences =  getActivity().getSharedPreferences(C.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mCurrentDB = getDBWithFileName(mSharedPreferences.getString(C.CURRENT_DB,C.NO_DB));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        getAllViews(view);
        fillAllViews();

        return view;
    }


    // Utils

    public void getAllViews(View view){

        mTVCurrentDBName = (TextView) view.findViewById(R.id.tvCurrentDBName);
        mTVCurrentDBVersion = (TextView) view.findViewById(R.id.tvCurrentDBVersion);
        mTVCurrentDBDate = (TextView) view.findViewById(R.id.tvCurrentDBDate);
        mTVCurrentDBSize = (TextView) view.findViewById(R.id.tvCurrentDBSize);

        mLLExportDB = (LinearLayout) view.findViewById(R.id.exportDB);
        mLLExportDB.setOnClickListener(this);
        mLLImportDB = (LinearLayout) view.findViewById(R.id.importDB);
        mLLImportDB.setOnClickListener(this);

        mSCourseBegin = (Switch) view.findViewById(R.id.swCourseBegin);
        mSCourseBegin.setOnCheckedChangeListener(this);
        mSCourseEnd = (Switch) view.findViewById(R.id.swCourseEnd);
        mSCourseEnd.setOnCheckedChangeListener(this);
        mSDevoirEnd = (Switch) view.findViewById(R.id.swDevoirEnd);
        mSDevoirEnd.setOnCheckedChangeListener(this);
        mSDevoirBegin = (Switch) view.findViewById(R.id.swDevoirBegin);
        mSDevoirBegin.setOnCheckedChangeListener(this);
    }

    public void fillAllViews(){
        if(mCurrentDB != null) {
            mTVCurrentDBName.setText(mCurrentDB.getName());
            mTVCurrentDBDate.setText(String.format("%s à %s", C.formatDate(mCurrentDB.getDate(), C.DATE_FORMAT_SHORT_MONTH), C.formatDate(mCurrentDB.getDate(),C.HH_mm)));
            mTVCurrentDBSize.setText(C.formatSize(mCurrentDB.getSize()));
            mTVCurrentDBVersion.setText("v.01");
        }
        // Notifications courses
        if(mSharedPreferences.getBoolean(C.SP_NOTIF_COURSE_END, false))
            mSCourseEnd.setChecked(true);
        if(mSharedPreferences.getBoolean(C.SP_NOTIF_COURSE_BEGIN, false))
            mSCourseBegin.setChecked(true);

        //Notification devoirs
        if(mSharedPreferences.getBoolean(C.SP_NOTIF_DEVOIR_BEGIN, false))
            mSDevoirBegin.setChecked(true);
        if(mSharedPreferences.getBoolean(C.SP_NOTIF_DEVOIR_END, false))
            mSDevoirEnd.setChecked(true);
    }


    public MyDB getDBWithFileName(String fileName){
        File dataBase = new File(Environment.getExternalStorageDirectory() + C.PATH_DB_FOLDER + fileName);
        if(!dataBase.exists())
            return null;
        else
            return new MyDB(dataBase);
    }


    // Dialog

    public void chooseDBDialog() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_choose_db, null);

        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);

        final AlertDialog createDialog = adb.create();
        createDialog.show();

        final ListView mListDB = (ListView) adv.findViewById(R.id.listOfDB);

        //On récupère les bases de données
        List<MyDB> listDBs = new ArrayList<>();
        File folder = new File(Environment.getExternalStorageDirectory() + C.PATH_DB_FOLDER);
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);

        for(File file : listOfFiles){
            if (file.isFile())
                listDBs.add(new MyDB(file));
        }

        if(listDBs.size()>0) {
            mListDB.setAdapter(new AdapterListViewDB(getActivity(), listDBs));
            mListDB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MyDB myDB = (MyDB) ((AdapterListViewDB) mListDB.getAdapter()).getItem(i);
                    mSharedPreferences.edit().putString(C.CURRENT_DB, myDB.getName()).commit();
                    keepOldDBDialog(myDB.getName());
                    createDialog.dismiss();
                }
            });
        }


    }

    public void keepOldDBDialog(final String nameNewDB){

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_delete, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);

        final AlertDialog createDialog = adb.create();
        createDialog.show();

        ((TextView) createDialog.findViewById(R.id.messageDialog)).setText("Souhaitez-vous conserver la base de données actuelle ?");

        createDialog.findViewById(R.id.valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(C.importDB(getActivity(), nameNewDB, false)){
                    mCurrentDB = getDBWithFileName(nameNewDB);
                    fillAllViews();
                };
                createDialog.dismiss();
            }
        });

        createDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(C.importDB(getActivity(), nameNewDB, false)){
                    mCurrentDB = getDBWithFileName(nameNewDB);
                    fillAllViews();
                };
                createDialog.dismiss();
            }
        });
    }


    // Interface

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exportDB :
                C.exportDB(getActivity());
                break;
            case R.id.importDB :
                chooseDBDialog();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
        if(compoundButton.getId() == R.id.swCourseBegin)
            mSharedPreferences.edit().putBoolean(C.SP_NOTIF_COURSE_BEGIN, isOn).commit();

        if(compoundButton.getId() == R.id.swCourseEnd)
            mSharedPreferences.edit().putBoolean(C.SP_NOTIF_COURSE_END, isOn).commit();

        if(compoundButton.getId() == R.id.swDevoirBegin)
            mSharedPreferences.edit().putBoolean(C.SP_NOTIF_DEVOIR_BEGIN, isOn).commit();

        if(compoundButton.getId() == R.id.swDevoirEnd)
            mSharedPreferences.edit().putBoolean(C.SP_NOTIF_DEVOIR_END, isOn).commit();
    }
}

