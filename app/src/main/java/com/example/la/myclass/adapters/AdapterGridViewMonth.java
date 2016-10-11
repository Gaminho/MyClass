package com.example.la.myclass.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.periodic.Month;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ariche on 10/10/2016.
 */

public class AdapterGridViewMonth extends BaseAdapter {

    Context mContext;
    Calendar mCalendar;
    int mMaxDay, firstDayOfMonth, currentMonth, mOffset;

    public AdapterGridViewMonth(Context context, int offset) {
        this.mContext = context;
        this.mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.DAY_OF_MONTH, 15);
        mCalendar.add(Calendar.MONTH, offset);

        this.currentMonth = mCalendar.get(Calendar.MONTH);
        this.mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        this.firstDayOfMonth = mCalendar.get(Calendar.DAY_OF_WEEK);
        this.mMaxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mCalendar.add(Calendar.DAY_OF_MONTH, 1-firstDayOfMonth);
        this.mOffset = offset;
    }

    @Override
    public int getCount() {
        return 42;
    }

    @Override
    public Object getItem(int i) {
        Calendar calendar = mCalendar;
        calendar.set(Calendar.MONTH,currentMonth);
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
        rowView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 200));

        CoursesBDD coursesBDD = new CoursesBDD(mContext);
        coursesBDD.open();
        List<Course> courses = coursesBDD.getListCourseForADay(mCalendar.getTimeInMillis());
        coursesBDD.close();
        DevoirBDD devoirBDD = new DevoirBDD(mContext);
        devoirBDD.open();
        List<Devoir> devoirs = devoirBDD.getListDevoirForADay(mCalendar.getTimeInMillis());
        devoirBDD.close();

        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        int indexOfMonth = mCalendar.get(Calendar.MONTH);
        int pixCourseColor = mContext.getResources().getColor(R.color.them700);
        int pixDevoirColor = mContext.getResources().getColor(R.color.unthem800);

        int drawableID  = R.drawable.bkg_day_calendar;


        if(indexOfMonth < currentMonth || indexOfMonth > currentMonth ) {
            drawableID = R.drawable.bkg_disabled_day_calendar;
            pixCourseColor = pixDevoirColor = mContext.getResources().getColor(R.color.secondaryColor);
        }

        else if (System.currentTimeMillis() - C.DAY < mCalendar.getTimeInMillis()
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
        Log.e("ADAPTERRR 1", mCalendar.getTime() + "");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        //calendar.set(Calendar.MONTH, currentMonth);
        calendar.add(Calendar.MONTH, mOffset);
        Log.e("ADAPTERRR 2", calendar.getTime() + "");
        return String.format("%s %d",
                C.MONTHS[calendar.get(Calendar.MONTH)],
                calendar.get(Calendar.YEAR)
        );

    }


}
