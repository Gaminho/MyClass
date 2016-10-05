package com.example.la.myclass.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.beans.Pupil;

import java.io.File;
import java.util.List;

/**
 * Created by Gaminho on 27/09/2016.
 */
public class SpinnerPupilWithPixAdapter extends BaseAdapter {

    //Statics

    // Variables de classe
    private Context mContext;
    private List<Pupil> mValues;
    private boolean mShowPix;
    private int mtextColor;


    public SpinnerPupilWithPixAdapter(Context ctx, List<Pupil> pupilList, boolean showPix, int textColor) {
        this.mContext = ctx;
        this.mValues = pupilList;
        this.mShowPix = showPix;
        this.mtextColor = textColor;
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
        return mValues.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.adapter_cell_spinner_with_pix, null);

        TextView main_text = (TextView) mySpinner.findViewById(R.id.label);
        main_text.setTextColor(mtextColor);
        ImageView pixItem = (ImageView) mySpinner.findViewById(R.id.pixItem);


        Pupil pupil = mValues.get(i);
        if(!mShowPix)
            pixItem.setVisibility(View.GONE);

        main_text.setText(pupil.getFullName());
        if(new File(pupil.getImgPath()).exists())
            pixItem.setImageBitmap(BitmapFactory.decodeFile(pupil.getImgPath()));
        else {
            if (pupil.getSex() == Pupil.SEXE_MALE)
                pixItem.setImageDrawable(mContext.getResources().getDrawable(R.drawable.man));
            else
                pixItem.setImageDrawable(mContext.getResources().getDrawable(R.drawable.woman));
        }

        return mySpinner;
    }

    public List<Pupil> getmValues() {
        return mValues;
    }

    public int getSelectionWithPupilID(int pupilId){
        int selection = 0;
        for(int i = 0 ; i < mValues.size() ; i++)
            if(mValues.get(i).getId() == pupilId)
                selection = i;
        return selection;
    }
}