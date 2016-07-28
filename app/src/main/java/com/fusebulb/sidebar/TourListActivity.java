package com.fusebulb.sidebar;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v7.app.ActionBar;


import com.fusebulb.sidebar.Parser.CityTourParser;
/**
 * Created by amiteshmaheshwari on 15/07/16.
 */

public class TourListActivity extends AppCompatActivity {
    private SharedPreference userSettings;
    private String userName;
    private String userLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userSettings = new SharedPreference();
        userName = userSettings.getUserName(this);
        userLanguage = userSettings.getUserLanguage(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_city);

        Toolbar toolbar = (Toolbar) findViewById(R.id.city_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(TourListActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });

        // MENU START



//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View hView =  navigationView.getHeaderView(0);
//        TextView nav_user = (TextView)hView.findViewById(R.id.user_name);
//        nav_user.setText(userName);
//        navigationView.setNavigationItemSelectedListener(this);
        // MENU END

        RecyclerView cityRecyclerView;
        cityRecyclerView = (RecyclerView)findViewById(R.id.tour_list_view);
        cityRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        cityRecyclerView.setLayoutManager(mLayoutManager);

        Bundle extras = getIntent().getExtras();
        String tour_source;
        if (extras != null) {
            tour_source = extras.getString("TOURS_SOURCE");
            //The key argument here must match that used in the other activity
            MediaPlayer mediaPlayer = new MediaPlayer();
            CityTourParser cityTourParser = new CityTourParser(this, userLanguage, mediaPlayer, tour_source, cityRecyclerView);
            cityTourParser.execute();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home){
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getUserLanguagePref(){
        return userSettings.getUserLanguage(this);
    }


}
