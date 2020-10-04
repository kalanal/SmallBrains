package com.example.gethelp.Consumer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gethelp.EditUser;
import com.example.gethelp.Login;
import com.example.gethelp.R;
import com.google.firebase.auth.FirebaseAuth;

public class ConsumerMainActivity extends AppCompatActivity{

    Home consumer_home = new Home();
    EditUser editUser = new EditUser();
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editUser).addToBackStack(null).commit();
        setTitle(R.string.user_profile_title);
    }
    public void onSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}