package com.example.la.myclass.activities.stats;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.database.PupilsBDD;

/**
 * Created by Gaminho on 21/09/2016.
 */
public class FragmentStatsGlobal extends Fragment {



    // Fragment life cycle
    public static FragmentStatsGlobal newInstance() {
        return new FragmentStatsGlobal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_global, container, false);

        fillPupilsStats(view);
        fillCourseStats(view);
        fillDevoirsStats(view);

        return view;
    }


    // Utils

    public void fillPupilsStats(View view){
        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        ((TextView) view.findViewById(R.id.nbPupils)).setText(String.format("%d", pupilsBDD.getAllPupils().size()));
        ((TextView) view.findViewById(R.id.nbAgencyPupil)).setText(String.format("%d", pupilsBDD.getPupilsWithCriteria(String.format("%s = %d", PupilsBDD.COL_TYPE, Pupil.AGENCY)).size()));
        ((TextView) view.findViewById(R.id.nbBlackPupil)).setText(String.format("%d", pupilsBDD.getPupilsWithCriteria(String.format("%s = %d", PupilsBDD.COL_TYPE, Pupil.BLACK)).size()));
        ((TextView) view.findViewById(R.id.nbRegularPupils)).setText(String.format("%d", pupilsBDD.getPupilsWithCriteria(String.format("%s = %d", PupilsBDD.COL_FREQUENCE, Pupil.REGULIAR)).size()));
        ((TextView) view.findViewById(R.id.nbOccasPupils)).setText(String.format("%d", pupilsBDD.getPupilsWithCriteria(String.format("%s = %d", PupilsBDD.COL_FREQUENCE, Pupil.OCCASIONALY)).size()));
        ((TextView) view.findViewById(R.id.nbTempPupils)).setText(String.format("%d", pupilsBDD.getPupilsWithCriteria(String.format("%s = %d", PupilsBDD.COL_FREQUENCE, Pupil.TEMPORALY)).size()));
        ((TextView) view.findViewById(R.id.nbMalePupils)).setText(String.format("%d", pupilsBDD.getPupilsWithCriteria(String.format("%s = %d", PupilsBDD.COL_SEX, Pupil.SEXE_MALE)).size()));
        ((TextView) view.findViewById(R.id.nbFemalePupils)).setText(String.format("%d", pupilsBDD.getPupilsWithCriteria(String.format("%s = %d", PupilsBDD.COL_SEX, Pupil.SEXE_FEMALE)).size()));
        ((TextView) view.findViewById(R.id.nbCollegePupils)).setText(String.format("%d", pupilsBDD.getPupilsWithCriteria(String.format("%s < 4", PupilsBDD.COL_CLASS)).size()));
        ((TextView) view.findViewById(R.id.nbLyceePupils)).setText(String.format("%d", pupilsBDD.getPupilsWithCriteria(String.format("%s > 3", PupilsBDD.COL_CLASS)).size()));
        pupilsBDD.close();
    }

    public void fillCourseStats(View view){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        ((TextView) view.findViewById(R.id.nbCours)).setText(String.format("%d", coursesBDD.getAllCourses().size()));
        ((TextView) view.findViewById(R.id.nbValidatedCourses)).setText(String.format("%d", coursesBDD.getCourseWithCriteria(String.format("%s = %d", CoursesBDD.COL_STATE, Course.VALIDATED)).size()));
        ((TextView) view.findViewById(R.id.nbWaitingCourses)).setText(String.format("%d", coursesBDD.getCourseWithCriteria(String.format("%s = %d", CoursesBDD.COL_STATE, Course.WAITING_FOT_VALIDATION)).size()));
        ((TextView) view.findViewById(R.id.nbForessenCourses)).setText(String.format("%d", coursesBDD.getCourseWithCriteria(String.format("%s = %d", CoursesBDD.COL_STATE, Course.FORESEEN)).size()));
        ((TextView) view.findViewById(R.id.nbCanceledCourses)).setText(String.format("%d", coursesBDD.getCourseWithCriteria(String.format("%s = %d", CoursesBDD.COL_STATE, Course.CANCELED)).size()));
        coursesBDD.close();
    }

    public void fillDevoirsStats(View view){
        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        ((TextView) view.findViewById(R.id.nbDevoirs)).setText(String.format("%d", devoirBDD.getAllDevoirs().size()));
        ((TextView) view.findViewById(R.id.nbDMDevoir)).setText(String.format("%d", devoirBDD.getDevoirWithCriteria(String.format("%s = %d", DevoirBDD.COL_TYPE, Devoir.DM)).size()));
        ((TextView) view.findViewById(R.id.nbInterroDevoir)).setText(String.format("%d", devoirBDD.getDevoirWithCriteria(String.format("%s = %d", DevoirBDD.COL_TYPE, Devoir.INTERRO)).size()));
        ((TextView) view.findViewById(R.id.nbDSTDevoir)).setText(String.format("%d", devoirBDD.getDevoirWithCriteria(String.format("%s = %d", DevoirBDD.COL_TYPE, Devoir.DST)).size()));
        devoirBDD.close();
    }

}
