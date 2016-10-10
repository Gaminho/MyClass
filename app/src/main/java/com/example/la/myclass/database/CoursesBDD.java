package com.example.la.myclass.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.periodic.Month;
import com.example.la.myclass.beans.periodic.PeriodicItem;
import com.example.la.myclass.beans.periodic.Week;
import com.example.la.myclass.beans.periodic.Year;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.example.la.myclass.database.PupilsBDD.COL_STATE;
import static com.example.la.myclass.database.PupilsBDD.PUPILS_FIELDS;

/**
 * Created by Léa on 01/10/2015.
 */
public class CoursesBDD {

    private static final String NOM_BDD = MyDatabase.DB_NAME;

    public static final String TABLE_COURSES = "courses_table";
    public static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    public static final String COL_DATE = "DATE";
    public static final int NUM_COL_DATE = 1;
    public static final String COL_DURATION = "DURATION";
    public static final int NUM_COL_DURATION = 2;
    public static final String COL_STATE = "STATE";
    public static final int NUM_COL_STATE = 3;
    public static final String COL_MONEY = "MONEY";
    public static final int NUM_COL_MONEY = 4;
    public static final String COL_THEME = "THEME";
    public static final int NUM_COL_THEME = 5;
    public static final String COL_MEMO = "MEMO";
    public static final int NUM_COL_MEMO = 6;
    public static final String COL_PUPIL_ID = "PUPIL_ID";
    public static final int NUM_COL_PUPIL_ID = 7;

    public static final String[] COURSES_FIELDS = new String[]{COL_ID, COL_DATE, COL_DURATION, COL_STATE, COL_MONEY, COL_THEME, COL_MEMO, COL_PUPIL_ID};

    private SQLiteDatabase bdd;

    private MyDatabase maBaseSQLite;

    public CoursesBDD(Context context) {
        maBaseSQLite = new MyDatabase(context, NOM_BDD, null, MyDatabase.VERSION_BDD);
    }

    public void open() {
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        bdd.close();
    }

    public SQLiteDatabase getBDD() {
        return bdd;
    }

    public long insertCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(COL_DATE, course.getDate());
        values.put(COL_DURATION, course.getDuration());
        values.put(COL_STATE, course.getState());
        values.put(COL_MONEY, course.getMoney());
        values.put(COL_THEME, course.getTheme());
        values.put(COL_MEMO, course.getMemo());
        values.put(COL_PUPIL_ID, course.getPupilID());
        return bdd.insert(TABLE_COURSES, null, values);
    }

    public int updateCourse(int id, Course course) {
        ContentValues values = new ContentValues();
        values.put(COL_DATE, course.getDate());
        values.put(COL_DURATION, course.getDuration());
        values.put(COL_STATE, course.getState());
        values.put(COL_MONEY, course.getMoney());
        values.put(COL_THEME, course.getTheme());
        values.put(COL_MEMO, course.getMemo());
        values.put(COL_PUPIL_ID, course.getPupilID());

        return bdd.update(TABLE_COURSES, values, COL_ID + " = " + id, null);
    }

    public int removeCourseWithID(int id) {
        return bdd.delete(TABLE_COURSES, COL_ID + " = " + id, null);
    }

    public Course getCourseWithId(int id) {
        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, COL_ID + " = " + id, null, null, null, null);
        return cursorToCourse(c);
    }

    public Course getNextCourse() {
        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, COL_STATE + " = " + Course.FORESEEN, null, null, null, COL_DATE, "1");
        return cursorToCourse(c);
    }

    public Course getFirstCourse() {
        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, null, null, null, null, COL_DATE, "1");
        return cursorToCourse(c);
    }

    public List<Course> getCourseWithPupilId(int pupilId) {
        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, COL_PUPIL_ID + " = " + pupilId, null, null, null, COL_DATE + " DESC");
        return cursorToListCourses(c);
    }

    public List<Course> getCourseWithCriteria(String criteria) {
        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, criteria, null, null, null, COL_DATE + " DESC");
        return cursorToListCourses(c);
    }

    private Course cursorToCourse(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Course course = new Course();
        course.setId(c.getInt(NUM_COL_ID));
        course.setDate(c.getLong(NUM_COL_DATE));
        course.setDuration(c.getInt(NUM_COL_DURATION));
        course.setState(c.getInt(NUM_COL_STATE));
        course.setMoney(c.getDouble(NUM_COL_MONEY));
        course.setTheme(c.getString(NUM_COL_THEME));
        course.setMemo(c.getString(NUM_COL_MEMO));
        course.setPupilID(c.getInt(NUM_COL_PUPIL_ID));
        Cursor c0 = bdd.query(PupilsBDD.TABLE_PUPILS, PupilsBDD.PUPILS_FIELDS, COL_ID + " = " + c.getInt(NUM_COL_PUPIL_ID), null, null, null, null);
        course.setPupil(PupilsBDD.cursorToPupil(c0));
        c0.close();

        return course;
    }

    protected List<Course> cursorToListCourses(Cursor c) {
        List<Course> list = new ArrayList<>();
        if (c.getCount() == 0)
            return list;

        c.moveToFirst();

        while (!c.isAfterLast()) {
            Course course = new Course();
            course.setId(c.getInt(NUM_COL_ID));
            course.setDate(c.getLong(NUM_COL_DATE));
            course.setDuration(c.getInt(NUM_COL_DURATION));
            course.setState(c.getInt(NUM_COL_STATE));
            course.setMoney(c.getDouble(NUM_COL_MONEY));
            course.setTheme(c.getString(NUM_COL_THEME));
            course.setMemo(c.getString(NUM_COL_MEMO));
            course.setPupilID(c.getInt(NUM_COL_PUPIL_ID));
            Cursor c0 = bdd.query(PupilsBDD.TABLE_PUPILS, PupilsBDD.PUPILS_FIELDS, COL_ID + " = " + c.getInt(NUM_COL_PUPIL_ID), null, null, null, null);
            course.setPupil(PupilsBDD.cursorToPupil(c0));
            c0.close();

            list.add(course);
            c.moveToNext();
        }

        c.close();

        return list;
    }

    public List<Course> getAllCourses() {
        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, null, null, null, null, COL_DATE + " DESC");
        return cursorToListCourses(c);
    }

    public List<Course> getActiveCourses() {
        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, CoursesBDD.COL_STATE + " != " + Course.CANCELED, null, null, null, COL_DATE + " DESC");
        return cursorToListCourses(c);
    }

    public int getNumberOfCourses() {
        Cursor c = bdd.rawQuery("SELECT * FROM " + TABLE_COURSES, null);
        return c.getCount();
    }

    public List<Course> getCoursesWithState(int state) {

        String orderBy = COL_DATE + " DESC";
        if (state == Course.FORESEEN)
            orderBy = COL_DATE + " ASC";

        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, COL_STATE + " = " + state, null, null, null, orderBy);
        return cursorToListCourses(c);
    }

    public int getNbCoursesWithPupilID(int pupilID) {

        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, COL_PUPIL_ID + " = " + pupilID + " AND " + COL_STATE + " != " + Course.FORESEEN, null, null, null, null);
        return c.getCount();
    }

    public List<Course> getCoursesBetweenTwoDates(long start, long end) {
        Cursor c = bdd.query(TABLE_COURSES, COURSES_FIELDS, null, null, null, null, COL_DATE + " ASC");

        List<Course> list = new ArrayList<>();
        if (c.getCount() == 0)
            return list;

        c.moveToFirst();

        while (!c.isAfterLast()) {
            long date = c.getLong(NUM_COL_DATE);
            if (date > start && date < end) {
                Course course = new Course();
                course.setId(c.getInt(NUM_COL_ID));
                course.setDate(date);
                course.setDuration(c.getInt(NUM_COL_DURATION));
                course.setState(c.getInt(NUM_COL_STATE));
                course.setMoney(c.getDouble(NUM_COL_MONEY));
                course.setTheme(c.getString(NUM_COL_THEME));
                course.setMemo(c.getString(NUM_COL_MEMO));
                course.setPupilID(c.getInt(NUM_COL_PUPIL_ID));
                Cursor c0 = bdd.query(PupilsBDD.TABLE_PUPILS, PupilsBDD.PUPILS_FIELDS, COL_ID + " = " + course.getPupilID(), null, null, null, null);
                course.setPupil(PupilsBDD.cursorToPupil(c0));
                c0.close();
                list.add(course);
            }
            c.moveToNext();
        }

        c.close();

        return list;
    }

    public int getNumberWeeks() {
        List<Course> list = getAllCourses();
        Calendar calendar = Calendar.getInstance();
        int weekNumber = -1;
        int nbWeeks = 0;
        for (Course course : list) {
            if (course.getState() != Course.FORESEEN && course.getState() != Course.CANCELED) {
                calendar.setTimeInMillis(course.getDate());
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                if (weekOfYear != weekNumber) {
                    nbWeeks = nbWeeks + 1;
                    weekNumber = weekOfYear;
                }
            }
        }
        return nbWeeks;
    }

    public List<PeriodicItem> getAllWeeks() {
        List<PeriodicItem> weekList = new ArrayList<>();
        List<Course> list = getAllCourses();

        if (list != null) {
            Collections.reverse(list);
            Calendar calendar = Calendar.getInstance();
            int weekNumber = -1;
            int nbCourse = 0;
            double money = 0;

            for (Course course : list) {
                if (course.getState() != Course.FORESEEN && course.getState() != Course.CANCELED) {
                    calendar.setTimeInMillis(course.getDate());
                    int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                    if (weekNumber != weekOfYear) {
                        weekNumber = weekOfYear;
                        calendar.setTimeInMillis(course.getDate());
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        long beginning = calendar.getTimeInMillis();
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        calendar.set(Calendar.HOUR_OF_DAY, 23);
                        calendar.set(Calendar.MINUTE, 59);
                        calendar.set(Calendar.SECOND, 59);
                        long ending = calendar.getTimeInMillis();
                        List<Course> weekCourse = getCoursesBetweenTwoDates(beginning, ending);
                        if (weekCourse != null) {
                            for (Course course1 : weekCourse) {
                                if (course1.getState() != Course.FORESEEN) {
                                    money += course1.getMoney();
                                    nbCourse += 1;
                                }
                            }
                            Week week = new Week(nbCourse, money, beginning);
                            weekList.add(week);
                            nbCourse = 0;
                            money = 0;
                        }
                    }
                }
            }
        }

        return weekList;
    }

    public List<PeriodicItem> getAllMonths() {
        List<PeriodicItem> monthList = new ArrayList<>();
        List<Course> list = getAllCourses();

        if (list != null) {
            Collections.reverse(list);
            Calendar calendar = Calendar.getInstance();
            int monthNumber = -1;
            int nbCourse = 0;
            double money = 0;

            for (Course course : list) {
                if (course.getState() != Course.FORESEEN && course.getState() != Course.CANCELED) {
                    calendar.setTimeInMillis(course.getDate());
                    int monthOfYear = calendar.get(Calendar.MONTH);
                    if (monthNumber != monthOfYear) {
                        monthNumber = monthOfYear;
                        calendar.setTimeInMillis(course.getDate());
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        long beginning = calendar.getTimeInMillis();
                        calendar.add(Calendar.MONTH, 1);
                        calendar.add(Calendar.DAY_OF_YEAR, -1);
                        calendar.set(Calendar.HOUR_OF_DAY, 23);
                        calendar.set(Calendar.MINUTE, 59);
                        calendar.set(Calendar.SECOND, 59);
                        long ending = calendar.getTimeInMillis();
                        List<Course> monthCourse = getCoursesBetweenTwoDates(beginning, ending);
                        if (monthCourse != null) {
                            for (Course course1 : monthCourse) {
                                if (course1.getState() != Course.FORESEEN) {
                                    money += course1.getMoney();
                                    nbCourse += 1;
                                }
                            }
                            Month month = new Month(nbCourse, money, beginning);
                            monthList.add(month);
                            nbCourse = 0;
                            money = 0;
                        }
                    }
                }
            }
        }

        return monthList;
    }

    public List<PeriodicItem> getAllYears() {
        List<PeriodicItem> yearList = new ArrayList<>();
        List<Course> list = getAllCourses();

        if (list != null) {
            Collections.reverse(list);
            Calendar calendar = Calendar.getInstance();
            Course course0 = list.get(0);
            calendar.setTimeInMillis(course0.getDate());
            int year = calendar.get(Calendar.YEAR);
            String label = String.format("%d/%d", year, year + 1);
            int nbCourse = 1;
            double money = course0.getMoney();
            for (Course course : list) {
                if (course.getState() != Course.FORESEEN && course.getState() != Course.CANCELED) {
                    calendar.setTimeInMillis(course.getDate());
                    if (calendar.get(Calendar.MONTH) > Calendar.JULY && calendar.get(Calendar.YEAR) > year) {
                        yearList.add(new Year(label, money, nbCourse));
                        year = calendar.get(Calendar.YEAR);
                        label = String.format("%d/%d", year, year + 1);
                        money = course.getMoney();
                        nbCourse = 1;
                    } else {
                        money += course.getMoney();
                        nbCourse += 1;
                    }
                }
            }
            yearList.add(new Year(label, money, nbCourse));
        }

        return yearList;

    }

    public int getNumberMonths() {
        List<Course> list = getAllCourses();
        Calendar calendar = Calendar.getInstance();
        int monthNumber = -1;
        int nbMonths = 0;
        for (Course course : list) {
            if (course.getState() != Course.FORESEEN && course.getState() != Course.CANCELED) {
                calendar.setTimeInMillis(course.getDate());
                int monthOfYear = calendar.get(Calendar.MONTH) + 1;
                if (monthOfYear != monthNumber) {
                    nbMonths += 1;
                    monthNumber = monthOfYear;
                }
            }
        }
        return nbMonths;
    }

    public double getMoneyEarnedWithState(int state) {
        List<Course> list = getCoursesWithState(state);
        if (list == null)
            return 0;
        double money = 0;
        for (Course course : list)
            money += course.getMoney();

        return money;
    }

    public Double[] getMoneyOfBestWeek() {
        List<Course> list = getAllCourses();
        Double[] result = new Double[2];
        int indexWeek = -1;
        int courseWeek = 60;
        double bestWeek = 0;
        double bestMoney = 0, bestMoneyIndex = -1;
        Calendar calendar = Calendar.getInstance();
        for (Course course : list) {
            if (bestWeek > bestMoney) {
                bestMoney = bestWeek;
                bestMoneyIndex = calendar.getTimeInMillis();
            }
            calendar.setTimeInMillis(course.getDate());
            courseWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            // If it's an other week, we begin again
            if (courseWeek != indexWeek) {
                indexWeek = courseWeek;
                bestWeek = course.getMoney();
            }
            // Else, continue to count
            else {
                bestWeek += course.getMoney();
            }
        }

        result[0] = bestMoney;
        result[1] = bestMoneyIndex;
        return result;
    }

    public Double[] getMoneyOfBestMonth() {
        List<Course> list = getAllCourses();
        Double[] result = new Double[2];
        int indexMonth = -1;
        int courseMonth = 60;
        double bestMonth = 0;
        double bestMoney = 0, bestMoneyIndex = -1;
        Calendar calendar = Calendar.getInstance();
        for (Course course : list) {
            if (bestMonth > bestMoney) {
                bestMoney = bestMonth;
                bestMoneyIndex = calendar.getTimeInMillis();
            }

            calendar.setTimeInMillis(course.getDate());
            courseMonth = calendar.get(Calendar.MONTH);
            // If it's an other week, we begin again
            if (courseMonth != indexMonth) {
                indexMonth = courseMonth;
                bestMonth = course.getMoney();
            }
            // Else, continue to count
            else {
                bestMonth += course.getMoney();
            }
        }

        result[0] = bestMoney;
        result[1] = bestMoneyIndex;
        return result;
    }

    public static Course getNextCourse(Context context) {
        CoursesBDD coursesBDD = new CoursesBDD(context);
        coursesBDD.open();
        Course course = coursesBDD.getNextCourse();
        coursesBDD.close();

        return course;
    }

    public static Course getCourseWithId(Context context, int courseID){
        CoursesBDD coursesBDD = new CoursesBDD(context);
        coursesBDD.open();
        Course course = coursesBDD.getCourseWithId(courseID);
        coursesBDD.close();
        return course;
    }

    public static void changeCourseState(Context context, Course course, int newState){
        CoursesBDD coursesBDD = new CoursesBDD(context);
        coursesBDD.open();
        course.setState(newState);
        coursesBDD.updateCourse(course.getId(), course);
        coursesBDD.close();
    }

    public List<Course> getListCourseForADay(long timestampOfTheDay){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampOfTheDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long begin = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long end = calendar.getTimeInMillis();

        String criteria = " 1 AND " + COL_DATE + " > " + begin
                + " AND " + COL_DATE + " < " + end + " AND "
                + COL_STATE + " = " + Course.VALIDATED;
        return getCourseWithCriteria(criteria);
        //return getCoursesBetweenTwoDates(begin,end);
    }
}