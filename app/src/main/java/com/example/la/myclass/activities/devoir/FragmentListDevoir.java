package com.example.la.myclass.activities.devoir;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.activities.AbstractFragmentList;
import com.example.la.myclass.adapters.RecyclerViewCourses;
import com.example.la.myclass.adapters.RecyclerViewDevoirs;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.DevoirBDD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Léa on 02/10/2015.
 */
public class FragmentListDevoir extends AbstractFragmentList implements AdapterView.OnItemSelectedListener {

    /**
     * Constantes du bundle
     */
    private static final String DEVOIR_STATE = "devoirs_state";

    /**
     * Variables de classe
     */
    protected int mState;   // Used to auto-fill listview with specified state (all by default)
    protected int mFilterPupilIDValue = -1, mFilterStateValue =-1;


    /**
     * Fragment Lide Cycle
     */
    public static FragmentListDevoir newInstance() {
        return new FragmentListDevoir();
    }

    public FragmentListDevoir() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mState = getArguments().getInt(DEVOIR_STATE, Devoir.ALL_DEVOIRS);
        mFilteringLabels.add(1, "");
        mFilteringLabels.add(2, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        fillListView(mListView, mTextViewNoItem);
        manageListView(getFilterList(), mListView, mTextViewNoItem);
        mButtonAddAnItem.setOnClickListener(this);
        return view;
    }

    /**
     * Implementing abstracts functions from AbstractFragmentList
     */
    @Override
    protected void fillListView(RecyclerView recyclerView, TextView textViewNoItem) {
        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        List<Devoir> devoirList = devoirBDD.getAllDevoirs();
        devoirBDD.close();

        boolean isEmpty = devoirList.size() > 0;
        devoirList = !isEmpty ? devoirList : new ArrayList<Devoir>();
        recyclerView.setAdapter(new RecyclerViewDevoirs(getActivity(), devoirList));

        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        textViewNoItem.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void removeFilters() {
        mFilteringLabels.set(1, "");
        mFilteringLabels.set(2, "");
        mFilterPupilIDValue = mFilterStateValue =-1;
        ((Spinner)super.mContentView.findViewById(R.id.spinnerPupils)).setSelection(0);
        ((Spinner)super.mContentView.findViewById(R.id.spinnerStates)).setSelection(0);
        super.removeFilters();
    }

    @Override
    protected View setFilterContent(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.course_filters, container, false);
        fillSpinnerPupils((Spinner) view.findViewById(R.id.spinnerPupils), this);
        setSpinnerContent((Spinner) view.findViewById(R.id.spinnerStates), R.array.filter_devoirs, this);
        mCBHide.setText("Afficher les devoirs annulés");
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public List<?> getFilterList() {
        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        String criteria = "1";

        if(super.mFilterDateValue > -1)
            criteria += " AND " + DevoirBDD.COL_DATE + " >= " + super.mFilterDateValue;
        if(super.mFilterBetweenDate > -1){
            criteria += " AND " + DevoirBDD.COL_DATE + " >= " + super.mFromDate + " AND " + DevoirBDD.COL_DATE + " <= " + super.mToDate;
        }

        if(mFilterPupilIDValue > -1)
            criteria += " AND " + DevoirBDD.COL_PUPIL_ID + " = " + mFilterPupilIDValue;

        if(mFilterStateValue > -1) {
            criteria += " AND " + DevoirBDD.COL_STATE + " = " + mFilterStateValue;
            mFilteringLabels.set(2, Devoir.getStateLabel(mFilterStateValue));
        }
        else
            mFilteringLabels.set(2, "");

        if (!mDisplayOldItems)
            criteria += " AND " + DevoirBDD.COL_STATE + " != " + Devoir.STATE_CANCELED;

        Log.e("Criteria", criteria);
        return devoirBDD.getDevoirWithCriteria(criteria);
    }

    @Override
    public void manageListView(List<?> list, RecyclerView listView, TextView textView) {
        ((RecyclerViewDevoirs) listView.getAdapter()).setmListDevoirs((List<Devoir>) list);
        textView.setText("Aucun devoir");
        super.manageListView(list, listView, textView);
    }

    /**
     * Implementing Interfaces methods
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerStates){
            if(i>0)
                mFilterStateValue = i - 1;
            else
                mFilterStateValue = -1;
        }
        if(adapterView.getId() == R.id.spinnerPupils){
            if (i > 0) {
                Pupil pupil = (Pupil) adapterView.getAdapter().getItem(i);
                mFilterPupilIDValue = pupil.getId();
                mFilteringLabels.set(1,pupil.getFullName());
            }
            else {
                mFilterPupilIDValue = -1;
                mFilteringLabels.set(1,"");
            }
        }

        mList = getFilterList();
        manageListView(mList, mListView, mTextViewNoItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch(view.getId()){
            case R.id.addAnItem:
                Intent intent = new Intent(getActivity(), ActivityDevoir.class);
                intent.putExtra(ActivityDevoir.DEVOIR_ACTION, ActivityDevoir.ADDING);
                startActivity(intent);
                break;
        }
    }
}

// 04h23 - 190 -> 05h29 - 159
