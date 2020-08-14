package com.example.gethelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    float values[] = {6f,1f,3f};
    String titles[] = {"Technician","Carpenter","Plumber"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminHome()).commit();
            setTitle(R.string.admin_home_title);
            navigationView.setCheckedItem(R.id.nav_admin_home);
//            setupPieChart();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_admin_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminHome()).commit();
                setTitle(R.string.admin_home_title);
//                setupPieChart();
                break;
            case R.id.nav_service_types:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ServiceTypes()).commit();
                setTitle(R.string.service_types_title);
                break;
            case R.id.nav_user_approvals:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserApprovals()).commit();
                setTitle(R.string.user_approvals_title);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupPieChart() {
        //Populating a list of pie entries
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i=0; i< values.length; i++){
            pieEntries.add(new PieEntry(values[i],titles[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"Service Performance");
        PieData data = new PieData(dataSet);

        // Get the chart
        PieChart chart = (PieChart)findViewById(R.id.service_chart);
        chart.setData(data);
    }

}