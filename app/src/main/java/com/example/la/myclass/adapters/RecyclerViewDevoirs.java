package com.example.la.myclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.devoir.ActivityDevoir;
import com.example.la.myclass.beans.Devoir;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Gaminho on 20/09/2016.
 */

public class RecyclerViewDevoirs extends RecyclerView.Adapter<RecyclerViewDevoirs.PersonViewHolder>{


    /**
     * Variables de classe *
     */
    private Context mContext;
    private List<Devoir> mListDevoir;


    public RecyclerViewDevoirs(Context context, List<Devoir> listDevoir) {
        this.mContext = context;
        this.mListDevoir = listDevoir;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_cell_listview_devoirs, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {

        final Devoir devoir = mListDevoir.get(i);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(devoir.getDate());

        personViewHolder.dayLabel.setText(C.DAYS[calendar.get(Calendar.DAY_OF_WEEK)].substring(0, 2) + ".");
        personViewHolder.dayNumber.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));

        String month = C.MONTHS[calendar.get(Calendar.MONTH)];
        if(month.length() > 4)
            month = month.substring(0,3) + ".";

        personViewHolder.dayMonth.setText(month);
        personViewHolder.devoirThem.setText(devoir.getTheme());
        personViewHolder.devoirPupil.setText(devoir.getPupil().getFullName());
        if(devoir.getNote() > 0)
            personViewHolder.mark.setText(devoir.getNote()+"/20");
        else
            personViewHolder.mark.setText("-/20");

        personViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityDevoir.class);
                intent.putExtra(ActivityDevoir.DEVOIR_ID, devoir.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListDevoir.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView devoirThem, devoirPupil, dayNumber, dayMonth, dayLabel, mark;
        RelativeLayout row;

        PersonViewHolder(View rowView) {
            super(rowView);

            dayNumber = (TextView) rowView.findViewById(R.id.dayNumber);
            dayMonth = (TextView) rowView.findViewById(R.id.dayMonth);
            dayLabel = (TextView) rowView.findViewById(R.id.dayLabel);
            devoirThem = (TextView) rowView.findViewById(R.id.devoirThem);
            devoirPupil = (TextView) rowView.findViewById(R.id.devoirPupil);
            mark = (TextView) rowView.findViewById(R.id.mark);
            row = (RelativeLayout) rowView.findViewById(R.id.row);

        }
    }

    public void setmListDevoirs(List<Devoir> listDevoirs) {
        this.mListDevoir = listDevoirs;
    }
}
