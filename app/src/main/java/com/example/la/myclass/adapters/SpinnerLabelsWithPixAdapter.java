package com.example.la.myclass.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.beans.Pupil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gaminho on 29/09/2016.
 */
public class SpinnerLabelsWithPixAdapter extends BaseAdapter {

    //Statics
    public static final String ALL = "Tous";
    public static final int INDEX_ALL = 0;
    // Dates
    public static final int TYPE_DATE = 100;
    public static final String THIS_WEEK = "Cette semaine";
    public static final int INDEX_THIS_WEEK = 1;
    public static final String THIS_MONTH = "Ce mois-ci";
    public static final int INDEX_THIS_MONTH = 2;
    public static final String THIS_YEAR = "Cette année";
    public static final int INDEX_THIS_YEAR = 3;
    // Frequences
    public static final int TYPE_FREQUENCY = 101;
    public static final String REGULAR = "Régulier";
    public static final int INDEX_REGULAR = 1;
    public static final String OCCASIONALY = "Occasionnel";
    public static final int INDEX_OCCASIONALY = 2;
    public static final String TEMPORALY = "Temporaire";
    public static final int INDEX_TEMPORALY = 3;
    // Types de paiement
    public static final int TYPE_PAIEMENT = 102;
    public static final String AGENCY = "Déclaré";
    public static final int INDEX_AGENCY = 1;
    public static final String PARTICULAR = "Non déclaré";
    public static final int INDEX_PARTICULAR = 2;


    // Variables de classe
    private Context mContext;
    private List<String> mValues;
    private boolean mShowPix;
    private int mTextColor, mPixColor;
    private int mDataType;


    public SpinnerLabelsWithPixAdapter(Context ctx, List<String> labels, boolean showPix, int textColor, int pixColor, int dataType) {
        this.mContext = ctx;
        this.mValues = labels;
        this.mShowPix = showPix;
        this.mTextColor = textColor;
        this.mPixColor = pixColor;
        this.mDataType = dataType;
    }


    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public Object getItem(int i) {
        return mValues.get(i);
    }

    @Override
    public long getItemId(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        if(mDataType==TYPE_DATE)
            switch(i){
                case INDEX_ALL:
                    return 0;
                case INDEX_THIS_WEEK:
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    return calendar.getTimeInMillis();
                case INDEX_THIS_MONTH:
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    return calendar.getTimeInMillis();
                case INDEX_THIS_YEAR:
                    calendar.set(Calendar.MONTH, Calendar.JULY);
                    calendar.set(Calendar.DAY_OF_MONTH, 30);
                    return calendar.getTimeInMillis();
                default:
                    return 0;
            }
        if(mDataType==TYPE_FREQUENCY)
            switch(i){
                case INDEX_ALL:
                    return 0;
                case INDEX_REGULAR:
                    return Pupil.REGULIAR;
                case INDEX_OCCASIONALY:
                    return Pupil.OCCASIONALY;
                case INDEX_TEMPORALY:
                    return Pupil.TEMPORALY;
                default:
                    return 0;
            }
        if(mDataType==TYPE_PAIEMENT)
            switch(i){
                case INDEX_ALL:
                    return 0;
                case INDEX_AGENCY:
                    return Pupil.AGENCY;
                case INDEX_PARTICULAR:
                    return Pupil.BLACK;
                default:
                    return 0;
            }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.adapter_cell_spinner_with_pix, null);


        TextView main_text = (TextView) mySpinner.findViewById(R.id.label);
        main_text.setTextColor(mTextColor);
        ImageView pixItem = (ImageView) mySpinner.findViewById(R.id.pixItem);
        pixItem.setColorFilter(mPixColor);

        main_text.setText(mValues.get(i));
        pixItem.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_today_white_48dp));

        return mySpinner;
    }
}
