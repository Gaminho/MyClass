package com.example.la.myclass.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.periodic.Month;
import com.example.la.myclass.database.CoursesBDD;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ariche on 10/10/2016.
 */

public class AdapterGridViewMonth extends BaseAdapter {

    Context mContext;
    Calendar mCalendar;
    int mMaxDay, firstDayOfMonth, currentMonth;

    public AdapterGridViewMonth(Context context, int offsetMonth) {
        this.mContext = context;
        this.mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.add(Calendar.MONTH, offsetMonth);
        this.currentMonth = mCalendar.get(Calendar.MONTH);
        this.mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        this.firstDayOfMonth = mCalendar.get(Calendar.DAY_OF_WEEK);
        this.mMaxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mCalendar.add(Calendar.DAY_OF_MONTH, 1-firstDayOfMonth);
    }

    @Override
    public int getCount() {
        return 42;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_grid_month, null);
        rowView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 200));

        CoursesBDD coursesBDD = new CoursesBDD(mContext);
        coursesBDD.open();
        List<Course> list = coursesBDD.getListCourseForADay(mCalendar.getTimeInMillis());
        coursesBDD.close();

        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        int indexOfMonth = mCalendar.get(Calendar.MONTH);

        int textColor   = mContext.getResources().getColor(R.color.green500);
        int drawableID  = R.drawable.bkg_day_calendar;


        if(indexOfMonth < currentMonth || indexOfMonth > currentMonth ) {
            textColor = mContext.getResources().getColor(R.color.red500);
            drawableID = R.drawable.bkg_disabled_day_calendar;
        }

        else if (System.currentTimeMillis() - C.DAY < mCalendar.getTimeInMillis()
                && mCalendar.getTimeInMillis() < System.currentTimeMillis() + C.DAY
                && new Date().getDate() == mCalendar.get(Calendar.DAY_OF_MONTH)) {
            textColor = mContext.getResources().getColor(R.color.them500);
            drawableID = R.drawable.bkg_current_day_calendar;
        }

        TextView tv = (TextView) rowView.findViewById(R.id.dayNumber);
        tv.setText(""+dayOfMonth);
        tv.setTextColor(textColor);

        rowView.findViewById(R.id.dateLayout).setBackground(mContext.getResources().getDrawable(drawableID));

        if(list.size() > 0)
            rowView.findViewById(R.id.dateLayout).setBackgroundColor(mContext.getResources().getColor(R.color.pupils));

        mCalendar.add(Calendar.DAY_OF_MONTH,1);

        return rowView;
    }

}
