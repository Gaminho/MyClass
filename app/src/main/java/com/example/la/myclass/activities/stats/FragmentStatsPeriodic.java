package com.example.la.myclass.activities.stats;

import android.os.Bundle;
import android.widget.TextView;

import com.example.la.myclass.beans.periodic.PeriodicItem;
import com.example.la.myclass.database.CoursesBDD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaminho on 21/09/2016.
 */
public class FragmentStatsPeriodic extends AbstractFragmentStats {

    /**
     * Variables de classe
     */
    protected int mItemType;

    /**
     * Bundle Variables
     */
    protected static final String PERIODIC_ITEM = "periodic_item";

    /**
     * Fragment Life Cycle
     */
    public static FragmentStatsPeriodic newInstance() {
        return new FragmentStatsPeriodic();
    }
    public static FragmentStatsPeriodic newInstance(int periodicItem) {
        FragmentStatsPeriodic fragmentStatsPeriodic = new FragmentStatsPeriodic();
        Bundle args = new Bundle();
        args.putInt(PERIODIC_ITEM, periodicItem);
        fragmentStatsPeriodic.setArguments(args);
        return fragmentStatsPeriodic;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().getInt(PERIODIC_ITEM,-1) != -1)
            mItemType = getArguments().getInt(PERIODIC_ITEM, FragmentStats.WEEK);
    }

    /**
     * Implementing abstracts functions from AbstractFragmentStats
     */
    @Override
    public List<PeriodicItem> getListItem() {
        List<PeriodicItem> periodicItemList = new ArrayList<>();
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        if(mItemType == FragmentStats.WEEK)
            periodicItemList = coursesBDD.getAllWeeks();
        else if(mItemType == FragmentStats.MONTH)
            periodicItemList = coursesBDD.getAllMonths();
        else if(mItemType == FragmentStats.YEAR)
            periodicItemList = coursesBDD.getAllYears();

        coursesBDD.close();
        return periodicItemList;
    }

    @Override
    public void setTypeItems(TextView textView) {
        String type = "";
        switch(mItemType){
            case FragmentStats.WEEK:
                type = "semaines";
                break;
            case FragmentStats.MONTH:
                type = "mois";
                break;
            case FragmentStats.YEAR:
                type = "années scolaires";
                break;
            default:
                break;
        }
        textView.setText(String.format("%s depuis le ", type));
    }

} //141
