package com.example.gethelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    AdminHome adminHome = new AdminHome();
    ServiceTypes serviceTypes = new ServiceTypes();
    UserApprovals userApprovals = new UserApprovals();

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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, adminHome).commit();
            setTitle(R.string.admin_home_title);
            navigationView.setCheckedItem(R.id.nav_admin_home);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_admin_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, adminHome).addToBackStack(null).commit();
                setTitle(R.string.admin_home_title);
                break;
            case R.id.nav_service_types:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, serviceTypes).addToBackStack(null).commit();
                setTitle(R.string.service_types_title);
                break;
            case R.id.nav_user_approvals:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, userApprovals).addToBackStack(null).commit();
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

    public void onPressViewApprovals(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, userApprovals).addToBackStack(null).commit();
        setTitle(R.string.user_approvals_title);
    }

    public void onPressViewSingleApproval(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ApproveUser()).addToBackStack(null).commit();
        setTitle(R.string.approve_user_title);
    }

    public void onPressDropdown(View view){
        serviceTypes.onPressDropdown(view);
    }
}