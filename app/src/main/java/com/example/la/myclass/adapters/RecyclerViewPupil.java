package com.example.la.myclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.R;
import com.example.la.myclass.activities.pupil.ActivityPupil;
import com.example.la.myclass.beans.Pupil;

import java.io.File;
import java.util.List;

/**
 * Created by Léa on 09/10/2015.
 */
public class RecyclerViewPupil extends RecyclerView.Adapter<RecyclerViewPupil.PersonViewHolder>{

    // BUNDLE
    static final String PUPIL_NAME = "pupil_name";
    static final String PUPIL_ID = "pupil_id";
    static final String PUPIL_ACTION = "pupil_action";
    static final int ADDING = 0;
    static final int SEEING = 1;



    List<Pupil> mLisPupils;
    Context mContext;

    public RecyclerViewPupil(Context context, List<Pupil> persons){
        this.mContext = context;
        this.mLisPupils = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_cell_listview_pupils, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {

        final Pupil pupil = mLisPupils.get(i);
        personViewHolder.personName.setText(pupil.getFullName());
        personViewHolder.personClass.setText(mContext.getResources().getStringArray(R.array.classes)[pupil.getLevel()]);

        personViewHolder.personPhoto.setImageDrawable(null);



        if(!"".equals(pupil.getImgPath()) && new File(pupil.getImgPath()).exists())
            personViewHolder.personPhoto.setImageBitmap(BitmapFactory.decodeFile(pupil.getImgPath()));
        else {
            if (pupil.getSex() == Pupil.SEXE_FEMALE)
                personViewHolder.personPhoto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.woman));
            else
                personViewHolder.personPhoto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.man));
        }


        personViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Toast.makeText(mContext, "Appeler " + pupil.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });

        personViewHolder.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Aller chez " + pupil.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });


        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityPupil.class);
                intent.putExtra(PUPIL_NAME, pupil.getFullName());
                intent.putExtra(PUPIL_ID, pupil.getId());
                intent.putExtra(PUPIL_ACTION, SEEING);
                mContext.startActivity(intent);
            }
        });

        personViewHolder.tvUnactive.setVisibility(View.GONE);
        if(pupil.getState() == Pupil.DESACTIVE)
            personViewHolder.tvUnactive.setVisibility(View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return mLisPupils.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personClass;
        TextView call, goTo;
        ImageView personPhoto;
        TextView tvUnactive;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.pupilName);
            personClass = (TextView)itemView.findViewById(R.id.pupilClass);
            personPhoto = (ImageView)itemView.findViewById(R.id.avatar);
            call = (TextView) itemView.findViewById(R.id.call);
            goTo = (TextView) itemView.findViewById(R.id.goTo);
            tvUnactive = (TextView) itemView.findViewById(R.id.unActive);
        }
    }



    public void setmListPupils(List<Pupil> listPupils) {
        this.mLisPupils = listPupils;
    }

}
