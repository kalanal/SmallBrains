package com.example.gethelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ConsumerMainActivity extends AppCompatActivity{

    Home consumer_home = new Home();
    EditUser1 editUser1 = new EditUser1();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, consumer_home).commit();
            setTitle(R.string.consumer_home_title);
        }

    }

    public void onClickHome(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, consumer_home).addToBackStack(null).commit();
        setTitle(R.string.consumer_home_title);
    }
    public void onClickUser(View view){
        setTitle(R.string.user_profile_title);
        startActivity(new Intent(getApplicationContext(),EditUser1.class));
        finish();
    }
    public void onSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }


}