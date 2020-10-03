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

public class ProblemListPage extends AppCompatActivity{

    CustomerRequestList list = new CustomerRequestList();
    EditUser1 editUser1 = new EditUser1();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problemlist);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, list).commit();
            setTitle("Customer Request List");
        }

    }

    public void onClickHome(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, list).addToBackStack(null).commit();
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