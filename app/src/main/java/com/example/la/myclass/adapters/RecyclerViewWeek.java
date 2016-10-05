package com.example.la.myclass.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.activities.FragmentDay;
import com.example.la.myclass.activities.MainActivity;
import com.example.la.myclass.activities.devoir.FragmentListDevoir;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Week;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gaminho on 05/11/2015.
 */
public class RecyclerViewWeek extends RecyclerView.Adapter<RecyclerViewWeek.PersonViewHolder>{

    // Statics
    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;
    private static final long HOUR = 3600 * SECOND;
    private static final long DAY = HOUR * 24;


    /**
     * Variables de classe *
     */
    private Context mContext;
    private Week mWeek;
    private List<Course> mListCourse;


    public RecyclerViewWeek(Context context, Week week, List<Course> listCours) {
        this.mContext = context;
        this.mWeek = week;
        this.mListCourse = listCours;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_cell_listview_week, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {

        final long date = mWeek.getBeginning() + i * DAY;
        if (new Date().getTime() > date && new Date().getTime() < date + DAY) {
            personViewHolder.dateLayout.setBackgroundColor(mContext.getResources().getColor(R.color.unthem300));
            personViewHolder.dayNumber.setTextColor(mContext.getResources().getColor(R.color.them600));
            personViewHolder.dayMonth.setTextColor(mContext.getResources().getColor(R.color.them600));
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));

        personViewHolder.dayNumber.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));

        String month = mContext.getResources().getStringArray(R.array.months)[calendar.get(Calendar.MONTH)];
        if(month.length() > 4)
            month = month.substring(0,3) + ".";

        personViewHolder.dayMonth.setText(month);

        int nbCours = 0;
        String description = "";

        if(mListCourse != null)
            for(Course course : mListCourse){
                Log.e("DEBUGGONS", course.toString());
                if (course.getDate() > date && course.getDate() < date + DAY) {
                    nbCours = nbCours + 1;
                    Date d = new Date(course.getDate());
                    Date d2 = new Date((course.getDate() + course.getDuration() * MINUTE));
                    description = description + String.format( "%02d:%02d - %02d:%02d : ", d.getHours(), d.getMinutes(), d2.getHours(), d2.getMinutes());
                    description = description + course.getPupil().getFullName() + "\n";
                }
            }

        if(!description.equals(""))
            description = description.substring(0, description.length() - 1);

        if(nbCours == 0)
            personViewHolder.courses.setVisibility(View.GONE);

        personViewHolder.courses.setText(description);

        personViewHolder.viewDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).setCurrentFragment(FragmentDay.newInstance(date));
                ((Activity)mContext).getFragmentManager().beginTransaction().replace(R.id.container, FragmentDay.newInstance(date)).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView courses, dayNumber, dayMonth;
        LinearLayout row, dateLayout, viewDay;

        PersonViewHolder(View rowView) {
            super(rowView);

            dayNumber = (TextView) rowView.findViewById(R.id.dayNumber);
            dayMonth = (TextView) rowView.findViewById(R.id.dayMonth);
            courses = (TextView) rowView.findViewById(R.id.courses);
            row = (LinearLayout) rowView.findViewById(R.id.row);
            dateLayout = (LinearLayout) rowView.findViewById(R.id.dateLayout);
            viewDay = (LinearLayout) rowView.findViewById(R.id.viewDay);

        }
    }


}

