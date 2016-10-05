package com.example.la.myclass.activities;

import android.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.la.myclass.R;

public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {

    // Statics
    public static int INDEX_HOME = 0;
    public static int INDEX_PUPILS = 1;
    public static int INDEX_CLASSES = 2;
    public static int INDEX_DEVOIRS = 3;
    public static int INDEX_WEEKS = 4;
    public static int INDEX_MONEY = 5;
    public static int INDEX_BILAN = 6;

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;

    public static String[] drawer_sections;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawer_sections = getActivity().getResources().getStringArray(R.array.drawer_sections);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, true);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(mCurrentSelectedPosition, drawer_sections[mCurrentSelectedPosition]);
        }
        selectItem(mCurrentSelectedPosition);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) view.findViewById(R.id.listSections);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
                if (mCallbacks != null) {
                    mCallbacks.onNavigationDrawerItemSelected(position, drawer_sections[position]);
                }
            }
        });

        mDrawerListView.setAdapter(new AdapterListViewDrawer(getActivity(), drawer_sections));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        return view;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final ActionBar actionBar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        actionBar.getCustomView().findViewById(R.id.icDrawer).setVisibility(View.VISIBLE);
        actionBar.getCustomView().findViewById(R.id.icDrawer).setOnClickListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
                ((ImageView)actionBar.getCustomView().findViewById(R.id.icDrawer))
                        .setImageDrawable(getResources().getDrawable
                                (R.drawable.ic_drawer));

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);


                ((ImageView)actionBar.getCustomView().findViewById(R.id.icDrawer))
                        .setImageDrawable(getResources().getDrawable
                                (R.drawable.ic_arrow_back_white_48dp));

                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.icDrawer:
                if(mDrawerLayout.isDrawerOpen(mFragmentContainerView))
                    mDrawerLayout.closeDrawers();
                else
                    mDrawerLayout.openDrawer(mFragmentContainerView);
        }
    }

    public interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position, String label);
    }








    // Adapters

    public class AdapterListViewDrawer extends BaseAdapter {

        // Variables de classe
        private Context mContext;
        private String[] mSections;


        /**
         * Constructeur *
         */
        public AdapterListViewDrawer(Context context, String[]sections) {
            this.mContext = context;
            this.mSections = sections;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            // Récupération de la vue de la cellule
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View rowView = inflater.inflate(R.layout.adapter_cell_listview_drawer, null);
            View rowView = inflater.inflate(R.layout.adapter_cell_drawer, null);

            String[] arr2 = mContext.getResources().getStringArray(R.array.drawer_sections0);

            TextView label, icon;
            label = (TextView) rowView.findViewById(R.id.label);
            label.setText(arr2[position].split(";")[1]);

            icon = (TextView) rowView.findViewById(R.id.icon);
            icon.setText(arr2[position].split(";")[0]);

            int color = mContext.getResources().getColor(R.color.calendar);
            icon.setTextColor(color);

            return rowView;
        }

        @Override
        public int getCount() {
            return mSections.length;
        }

        @Override
        public Object getItem(int position) {
            return mSections[position];
        }

        @Override
        public long getItemId(int position) {
            return mSections.length;
        }

    }


}
