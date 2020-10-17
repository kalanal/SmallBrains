package com.example.gethelp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.example.gethelp.Consumer.ConsumerMainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import org.w3c.dom.Document;

public class ProblemListPage extends AppCompatActivity{

    CustomerRequestList list = new CustomerRequestList();
    ProfessionalProfile editUser1 = new ProfessionalProfile();
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problemlist);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, list).commit();
            setTitle("Customer Request List");
        }
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        validateUser();
    }
    public void onClickHome(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, list).addToBackStack(null).commit();
        setTitle(R.string.consumer_home_title);
    }

    public void onClickUser(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editUser).addToBackStack(null).commit();
        setTitle(R.string.user_profile_title);
    }

    public void onSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    private void validateUser() {
        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("professionalPending").document(userId);
        
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                View view = null;
                if(value.exists()){
                    Toast.makeText(ProblemListPage.this,"Sorry, Your account is not approved" , Toast.LENGTH_SHORT).show();
                    onSignOut(view);
                }
                Toast.makeText(ProblemListPage.this,"Login successful" , Toast.LENGTH_SHORT).show();
            }
        });
    }
}