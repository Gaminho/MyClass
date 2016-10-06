package com.example.la.myclass.activities.course;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;

/**
 * Created by Léa on 01/10/2015.
 */
public class ActivityCourse extends ActionBarActivity implements FragmentDetailsCourse.CourseDetailsInterface {


    // BUNDLE
    static final String COURSE_ID = "course_id";
    public static final String COURSE_ACTION = "course_action";
    public static final String COURSE_DAY = "course_day";
    public static final int ADDING = 0;
    static final int SEEING = 1;


    //Variables de classe
    Fragment mFragment;
    public ActionBar mActionBar;


    //Activity Life Cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_framelayout);

        mActionBar = C.setUpActionBar(getSupportActionBar(), this);
        ((TextView) mActionBar.getCustomView().findViewById(R.id.title)).setText("Cours");
        ((ImageView) mActionBar.getCustomView().findViewById(R.id.mainPix)).setImageDrawable(getResources().getDrawable(R.drawable.ic_book_open_page_variant_white_48dp));
        mActionBar.getCustomView().findViewById(R.id.mainPix).setVisibility(View.VISIBLE);

        Fragment fragment;
        switch(getIntent().getIntExtra(COURSE_ACTION, SEEING)){
            case ADDING :
                Log.e("DEBUGDAY-ACOURSE", "on est dans le switch");
                if(getIntent().getLongExtra(COURSE_DAY, 0) != 0) {
                    fragment = FragmentAddOrEditCourse.newInstance(getIntent().getLongExtra(COURSE_DAY, 0));
                    Log.e("DEBUGDAY-FDAY", "DANS LE IF : " + getIntent().getLongExtra(COURSE_DAY, 0));
                }
                else
                    fragment = FragmentAddOrEditCourse.newInstance();
                break;
            default:
                fragment = FragmentDetailsCourse.newInstance(getIntent().getIntExtra(COURSE_ID, 0));
                break;
        }

        setCurrentFragment(fragment);
        loadFragment(fragment);
    }


    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.default_container, fragment).commit();
    }

    public void setCurrentFragment(Fragment fragment){
        mFragment = fragment;
    }


    // Interface

    @Override
    public void goBack() {
        finish();
    }
}