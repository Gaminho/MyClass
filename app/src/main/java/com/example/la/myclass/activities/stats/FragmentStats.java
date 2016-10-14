package com.example.la.myclass.activities.stats;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.la.myclass.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaminho on 22/05/2016.
 */
public class FragmentStats extends Fragment implements View.OnClickListener{


    /**
     * Static Variables
     */
    protected static final int ALL = 0;
    protected static final int WEEK = 1;
    protected static final int MONTH = 2;
    protected static final int YEAR = 3;


    /**
     * Views
     */
    protected List<TextView> mListTVOnglet;
    protected FrameLayout mFrameStats;


    /**
     * Fragment Life Cycle
     */
    public static FragmentStats newInstance() {
        return new FragmentStats();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_main, container, false);
        getAllViews(view);
        changeModView(ALL);
        return view;
    }


    /**
     * Utils
     */

    public void getAllViews(View view){

        mListTVOnglet = new ArrayList<>();

        TextView textView = (TextView) view.findViewById(R.id.statGlobal);
        textView.setOnClickListener(this);
        mListTVOnglet.add(ALL, textView);

        textView = (TextView) view.findViewById(R.id.statWeek);
        textView.setOnClickListener(this);
        mListTVOnglet.add(WEEK, textView);

        textView = (TextView) view.findViewById(R.id.statMonth);
        textView.setOnClickListener(this);
        mListTVOnglet.add(MONTH, textView);

        textView = (TextView) view.findViewById(R.id.statYear);
        textView.setOnClickListener(this);
        mListTVOnglet.add(YEAR, textView);

        mFrameStats = (FrameLayout) view.findViewById(R.id.statsFrame);
    }

    public void changeModView(int modView){

        for(TextView textView : mListTVOnglet){
            textView.setTextColor(getActivity().getResources().getColor(R.color.white));
            textView.setTypeface(null, Typeface.NORMAL);
        }

        Fragment fragment;


        if(modView != ALL)
            fragment = FragmentStatsPeriodic.newInstance(modView);

        else
            fragment = FragmentStatsGlobal.newInstance();

        TextView textView = mListTVOnglet.get(modView);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(getActivity().getResources().getColor(R.color.unthem300));

        getFragmentManager().beginTransaction().replace(R.id.statsFrame, fragment).commit();
    }

    /**
     * Implemeting interfqces methods
     * @param view
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.statGlobal :
                changeModView(ALL);
                break;
            case R.id.statMonth :
                changeModView(MONTH);
                break;
            case R.id.statWeek :
                changeModView(WEEK);
                break;
            case R.id.statYear :
                changeModView(YEAR);
                break;
        }
    }
} // 264

