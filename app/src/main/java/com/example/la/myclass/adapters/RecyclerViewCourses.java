package com.example.la.myclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


    // BUNDLE
    static final String COURSE_ID = "course_id";


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
                intent.putExtra(ActivityCourse.COURSE_ID, mCourse.getId());
                mContext.startActivity(intent);
            }
        });

        switch(mCourse.getState()){
            case Course.FORESEEN :
                personViewHolder.imageState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_schedule_white_48dp));
                personViewHolder.imageState.setColorFilter(mContext.getResources().getColor(R.color.unthem600));
                break;
            case Course.WAITING_FOT_VALIDATION :
                personViewHolder.imageState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_done_black_48dp));
                personViewHolder.imageState.setColorFilter(mContext.getResources().getColor(R.color.them700));
                break;
            case Course.CANCELED :
                personViewHolder.imageState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_clear_white_48dp));
                personViewHolder.imageState.setColorFilter(mContext.getResources().getColor(R.color.red500));
                break;
            case Course.VALIDATED :
                personViewHolder.imageState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_done_all_white_48dp));
                personViewHolder.imageState.setColorFilter(mContext.getResources().getColor(R.color.green500));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mListCourse.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView coursesPupil, coursesHours, dayNumber, dayMonth, dayLabel;
        ImageView imageState;
        LinearLayout dateLayout;
        RelativeLayout row;

        PersonViewHolder(View rowView) {
            super(rowView);

            dayNumber = (TextView) rowView.findViewById(R.id.dayNumber);
            dayMonth = (TextView) rowView.findViewById(R.id.dayMonth);
            dayLabel = (TextView) rowView.findViewById(R.id.dayLabel);
            coursesPupil = (TextView) rowView.findViewById(R.id.coursesPupil);
            coursesHours = (TextView) rowView.findViewById(R.id.coursesHours);
            row = (RelativeLayout) rowView.findViewById(R.id.row);
            dateLayout = (LinearLayout) rowView.findViewById(R.id.dateLayout);
            imageState = (ImageView) rowView.findViewById(R.id.imageState);
        }
    }

    public void setmListCourses(List<Course> listCourses) {
        this.mListCourse = listCourses;
    }
}

