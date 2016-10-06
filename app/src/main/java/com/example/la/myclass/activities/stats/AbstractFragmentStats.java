package com.example.la.myclass.activities.stats;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.beans.PeriodicItem;
import com.example.la.myclass.customviews.GraphicView;
import com.example.la.myclass.customviews.Podium;
import com.example.la.myclass.customviews.Podium2;
import com.example.la.myclass.database.CoursesBDD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ariche on 06/10/2016.
 */

public abstract class AbstractFragmentStats extends Fragment {

    /**
     * Common Views
     */
    protected GraphicView mGraphic;
    protected Podium2 mPodium;
    protected TextView mTVNbItems, mTVFirstCourse, mTVTypeItems;

    /**
     * Variables de classe
     */
    protected List<PeriodicItem> mListItem;

    /**
     * Fragment Life Cycle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_fragment_stats, container, false);
        getAllViews(view);
        mListItem = getListItem();
        fillGrqphic(mGraphic,mListItem);
        fillPodium(mPodium, mListItem);
        fillNbItems(mTVNbItems,mListItem.size());
        setTypeItems(mTVTypeItems);

        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        mTVFirstCourse.setText(C.formatDate(coursesBDD.getFirstCourse().getDate(), C.DAY_DATE_D_MONTH_YEAR));
        coursesBDD.close();
        return view;
    }

    /**
     * Filling Views
     */
    public void getAllViews(View view){
        mGraphic = (GraphicView) view.findViewById(R.id.graphic);
        mPodium = (Podium2) view.findViewById(R.id.podium);
        mTVNbItems = (TextView) view.findViewById(R.id.nbItems);
        mTVTypeItems = (TextView) view.findViewById(R.id.typeItems);
        mTVFirstCourse = (TextView) view.findViewById(R.id.firstCourse);
    }
    public void fillGrqphic(GraphicView graphic, List<PeriodicItem> list) {
        graphic.setListMonths(list);
    }
    public void fillPodium(Podium2 podium, List<PeriodicItem> list){

        Collections.sort(list, new Comparator<PeriodicItem>() {
            public int compare(PeriodicItem it1, PeriodicItem it2) {
                if (it1.getMoney() == it2.getMoney())
                    return 0;
                return it1.getMoney() > it2.getMoney() ? -1 : 1;
            }
        });

        podium.addGoldItem(String.format("%.2f€", list.get(0).getMoney()),
                String.format("%d cours", list.get(0).getNbCourse()),
                list.get(0).getLabel());

        if(list.size() == 1)
            return;

        podium.addSilverItem(String.format("%.2f€", list.get(1).getMoney()),
                String.format("%d cours", list.get(1).getNbCourse()),
                list.get(1).getLabel());

        if(list.size() == 2)
            return;

        podium.addBronzeItem(String.format("%.2f€", list.get(2).getMoney()),
                String.format("%d cours", list.get(2).getNbCourse()),
                list.get(2).getLabel());
    }
    public abstract List<PeriodicItem> getListItem();
    public abstract void setTypeItems(TextView textView);
    public void fillNbItems(TextView textView, int nbItems){
        textView.setText(String.format("%d", nbItems));
    }
}
