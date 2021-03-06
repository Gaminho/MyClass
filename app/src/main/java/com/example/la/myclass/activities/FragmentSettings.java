package com.example.la.myclass.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.adapters.AdapterListViewDB;
import com.example.la.myclass.beans.Database;
import com.example.la.myclass.utils.MyJSONParser;

import java.util.List;

/**
 * Created by Gaminho on 22/05/2016.
 */
public class FragmentSettings extends Fragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    // Variables de classe
    SharedPreferences mSharedPreferences;
    Database mCurrentDB;

    //Views
    protected TextView mTVCurrentDBName, mTVCurrentDBVersion, mTVCurrentDBDate, mTVCurrentDBSize;
    protected LinearLayout mLLExportDB, mLLImportDB, mLLSaveNewDB;
    protected Switch mSCourseBegin, mSCourseEnd, mSDevoirBegin, mSDevoirEnd, mSDatabaseUpdate;
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
        mSDatabaseUpdate = (Switch) view.findViewById(R.id.swDatabaseUpdate);
        mSDatabaseUpdate.setOnCheckedChangeListener(this);

        ((RadioGroup)view.findViewById(R.id.rGroupFrequenceUpdate)).setOnCheckedChangeListener(this);
    }

    public void fillAllViews(Database database){
        if(database != null) {
            ((TextView) mRootView.findViewById(R.id.tvCurrentDBName)).setText(database.getName());
            ((TextView) mRootView.findViewById(R.id.tvDBComment)).setText(database.getCommentaire());
            ((TextView) mRootView.findViewById(R.id.tvDBFilePath)).setText(String.format("../%s",
                    database.getFilePath().substring(database.getFilePath().indexOf(C.NAME_EXPORTED_DB)))
            );
            ((TextView) mRootView.findViewById(R.id.tvCurrentDBLastUpdate)).setText(C.formatDate(database.getLastUpdate(), C.DD_MM_YY));
            ((TextView) mRootView.findViewById(R.id.tvDBCreationDate)).setText(C.formatDate(database.getDate(), C.DD_MM_YY));
            ((TextView) mRootView.findViewById(R.id.tvCurrentDBSize)).setText(C.formatSize(database.getSize()));

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
        if(mSharedPreferences.getBoolean(C.SP_NOTIF_DATABASE_UPDATE, false)) {
            mSDatabaseUpdate.setChecked(true);
            mRootView.findViewById(R.id.rb10days).setEnabled(true);
            mRootView.findViewById(R.id.rb7days).setEnabled(true);
            mRootView.findViewById(R.id.rb3days).setEnabled(true);
        }

        if(mSharedPreferences.getLong(C.SP_UPDATE_DB_DELAY,0) == 10 * C.DAY)
            ((RadioButton)mRootView.findViewById(R.id.rb10days)).setChecked(true);
        if(mSharedPreferences.getLong(C.SP_UPDATE_DB_DELAY,0) == 7 * C.DAY)
            ((RadioButton)mRootView.findViewById(R.id.rb7days)).setChecked(true);
        if(mSharedPreferences.getLong(C.SP_UPDATE_DB_DELAY,0) == 3 * C.DAY)
            ((RadioButton)mRootView.findViewById(R.id.rb3days)).setChecked(true);
    }

    // Dialog

    public void chooseDBDialog() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_choose_db, null);

        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);


        final ListView mListDB = (ListView) adv.findViewById(R.id.listOfDB);

        //On récupère les bases de données
        List<Database> listDBs = new MyJSONParser().getListDatabaseFromJsonFile();

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
                    Database database = (Database) mListDB.getAdapter().getItem(i);

                    if(C.importDB(getActivity(), database.getFilePath())) {
                        mSharedPreferences.edit()
                                .putString(C.CURRENT_DB, database.getFilePath())
                                .apply();
                        fillAllViews(new MyJSONParser().getDatabaseFromJsonFile(database.getFilePath()));
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
                if(C.importDB(getActivity(), filePathNewDb)) {
                    mSharedPreferences.edit()
                            .putString(C.CURRENT_DB, filePathNewDb)
                            .apply();
                    fillAllViews(new MyJSONParser().getDatabaseFromJsonFile(filePathNewDb));
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
                    updateDBDialog(C.exportDB(getActivity(), editNameDb.getText().toString(), editComDb.getText().toString()));;
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

        if(compoundButton.getId() == R.id.swDatabaseUpdate) {
            mSharedPreferences.edit().putBoolean(C.SP_NOTIF_DATABASE_UPDATE, isOn).apply();
            if(isOn)
                mRootView.findViewById(R.id.rGroupFrequenceUpdate).setVisibility(View.VISIBLE);
            else
                mRootView.findViewById(R.id.rGroupFrequenceUpdate).setVisibility(View.GONE);
            mRootView.findViewById(R.id.rb10days).setEnabled(isOn);
            mRootView.findViewById(R.id.rb7days).setEnabled(isOn);
            mRootView.findViewById(R.id.rb3days).setEnabled(isOn);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int radioButtonId) {
        if(radioGroup.getId() == R.id.rGroupFrequenceUpdate){
            switch(radioButtonId){
                case R.id.rb10days:
                    mSharedPreferences.edit().putLong(C.SP_UPDATE_DB_DELAY, 10 * C.DAY).apply();
                    break;
                case R.id.rb7days:
                    mSharedPreferences.edit().putLong(C.SP_UPDATE_DB_DELAY, 7 * C.DAY).apply();
                    break;
                case R.id.rb3days:
                    mSharedPreferences.edit().putLong(C.SP_UPDATE_DB_DELAY, 3 * C.DAY).apply();
                    break;
            }
        }
    }
}

