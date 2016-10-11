package com.example.la.myclass.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ariche on 11/10/2016.
 */

public class AdapterGridViewWeek extends BaseAdapter {

    Context mContext;
    Calendar mCalendar;
    int mMaxDay, firstDayOfMonth, mOffset;

    public AdapterGridViewWeek(Context context, int offset) {
        this.mContext = context;
        this.mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);

        mCalendar.add(Calendar.WEEK_OF_YEAR, offset);

        mCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        long begin = mCalendar.getTimeInMillis();
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        long end = mCalendar.getTimeInMillis();
        Log.e("WEEKLY 1", new Date(begin) + " x " + new Date(end));

        this.mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        this.mCalendar.add(Calendar.DAY_OF_YEAR,-1);
        Log.e("WEEKLY 2", mCalendar.getTime()+"");
        this.mOffset = offset;


    }

    @Override
    public int getCount() {
            return 7;
    }

    @Override
    public Object getItem(int i) {
        Calendar calendar = mCalendar;
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.DAY_OF_MONTH, i + 2 - firstDayOfMonth);
        return calendar.getTime();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_grid_month, null);
        rowView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 300));

        CoursesBDD coursesBDD = new CoursesBDD(mContext);
        coursesBDD.open();
        List<Course> courses = coursesBDD.getListCourseForADay(mCalendar.getTimeInMillis());
        coursesBDD.close();
        DevoirBDD devoirBDD = new DevoirBDD(mContext);
        devoirBDD.open();
        List<Devoir> devoirs = devoirBDD.getListDevoirForADay(mCalendar.getTimeInMillis());
        devoirBDD.close();

        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

        int pixCourseColor = mContext.getResources().getColor(R.color.them700);
        int pixDevoirColor = mContext.getResources().getColor(R.color.unthem800);

        int drawableID  = R.drawable.bkg_day_calendar;


        if (System.currentTimeMillis() - C.DAY < mCalendar.getTimeInMillis()
                && mCalendar.getTimeInMillis() < System.currentTimeMillis() + C.DAY
                && new Date().getDate() == mCalendar.get(Calendar.DAY_OF_MONTH)) {
            drawableID = R.drawable.bkg_current_day_calendar;
        }

        TextView tv = (TextView) rowView.findViewById(R.id.dayNumber);
        tv.setText(""+dayOfMonth);

        rowView.findViewById(R.id.dateLayout).setBackground(mContext.getResources().getDrawable(drawableID));

        if(courses.size() > 0)
            rowView.findViewById(R.id.isThereClass).setVisibility(View.VISIBLE);
        else
            rowView.findViewById(R.id.isThereClass).setVisibility(View.GONE);

        if(devoirs.size() > 0)
            rowView.findViewById(R.id.isThereDevoir).setVisibility(View.VISIBLE);
        else
            rowView.findViewById(R.id.isThereDevoir).setVisibility(View.GONE);

        ((ImageView)rowView.findViewById(R.id.isThereClass)).setColorFilter(pixCourseColor);
        ((ImageView)rowView.findViewById(R.id.isThereDevoir)).setColorFilter(pixDevoirColor);

        mCalendar.add(Calendar.DAY_OF_MONTH,1);

        return rowView;
    }

    public String getCurrentLabel(){
        Log.e("ADAPTERRR 0", mCalendar.get(Calendar.MONTH) + " " + C.MONTHS[mCalendar.get(Calendar.MONTH)]);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, mOffset);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        long begin = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR,6);
        long end = calendar.getTimeInMillis();
        return String.format("%s - %s", C.formatDate(begin, C.DD_MM_YY), C.formatDate(end, C.DD_MM_YY));

    }


}
