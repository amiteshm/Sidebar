package com.fusebulb.sidebar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amiteshmaheshwari on 13/07/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    // Database name
    private static final String DATABASE_NAME = "fuseblub";


    // TABLE NAME
    private static final String TABLE_CITIES = "cities";
    private static final String TABLE_TOURS = "tours";
    private static final String TABLE_CLIPS = "clips";


    //CITY TABLE COULMNS
    private static final String KEY_CITY_ID = "city_id";
    private static final String KEY_CITY_NAME = "name";
    private static final String KEY_CITY_LATITUDE = "latitude";
    private static final String KEY_CITY_LONGITUDE= "longitude";
    private static final String KEY_CITY_PICTURE = "picture";

    //TOUR TABLE COULMNS
    private static final String KEY_TOUR_ID = "tour_id";
    private static final String KEY_TOUR_NAME = "name";
    private static final String KEY_TOUR_LATITUDE = "latitude";
    private static final String KEY_TOUR_LONGITUDE= "longitude";
    private static final String KEY_TOUR_LOCATION_TYPE = "location_type";
    private static final String KEY_TOUR_PICTURE = "picture";
    private static final String KEY_TOUR_SUMMARY = "summary";
    private static final String KEY_TOUR_CITY = "city";

    //CLIP TABLE COLUMNS
    private static final String KEY_CLIP_ID = "clip_id";
    private static final String KEY_CLIP_NAME = "name";
    private static final String KEY_CLIP_TOUR = "tour";
    private static final String KEY_CLIP_SOURCE = "source";
    private static final String KEY_CLIP_ORDER = "clip_order";


    //TABLE CITY CREATE STATEMENT
    private static final String CREATE_TABLE_CITIES = "CREATE TABLE "
            + TABLE_CITIES + "("  + KEY_CITY_ID + " INTEGER PRIMARY KEY, "
            + KEY_CITY_NAME + " STRING, "
            + KEY_CITY_LATITUDE + " REAL, "
            + KEY_CITY_LONGITUDE + " REAL,"
            + KEY_CITY_PICTURE + " STRING"
            + ")";

    //TABLE CITY CREATE STATEMENT
    private static final String CREATE_TABLE_TOURS = "CREATE TABLE "
            + TABLE_TOURS + "(" + KEY_TOUR_ID + " INTEGER PRIMARY KEY, "
            + KEY_TOUR_CITY + " INTEGER, "
            + KEY_TOUR_NAME + " STRING,"
            + KEY_TOUR_LATITUDE + " REAL,"
            + KEY_TOUR_LONGITUDE + " REAL,"
            + KEY_TOUR_LOCATION_TYPE + " INTEGER, "
            + KEY_TOUR_SUMMARY + " TEXT, "
            + KEY_TOUR_PICTURE + " STRING"
            + ")";

    private static final String CREATE_TABLE_CLIPS = "CREATE TABLE "
            + TABLE_CLIPS + "(" + KEY_CLIP_ID + " INTEGER PRIMARY KEY, "
            + KEY_CLIP_TOUR + " INTEGER, "
            + KEY_CLIP_NAME + " STRING, "
            + KEY_CLIP_ORDER + " INTEGER, "
            + KEY_CLIP_SOURCE + " STRING"
            + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CITIES);
        db.execSQL(CREATE_TABLE_TOURS);
        db.execSQL(CREATE_TABLE_CLIPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIPS);
        // create new tables
        onCreate(db);
    }

    public long createClip(Clip clip){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLIPS, KEY_CLIP_ID+ " = ?", new String[] {String.valueOf(clip.getId())});
        ContentValues values = new ContentValues();
        values.put(KEY_CLIP_ID, clip.getId());
        values.put(KEY_CLIP_NAME, clip.getName());
        values.put(KEY_CLIP_TOUR, clip.getTourId());
        values.put(KEY_CLIP_ORDER, clip.getOrder());
        values.put(KEY_CLIP_SOURCE, clip.getSource());

        long clip_id = db.insert(TABLE_CLIPS, null, values);

        return clip_id;

    }

    public long createTour(Tour tour){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOURS, KEY_TOUR_ID + " = ?", new String[] {String.valueOf(tour.getId())});
        ContentValues values = new ContentValues();
        values.put(KEY_TOUR_ID, tour.getId());
        values.put(KEY_TOUR_NAME, tour.getName());
        values.put(KEY_TOUR_LATITUDE, tour.getLocation().getLatitude());
        values.put(KEY_TOUR_LONGITUDE, tour.getLocation().getLongitude());
        values.put(KEY_TOUR_LOCATION_TYPE, tour.getTourType());
        values.put(KEY_TOUR_SUMMARY, tour.getSummary());
        values.put(KEY_TOUR_CITY, tour.getCity());
        //values.put(KEY_TOUR_PICTURE , tour.getPicture());

        long tour_id = db.insert(TABLE_TOURS, null, values);
        //}

        return tour_id;

    }

    public long createCity(City city){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CITIES, KEY_CITY_ID + " = ? ", new String[] {String.valueOf(city.getId())});
        ContentValues values = new ContentValues();
        values.put(KEY_CITY_ID, city.getId());
        values.put(KEY_CITY_NAME, city.getName());
        values.put(KEY_CITY_LATITUDE, city.getLocation().getLatitude());
        values.put(KEY_CITY_LONGITUDE, city.getLocation().getLongitude());
        //values.put(KEY_CITY_PICTURE , city.getPicture());
        long city_id = db.insert(TABLE_CITIES, null, values);
        return city_id;
    }

    public Clip getClip(int clip_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CLIPS+ " WHERE "
                + KEY_CLIP_ID + " = " + clip_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        Clip clip= new Clip();
        clip.setId(c.getInt(c.getColumnIndex(KEY_CLIP_ID)));
        clip.setName(c.getString(c.getColumnIndex(KEY_CLIP_NAME)));
        clip.setSource(c.getString(c.getColumnIndex(KEY_CLIP_SOURCE)));
        clip.setTourId(c.getInt(c.getColumnIndex(KEY_CLIP_TOUR)));
        clip.setOrder(c.getString(c.getColumnIndex(KEY_CLIP_ORDER)));
        return clip;

    }

    public Tour getTour(int tour_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TOURS + " WHERE "
                + KEY_TOUR_ID + " = " + tour_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        Tour tour= new Tour();
        tour.setId(c.getInt(c.getColumnIndex(KEY_TOUR_ID)));
        tour.setName(c.getString(c.getColumnIndex(KEY_TOUR_NAME)));
        tour.setLocation(c.getDouble(c.getColumnIndex(KEY_TOUR_LATITUDE)), c.getDouble(c.getColumnIndex(KEY_TOUR_LONGITUDE)));
        //tour.setPicture(c.getString(c.getColumnIndex(KEY_TOUR_PICTURE)));
        tour.setSummary(c.getString(c.getColumnIndex(KEY_TOUR_SUMMARY)));
        tour.setCity(c.getInt(c.getColumnIndex(KEY_TOUR_CITY)));
        return tour;

    }

    public City getCity(int city_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CITIES + " WHERE "
                + KEY_CITY_ID + " = " + city_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        City city = new City();
        city.setId(c.getInt(c.getColumnIndex(KEY_CITY_ID)));
        city.setName(c.getString(c.getColumnIndex(KEY_CITY_NAME)));
        city.setLocation(c.getDouble(c.getColumnIndex(KEY_CITY_LATITUDE)), c.getDouble(c.getColumnIndex(KEY_CITY_LONGITUDE)));
       // city.setPicture(c.getString(c.getColumnIndex(KEY_CITY_PICTURE)));

        return city;
    }


    public List<Tour> getAllCityTours(int city_id){
        List<Tour> tours = new ArrayList<Tour>();
        String selectQuery = "SELECT * FROM " + TABLE_TOURS + " WHERE CITY";

        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if(c.moveToFirst()){
            do {
                Tour tour  = new Tour();
                tour.setId(c.getInt(c.getColumnIndex(KEY_TOUR_ID)));
                tour.setName(c.getString(c.getColumnIndex(KEY_TOUR_NAME)));
                tour.setLocation(c.getDouble(c.getColumnIndex(KEY_TOUR_LATITUDE)), c.getDouble(c.getColumnIndex(KEY_TOUR_LONGITUDE)));
                //tour.setPicture(c.getString(c.getColumnIndex(KEY_TOUR_PICTURE)));
                tour.setCity(c.getInt(c.getColumnIndex(KEY_TOUR_CITY)));
                tour.setSummary(c.getString(c.getColumnIndex(KEY_TOUR_SUMMARY)));
                tours.add(tour);
            } while (c.moveToNext());

        }
        return tours;
    }

    public List<City> getAllCities() {
        List<City> cities = new ArrayList<City>();
        String selectQuery = "SELECT * FROM " + TABLE_CITIES;

        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if(c.moveToFirst()){
            do {
                City city  = new City();
                city.setId(c.getInt(c.getColumnIndex(KEY_CITY_ID)));
                city.setName(c.getString(c.getColumnIndex(KEY_CITY_NAME)));
                city.setLocation(c.getDouble(c.getColumnIndex(KEY_CITY_LATITUDE)), c.getDouble(c.getColumnIndex(KEY_CITY_LONGITUDE)));
               // city.setPicture(c.getString(c.getColumnIndex(KEY_CITY_PICTURE)));
                cities.add(city);
            } while (c.moveToNext());

        }

        return cities;
    }

    public void deleteCity(int city_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CITIES, KEY_CITY_ID + " = ? ", new String[] {String.valueOf(city_id)});
    }

}
