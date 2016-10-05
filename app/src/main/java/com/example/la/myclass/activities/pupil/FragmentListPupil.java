package com.example.la.myclass.activities.pupil;

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
import com.example.la.myclass.activities.FragmentListDefault;
import com.example.la.myclass.adapters.RecyclerViewPupil;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.PupilsBDD;

import java.util.List;

/**
 * Created by Léa on 28/09/2015.
 */
public class FragmentListPupil extends FragmentListDefault implements AdapterView.OnItemSelectedListener {


    /**
     * Variables de classe
     */
    protected int mFilterFrequencyValue = -1, mFilterTypeValue = -1, mFilterClassValue = -1;

    /**
     * Fragment Lide Cycle
     */
    public static FragmentListPupil newInstance() {
        return new FragmentListPupil();
    }

    public FragmentListPupil() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFilteringLabels.add(1, "");
        mFilteringLabels.add(2, "");
        mFilteringLabels.add(3, "");
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
     * Implementing abstracts functions from FragmentListDefault
     */
    @Override
    protected void fillListView(RecyclerView recyclerView, TextView textViewNoItem) {
        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        List<Pupil> pupilList = pupilsBDD.getAllPupils();
        pupilsBDD.close();

        if (pupilList.size() > 0) {
            recyclerView.setAdapter(new RecyclerViewPupil(getActivity(), pupilList));
        } else {
            recyclerView.setVisibility(View.GONE);
            textViewNoItem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void removeFilters() {
        mFilteringLabels.set(1, "");
        mFilteringLabels.set(2, "");
        mFilteringLabels.set(3, "");
        mFilterFrequencyValue = mFilterTypeValue = mFilterClassValue = -1;
        ((Spinner)super.mContentView.findViewById(R.id.spinnerClass)).setSelection(0);
        ((Spinner)super.mContentView.findViewById(R.id.spinnerPaiementType)).setSelection(0);
        ((Spinner)super.mContentView.findViewById(R.id.spinnerFrequency)).setSelection(0);
        super.removeFilters();
    }

    @Override
    protected View setFilterContent(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filters_pupils, container, false);

        setSpinnerContent((Spinner) view.findViewById(R.id.spinnerClass), R.array.filter_classe, this);
        setSpinnerContent((Spinner) view.findViewById(R.id.spinnerPaiementType), R.array.filter_paiement_type, this);
        setSpinnerContent((Spinner) view.findViewById(R.id.spinnerFrequency), R.array.filter_frequency, this);

        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public List<?> getFilterList() {

        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        String criteria = "1";

        if(super.mFilterDateValue > -1)
            criteria += " AND " + PupilsBDD.COL_DATE_SINCE + " >= " + super.mFilterDateValue;
        if(super.mFilterBetweenDate > -1){
            criteria += " AND " + PupilsBDD.COL_DATE_SINCE + " >= " + super.mFromDate + " AND " + PupilsBDD.COL_DATE_SINCE + " <= " + super.mToDate;
        }

        if (mFilterFrequencyValue > -1) {
            criteria += " AND " + PupilsBDD.COL_FREQUENCE + " = " + mFilterFrequencyValue;
            mFilteringLabels.set(2, getResources().getStringArray(R.array.filter_frequency)[mFilterFrequencyValue+1]);
        }

        if (mFilterTypeValue > -1) {
            criteria += " AND " + PupilsBDD.COL_TYPE + " = " + mFilterTypeValue;
            mFilteringLabels.set(3, getResources().getStringArray(R.array.filter_paiement_type)[mFilterTypeValue+1]);
        }

        if (mFilterClassValue > -1) {
            criteria += " AND " + PupilsBDD.COL_CLASS + " = " + mFilterClassValue;
            mFilteringLabels.set(1, getResources().getStringArray(R.array.filter_classe)[mFilterClassValue]);
        }

        Log.e("Criteria", criteria);
        return pupilsBDD.getPupilsWithCriteria(criteria);
    }

    @Override
    public void manageListView(List<?> list, RecyclerView listView, TextView textView) {
        ((RecyclerViewPupil) listView.getAdapter()).setmListPupils((List<Pupil>) list);
        textView.setText("Aucun élève");
        super.manageListView(list, listView, textView);
    }

    /**
     * Implementing Interfaces methods
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinnerClass) {
            if(i>0)
                mFilterClassValue = i;
            else
                mFilterClassValue = -1;
        }
        if (adapterView.getId() == R.id.spinnerPaiementType) {
            if (i > 0)
                mFilterTypeValue = i-1;
            else
                mFilterTypeValue = -1;
        }
        if (adapterView.getId() == R.id.spinnerFrequency) {
            if (i > 0)
                mFilterFrequencyValue = i-1;
            else
                mFilterFrequencyValue = -1;
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
                Intent intent = new Intent(getActivity(),ActivityPupil.class);
                intent.putExtra(ActivityPupil.PUPIL_ACTION, ActivityPupil.ADDING);
                startActivity(intent);
                break;
        }
    }
} //203 -> 161