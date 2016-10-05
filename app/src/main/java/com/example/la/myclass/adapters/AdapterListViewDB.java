package com.example.la.myclass.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.MyDB;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gaminho on 24/08/2016.
 */
public class AdapterListViewDB extends BaseAdapter {

    /**
     * Variables de classe *
     */
    private Context mContext;
    private List<MyDB> mListDB;
    private SharedPreferences mSharedPreferences;


    /**
     * Constructeur *
     */
    public AdapterListViewDB(Context context, List<MyDB> dbs) {
        this.mContext = context;
        this.mListDB = dbs;
        this.mSharedPreferences =  context.getSharedPreferences(C.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de la vue de la cellule
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_cell_listview_dbs, null);

        MyDB myDB = mListDB.get(position);

        TextView date, size, name;
        name = (TextView) rowView.findViewById(R.id.tvCurrentDBName);
        name.setText(myDB.getName());

        size = (TextView) rowView.findViewById(R.id.tvCurrentDBSize);
        size.setText(C.formatSize(myDB.getSize()));

        date = (TextView) rowView.findViewById(R.id.tvCurrentDBDate);
        date.setText(
                String.format("%s à %s",
                        C.formatDate(myDB.getDate(), C.DATE_FORMAT_SHORT_MONTH),
                        C.formatDate(myDB.getDate(), C.HH_mm)));

        if(myDB.getName().equals(mSharedPreferences.getString(C.CURRENT_DB, "")))
            rowView.findViewById(R.id.currentDB).setVisibility(View.VISIBLE);
        else
            rowView.findViewById(R.id.currentDB).setVisibility(View.INVISIBLE);

        return rowView;
    }

    @Override
    public int getCount() {
        return mListDB.size();
    }

    @Override
    public Object getItem(int position) {
        return mListDB.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListDB.size();
    }


    public void setmListDBs(List<MyDB> listDBs){
        this.mListDB = listDBs;
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
