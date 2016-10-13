package com.example.la.myclass.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.course.FragmentListCourse;
import com.example.la.myclass.activities.devoir.FragmentListDevoir;
import com.example.la.myclass.activities.pupil.FragmentListPupil;
import com.example.la.myclass.services.CourseService;
import com.example.la.myclass.activities.stats.FragmentStats;
import com.example.la.myclass.services.DatabaseService;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{


    //Intent Extra
    public static final String NAVIGATION_POSITION = "navigation_position";
    public static final String PUPIL_ID = "pupil_id";

    //Courses
    private static final int ALL_COURSES = 3;

    // Variable de classe
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Fragment mFragment = new Fragment();
    public ActionBar mActionBar;

    // Views
    protected TextView mTextViewTitle;


    // Activity Life Cycle


    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(MainActivity.this, CourseService.class);
        startService(i);
        i = new Intent(MainActivity.this, DatabaseService.class);
        startService(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MainActivity", "onCreate");

        setContentView(R.layout.activity_main);
        getAllViews();

        mActionBar = C.setUpActionBar(getSupportActionBar(), this);
        mTextViewTitle = (TextView) mActionBar.getCustomView().findViewById(R.id.title);

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mActionBar);
        mTextViewTitle.setText(getResources().getStringArray(R.array.drawer_sections)[0].split(";")[0]);

        if(getIntent() != null && getIntent().getIntExtra(NAVIGATION_POSITION,-1) != -1){
            int indexSection = getIntent().getIntExtra(NAVIGATION_POSITION,-1);
            Fragment fragment;
            switch(indexSection){
                case 4:
                    fragment = FragmentSuivi.newInstance(getIntent().getIntExtra(PUPIL_ID,-1));
                    break;
                default:
                    fragment = null;
                    break;
            }
            setCurrentFragment(fragment);
            loadFragment(fragment,indexSection,getResources().getStringArray(R.array.drawer_sections)[indexSection].split(";")[0],false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mFragment.getClass() == FragmentListPupil.class)
            getFragmentManager().beginTransaction().replace(R.id.container, FragmentListPupil.newInstance()).commit();

        else if(mFragment.getClass() == FragmentListCourse.class)
            getFragmentManager().beginTransaction().replace(R.id.container, FragmentListCourse.newInstance(ALL_COURSES)).commit();

        else if(mFragment.getClass() == FragmentListDevoir.class)
            getFragmentManager().beginTransaction().replace(R.id.container, FragmentListDevoir.newInstance()).commit();

    }

    @Override
    public void onBackPressed() {
        if(mFragment.getClass() != FragmentHome.class)
            onNavigationDrawerItemSelected(0, getResources().getStringArray(R.array.drawer_sections)[0].split(";")[0]);
        else
            super.onBackPressed();
    }

    // Utils
    public void getAllViews(){
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
    }

    // Interface

    @Override
    public void onNavigationDrawerItemSelected(int position, String label) {

        if(label.contains("Elèves"))
            mFragment = FragmentListPupil.newInstance();

        else if (label.contains("Cours"))
            mFragment = FragmentListCourse.newInstance(ALL_COURSES);

        else if (label.contains("Argent"))
            mFragment = FragmentMoney.newInstance();

        else if (label.contains("Calendrier"))
            mFragment = FragmentCalendar.newInstance();

        else if (label.contains("Devoirs"))
            mFragment = FragmentListDevoir.newInstance();

        else if(label.contains("Statistiques"))
            mFragment = FragmentStats.newInstance();

        else if(label.contains("Paramètres"))
            mFragment = FragmentSettings.newInstance();

        else if(label.contains("Suivi"))
            mFragment = FragmentSuivi.newInstance();

        else
            mFragment = FragmentHome.newInstance();

        loadFragment(mFragment, position, label, false);

    }

    public void loadFragment(Fragment fragment, int position, String label, boolean addToBackStack){
        FragmentManager fragmentManager = getFragmentManager();

        if(addToBackStack){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(label)
                    .commit();
        }
        else
            fragmentManager.beginTransaction()
                   .replace(R.id.container, fragment)
                    .commit();

        if(mTextViewTitle != null)
            mTextViewTitle.setText(label);

        if(mNavigationDrawerFragment != null)
            mNavigationDrawerFragment.selectItem(position);
    }

    public void setCurrentFragment(Fragment fragment){
        mFragment = fragment;
    }

}
