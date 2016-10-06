package com.example.la.myclass.activities;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.adapters.SpinnerPupilWithPixAdapter;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.PupilsBDD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gaminho on 02/10/2016.
 */
public abstract class AbstractFragmentAddOrEdit extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /**
     * Common views
     */
    protected ScrollView mScrollView;
    protected LinearLayout mContentView;
    protected LinearLayout mLeftAction, mRightAction;
    protected TextView mLeftActionLabel, mLeftActionIcon, mRightActionLabel, mRightActionIcon;

    /**
     * Variables de classe
     */
    protected DatePickerDialog mDatePickerDialog;
    protected TimePickerDialog mTimePickerDialog;

    /**
     * Fragment Life cycle
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_fragment_adding, container, false);
        getAllViews(view);
        mContentView = (LinearLayout) setContent(getActivity(), mScrollView);
        mScrollView.addView(mContentView);
        setLeftAction(mLeftAction, mLeftActionLabel, mLeftActionIcon);
        setRightAction(mRightAction, mRightActionLabel, mRightActionIcon);
        return view;
    }

    /**
     * Utils methods
     * @param view
     */
    protected void getAllViews(View view){
        mScrollView = (ScrollView) view.findViewById(R.id.scrollViewContent);
        mLeftActionLabel = (TextView) view.findViewById(R.id.leftActionLabel);
        mLeftActionIcon = (TextView) view.findViewById(R.id.leftActionIcon);
        mRightActionLabel = (TextView) view.findViewById(R.id.rightActionLabel);
        mRightActionIcon = (TextView) view.findViewById(R.id.rightActionIcon);
        mLeftAction = (LinearLayout) view.findViewById(R.id.leftAction);
        mRightAction = (LinearLayout) view.findViewById(R.id.rightAction);
        mLeftAction.setOnClickListener(this);
        mRightAction.setOnClickListener(this);
    }
    protected abstract View setContent(Context context, ViewGroup container);
    public void fillSpinnerPupils(final Spinner spinner, AdapterView.OnItemSelectedListener listener, int textColor){
        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        List<Pupil> listPupils = pupilsBDD.getActivePupils();
        pupilsBDD.close();
        spinner.setAdapter(new SpinnerPupilWithPixAdapter(getActivity(), listPupils, true, textColor));
        spinner.setOnItemSelectedListener(listener);
    }
    protected abstract void setLeftAction(LinearLayout leftLayout, TextView leftLabel, TextView leftIcon);
    protected abstract void setRightAction(LinearLayout rightLayout, TextView rightLabel, TextView rightIcon);
}
