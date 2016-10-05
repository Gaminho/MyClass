package com.example.la.myclass.activities.devoir;

import android.app.Activity;
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
 * Created by Léa on 05/10/2015.
 */
public class ActivityDevoir extends ActionBarActivity implements FragmentDetailsDevoir.DevoirDetailsInterface{


    // BUNDLE
    public static final String DEVOIR_ID = "devoir_id";
    public static final String DEVOIR_ACTION = "devoir_action";
    public static final int ADDING = 0;
    static final int SEEING = 1;

    //Variables de classe
    Fragment mFragment;
    public ActionBar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_framelayout);

        mActionBar = C.setUpActionBar(getSupportActionBar(), this);
        ((TextView) mActionBar.getCustomView().findViewById(R.id.title)).setText("Devoir");
        ((ImageView) mActionBar.getCustomView().findViewById(R.id.mainPix)).setImageDrawable(getResources().getDrawable(R.drawable.man));
        mActionBar.getCustomView().findViewById(R.id.mainPix).setVisibility(View.VISIBLE);

        Fragment fragment;
        switch(getIntent().getIntExtra(DEVOIR_ACTION, SEEING)){
            case ADDING :
                fragment = FragmentAddOrEditDevoir.newInstance();
                break;
            default:
                fragment = FragmentDetailsDevoir.newInstance(getIntent().getIntExtra(DEVOIR_ID, 0));
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


    @Override
    public void goBack() {
        finish();
    }

}
