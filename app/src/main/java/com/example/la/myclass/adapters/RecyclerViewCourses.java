package com.example.la.myclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.activities.course.ActivityCourse;
import com.example.la.myclass.beans.Course;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Gaminho on 05/11/2015.
 */
public class RecyclerViewCourses extends RecyclerView.Adapter<RecyclerViewCourses.PersonViewHolder>{


    private static final String FA_HOURGLASS = "{fa-hourglass-2}";
    private static final String FA_CHECK = "{fa-check}";
    private static final String FA_CLOSE = "{fa-close}";

    // BUNDLE
    static final String COURSE_ID = "course_id";
    private static final String COURSES_STATE = "courses_state";
    static final String COURSE_ACTION = "course_action";
    static final int ADDING = 0;



    /**
     * Variables de classe *
     */
    private Context mContext;
    private List<Course> mListCourse;


    public RecyclerViewCourses(Context context, List<Course> listCours) {
        this.mContext = context;
        this.mListCourse = listCours;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_cell_listview_courses, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {

        final Course mCourse = mListCourse.get(i);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mCourse.getDate());

        personViewHolder.dayLabel.setText(mContext.getResources().getStringArray(R.array.days)[calendar.get(Calendar.DAY_OF_WEEK)].substring(0, 2) + ".");
        personViewHolder.dayNumber.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));

        String month = mContext.getResources().getStringArray(R.array.months)[calendar.get(Calendar.MONTH)];
        if(month.length() > 4)
            month = month.substring(0,3) + ".";

        personViewHolder.dayMonth.setText(month);
        personViewHolder.coursesHours.setText(mCourse.getHoursSlot());
        personViewHolder.coursesPupil.setText(mCourse.getPupil().getFullName());

        personViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityCourse.class);
                intent.putExtra(COURSE_ID, mCourse.getId());
                mContext.startActivity(intent);
            }
        });

        switch(mCourse.getState()){
            case Course.FORESEEN :
                personViewHolder.state.setText(FA_CLOSE);
                personViewHolder.state.setTextColor(mContext.getResources().getColor(R.color.red500));
                break;
            case Course.WAITING_FOT_VALIDATION :
                personViewHolder.state.setText(FA_HOURGLASS);
                personViewHolder.state.setTextColor(mContext.getResources().getColor(R.color.them700));
                break;
            case Course.VALIDATED :
                personViewHolder.state.setText(FA_CHECK);
                personViewHolder.state.setTextColor(mContext.getResources().getColor(R.color.green500));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mListCourse.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView coursesPupil, coursesHours, dayNumber, dayMonth, dayLabel, state;
        LinearLayout dateLayout;
        RelativeLayout row;

        PersonViewHolder(View rowView) {
            super(rowView);

            dayNumber = (TextView) rowView.findViewById(R.id.dayNumber);
            dayMonth = (TextView) rowView.findViewById(R.id.dayMonth);
            dayLabel = (TextView) rowView.findViewById(R.id.dayLabel);
            coursesPupil = (TextView) rowView.findViewById(R.id.coursesPupil);
            coursesHours = (TextView) rowView.findViewById(R.id.coursesHours);
            state = (TextView) rowView.findViewById(R.id.state);
            row = (RelativeLayout) rowView.findViewById(R.id.row);
            dateLayout = (LinearLayout) rowView.findViewById(R.id.dateLayout);

        }
    }

    public void setmListCourses(List<Course> listCourses) {
        this.mListCourse = listCourses;
    }
}

