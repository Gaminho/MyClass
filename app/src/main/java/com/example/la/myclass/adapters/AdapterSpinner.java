package com.example.la.myclass.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.database.PupilsBDD;

/**
 * Created by Gaminho on 26/09/2016.
 */
public class AdapterSpinner extends ArrayAdapter<String> {

    private Context mContext;
    private String[] mValues;
    private int mTextColor;


    public AdapterSpinner(Context ctx, int txtViewResourceId, String[] objects) {
        super(ctx, txtViewResourceId, objects);
        this.mContext = ctx;
        this.mValues = objects;
    }

    public AdapterSpinner(Context ctx, int txtViewResourceId, String[] objects, int textColor) {
        super(ctx, txtViewResourceId, objects);
        this.mContext = ctx;
        this.mValues = objects;
        this.mTextColor = textColor;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.adapter_cell_spinner_course, parent, false);
        TextView label = (TextView) mySpinner.findViewById(R.id.label);
        label.setText(mValues[position]);
        if(mTextColor != 0)
        label.setTextColor(mTextColor);

        return mySpinner;
    }
}
