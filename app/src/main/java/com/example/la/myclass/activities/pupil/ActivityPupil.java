package com.example.la.myclass.activities.pupil;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;

/**
 * Created by Léa on 29/09/2015.
 */
public class ActivityPupil extends ActionBarActivity implements FragmentDetailsPupil.PupilDetailsInterface {


    // BUNDLE
    public static final String PUPIL_ID = "pupil_id";
    public static final String PUPIL_ACTION = "pupil_action";
    public static final int ADDING = 0;
    public static final int SEEING = 1;

    /**
     * VIEWS
     */
    protected TextView mTVTitle;

    //Variables de classe
    Fragment mFragment;
    public ActionBar mActionBar;



    //Activity Life Cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_framelayout);

        mActionBar = C.setUpActionBar(getSupportActionBar(), this);
        mTVTitle = (TextView) mActionBar.getCustomView().findViewById(R.id.title);
        ((TextView) mActionBar.getCustomView().findViewById(R.id.title)).setText("Elèves");
        ((ImageView) mActionBar.getCustomView().findViewById(R.id.mainPix)).setImageDrawable(getResources().getDrawable(R.drawable.ic_group_white_48dp));
        mActionBar.getCustomView().findViewById(R.id.mainPix).setVisibility(View.VISIBLE);

        Fragment fragment;
        switch(getIntent().getIntExtra(PUPIL_ACTION, SEEING)){
            case ADDING :
                fragment = FragmentAddOrEditPupil.newInstance();
                break;
            case SEEING :
                fragment = FragmentDetailsPupil.newInstance(getIntent().getIntExtra(PUPIL_ID,0));
                break;
            default:
                fragment = null;
                break;
        }
        setCurrentFragment(fragment);
        loadFragment(fragment);
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.default_container, fragment)
                .commit();
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
