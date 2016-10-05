package com.example.la.myclass.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.beans.Devoir;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Léa on 04/10/2015.
 */
public class AdapterListViewDevoirs extends BaseAdapter {

    /**
     * Variables de classe *
     */
    private Context mContext;
    private List<Devoir> mListDevoirs;


    /**
     * Constructeur *
     */
    public AdapterListViewDevoirs(Context context, List<Devoir> devoirs) {
        this.mContext = context;
        this.mListDevoirs = devoirs;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de la vue de la cellule
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_cell_listview_devoirs, null);

        Devoir devoir = mListDevoirs.get(position);

        TextView date, pupilName, state;
        date = (TextView) rowView.findViewById(R.id.date);
        date.setText(devoir.getTheme());

        pupilName = (TextView) rowView.findViewById(R.id.pupilName);
        pupilName.setText(devoir.getPupil().getFullName());

        state = (TextView) rowView.findViewById(R.id.state);
        state.setText(formatDate(devoir.getDate()) + " ("+devoir.getId()+")");

        return rowView;
    }

    @Override
    public int getCount() {
        return mListDevoirs.size();
    }

    @Override
    public Object getItem(int position) {
        return mListDevoirs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListDevoirs.size();
    }


    public void setmListDevoirs(List<Devoir> listDevoirs){
        this.mListDevoirs = listDevoirs;
    }

    public String formatDate(long milliseconds){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(milliseconds));

        return String.format("%02d/%02d/%02d",
                calendar.get(Calendar.DAY_OF_MONTH),
                (calendar.get(Calendar.MONTH) + 1),
                (calendar.get(Calendar.YEAR) - 2000));
    }
}
