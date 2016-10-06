package com.example.la.myclass.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.adapters.RecyclerViewWeek;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.periodic.Week;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Léa on 02/10/2015.
 */
public class FragmentHome extends Fragment {

    // Views
    protected RecyclerView mListViewWeekDays;

    protected TextView mTextViewCours, mTextViewDevoirs, mTextViewMoney, mTextViewCountDown;


    // Variables de classe
    protected Week mActualWeek;
    protected List<Course> mListCourses;
    protected List<Devoir> mListDevoirs;
    protected CountDownTimer mCountDownTimer;


    // Fragment life cycle
    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    public FragmentHome() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActualWeek = new Week();

        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        mListCourses = coursesBDD.getCoursesBetweenTwoDates(mActualWeek.getBeginning(), mActualWeek.getEnding());
        coursesBDD.close();

        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        mListDevoirs = devoirBDD.getDevoirsBetweenTwoDates(mActualWeek.getBeginning(), mActualWeek.getEnding());
        devoirBDD.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getAllViews(view);

        fillListWeekViews();
        fillBottomBar();

        setUpTimer();

        return view;
    }





    // Utils

    public void getAllViews(View view){

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mListViewWeekDays = (RecyclerView) view.findViewById(R.id.weekList);
        mListViewWeekDays.setLayoutManager(llm);


        mTextViewCours = (TextView) view.findViewById(R.id.cours);
        mTextViewDevoirs = (TextView) view.findViewById(R.id.devoirs);
        mTextViewMoney = (TextView) view.findViewById(R.id.money);
        mTextViewCountDown = (TextView) view.findViewById(R.id.timer);

    }

    public void fillBottomBar(){

        String nbCourse = ""+0;
        if(mListCourses != null)
            nbCourse = ""+mListCourses.size();

        mTextViewCours.setText(nbCourse);

        String nbDevoirs = ""+0;
        if(mListDevoirs != null)
            nbDevoirs = ""+mListDevoirs.size();

        mTextViewDevoirs.setText(nbDevoirs);

        double money = 0;
        if(mListCourses != null)
            for(Course course : mListCourses)
                money = money + course.getMoney();

        mTextViewMoney.setText(String.format("%.2f", money));

    }


    public Course getNextCourse(){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> list = coursesBDD.getCoursesWithState(Course.FORESEEN);
        coursesBDD.close();

        if (list != null)
            return list.get(0);

        return null;
    }

    public Devoir getNextDevoir(){
        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        List<Devoir> list = devoirBDD.getDevoirsWithState(Devoir.STATE_PREPARATING);
        devoirBDD.close();

        if (list != null)
            return list.get(0);

        return null;
    }



    public void fillListWeekViews() {
        mListViewWeekDays.setAdapter(new RecyclerViewWeek(getActivity(), mActualWeek, mListCourses));
    }

    public void setUpTimer() {

        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> list = coursesBDD.getCoursesWithState(Course.FORESEEN);
        coursesBDD.close();

        if (list .size()>0) {
            Course course = list.get(0);

            long milliseconds = course.getDate() - new Date().getTime();

            mCountDownTimer = new CountDownTimer(milliseconds, C.SECOND) {
                @Override
                public void onTick(long milliseconds) {

                    String str = String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(milliseconds),
                            TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                            TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
                    );

                    mTextViewCountDown.setText(str);
                }

                @Override
                public void onFinish() {
                    mTextViewCountDown.setText("-");
                }
            }.start();
        }
    }


    public int getWaitingForValidationCourses(){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> list = coursesBDD.getCoursesWithState(Course.WAITING_FOT_VALIDATION);
        coursesBDD.close();

        if(list == null)
            return 0;

        return list.size();
    }

}
