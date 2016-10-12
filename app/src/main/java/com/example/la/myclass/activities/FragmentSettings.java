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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.adapters.AdapterListViewDB;
import com.example.la.myclass.beans.MyDb;
import com.example.la.myclass.utils.MyJSONParser;

import java.io.File;
import java.util.List;

/**
 * Created by Gaminho on 22/05/2016.
 */
public class FragmentSettings extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    // Variables de classe
    SharedPreferences mSharedPreferences;
    MyDb mCurrentDB;

    //Views
    protected TextView mTVCurrentDBName, mTVCurrentDBVersion, mTVCurrentDBDate, mTVCurrentDBSize;
    protected LinearLayout mLLExportDB, mLLImportDB, mLLSaveNewDB;
    protected Switch mSCourseBegin, mSCourseEnd, mSDevoirBegin, mSDevoirEnd;
    protected View mRootView;

    // Fragment life cycle
    public static FragmentSettings newInstance() {
        return new FragmentSettings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences =  getActivity().getSharedPreferences(C.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mCurrentDB = new MyJSONParser().getDatabaseFromJsonFile(mSharedPreferences.getString(C.CURRENT_DB, C.NO_DB));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_settings, container, false);
        getAllViews(mRootView);
        fillAllViews(mCurrentDB);

        return mRootView;
    }


    // Utils

    public void getAllViews(View view){

        mLLExportDB = (LinearLayout) view.findViewById(R.id.exportDB);
        mLLExportDB.setOnClickListener(this);
        mLLImportDB = (LinearLayout) view.findViewById(R.id.importDB);
        mLLImportDB.setOnClickListener(this);
        mLLSaveNewDB = (LinearLayout) view.findViewById(R.id.saveNewDB);
        mLLSaveNewDB.setOnClickListener(this);

        mSCourseBegin = (Switch) view.findViewById(R.id.swCourseBegin);
        mSCourseBegin.setOnCheckedChangeListener(this);
        mSCourseEnd = (Switch) view.findViewById(R.id.swCourseEnd);
        mSCourseEnd.setOnCheckedChangeListener(this);
        mSDevoirEnd = (Switch) view.findViewById(R.id.swDevoirEnd);
        mSDevoirEnd.setOnCheckedChangeListener(this);
        mSDevoirBegin = (Switch) view.findViewById(R.id.swDevoirBegin);
        mSDevoirBegin.setOnCheckedChangeListener(this);
    }

    public void fillAllViews(MyDb myDb){
        if(myDb != null) {
            ((TextView) mRootView.findViewById(R.id.tvCurrentDBName)).setText(myDb.getName());
            ((TextView) mRootView.findViewById(R.id.tvDBComment)).setText(myDb.getCommentaire());
            ((TextView) mRootView.findViewById(R.id.tvDBFilePath)).setText(String.format("../%s",
                    myDb.getFilePath().substring(myDb.getFilePath().indexOf(C.NAME_EXPORTED_DB)))
            );
            ((TextView) mRootView.findViewById(R.id.tvCurrentDBLastUpdate)).setText(C.formatDate(myDb.getLastUpdate(), C.DD_MM_YY));
            ((TextView) mRootView.findViewById(R.id.tvDBCreationDate)).setText(C.formatDate(myDb.getDate(), C.DD_MM_YY));
            ((TextView) mRootView.findViewById(R.id.tvCurrentDBSize)).setText(C.formatSize(myDb.getSize()));

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

    // Dialog

    public void chooseDBDialog() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_choose_db, null);

        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);


        final ListView mListDB = (ListView) adv.findViewById(R.id.listOfDB);

        //On récupère les bases de données
        List<MyDb> listDBs = new MyJSONParser().getListDatabaseFromJsonFile();

        if(listDBs.size() == 0) {
            Toast.makeText(getActivity(), "Aucune base de données enregistrée.", Toast.LENGTH_SHORT).show();
            return;
        }

        final AlertDialog createDialog = adb.create();
        createDialog.show();

        if(listDBs.size()>0) {
            mListDB.setAdapter(new AdapterListViewDB(getActivity(), listDBs));
            mListDB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MyDb myDB = (MyDb) mListDB.getAdapter().getItem(i);
                    mSharedPreferences.edit().putString(C.CURRENT_DB, myDB.getName()).apply();

                    if(C.importDB2(getActivity(), myDB.getFilePath())) {
                        mSharedPreferences.edit()
                                .putString(C.CURRENT_DB, myDB.getFilePath())
                                .apply();
                        createDialog.dismiss();
                    }
                }
            });
        }


    }

    public void updateDBDialog(final String filePathNewDb) {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_delete, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);

        final AlertDialog createDialog = adb.create();
        createDialog.show();

        ((TextView) createDialog.findViewById(R.id.messageDialog)).setText(getText(R.string.dialog_update_db));

        createDialog.findViewById(R.id.valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(C.importDB2(getActivity(), filePathNewDb)) {
                    mSharedPreferences.edit()
                            .putString(C.CURRENT_DB, filePathNewDb)
                            .apply();
                    createDialog.dismiss();
                }
            }
        });

        createDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog.dismiss();
            }
        });
    }

    public void saveNewDBDialog() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_save_new_db, null);

        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);

        final AlertDialog createDialog = adb.create();
        createDialog.show();

        final EditText editNameDb = (EditText) adv.findViewById(R.id.newDbName);
        final EditText editComDb = (EditText) adv.findViewById(R.id.newDbCom);

        createDialog.findViewById(R.id.valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(editNameDb.getText().toString())) {
                    updateDBDialog(C.exportDB2(getActivity(), editNameDb.getText().toString(), editComDb.getText().toString()));;
                    createDialog.dismiss();
                }
                else
                    Toast.makeText(getActivity(), "Renseignez un nom pour la base de données.", Toast.LENGTH_SHORT).show();

            }
        });

        createDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog.dismiss();
            }
        });
    }


    // Interface

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exportDB :
                saveNewDBDialog();
                break;
            case R.id.importDB :
                chooseDBDialog();
                break;
            case R.id.saveNewDB:
                C.updateDB(getActivity());
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
        if(compoundButton.getId() == R.id.swCourseBegin)
            mSharedPreferences.edit().putBoolean(C.SP_NOTIF_COURSE_BEGIN, isOn).apply();

        if(compoundButton.getId() == R.id.swCourseEnd)
            mSharedPreferences.edit().putBoolean(C.SP_NOTIF_COURSE_END, isOn).apply();

        if(compoundButton.getId() == R.id.swDevoirBegin)
            mSharedPreferences.edit().putBoolean(C.SP_NOTIF_DEVOIR_BEGIN, isOn).apply();

        if(compoundButton.getId() == R.id.swDevoirEnd)
            mSharedPreferences.edit().putBoolean(C.SP_NOTIF_DEVOIR_END, isOn).apply();
    }
}

