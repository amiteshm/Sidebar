package com.fusebulb.sidebar;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;

import com.fusebulb.sidebar.Parser.CityParser;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreference userSettings;
    private String userName;
    private String userLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSettings = new SharedPreference();

        userName = userSettings.getUserName(this);
        userLanguage = userSettings.getUserLanguage(this);
        if(userName == null || userName.equals("")){
            Intent userSettingIntent= new Intent(this, UserSettings.class);
            startActivity(userSettingIntent);
        }

        setContentView(R.layout.activity_main);

        // MENU START
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.user_name);
        nav_user.setText(userName);
        navigationView.setNavigationItemSelectedListener(this);

        //MENU END

        RecyclerView cityRecyclerView;
        cityRecyclerView = (RecyclerView)findViewById(R.id.city_list_view);
        cityRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        cityRecyclerView.setLayoutManager(mLayoutManager);
        CityParser cityParser = new CityParser(this, userLanguage, cityRecyclerView);
        cityParser.execute();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_name || id == R.id.nav_language) {
            Intent userSettingIntent= new Intent(this, UserSettings.class);
            startActivity(userSettingIntent);
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            Resources rs = getResources();
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, rs.getString(R.string.share_app_message));
            startActivity(shareIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getUserLanguagePref(){
        return userSettings.getUserLanguage(this);
    }

}
