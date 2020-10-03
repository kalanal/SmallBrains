package com.example.gethelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class ProfessionalDetails extends AppCompatActivity {

    EditText Name,Category,ProEmail,About;
    Button Save,Back;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_details);

        Name = findViewById(R.id.nameTxt);
        Category = findViewById(R.id.categoryTxt);
//        ProEmail = findViewById(R.id.pemailTxt);
        About = findViewById(R.id.aboutTxt);
        Save = findViewById(R.id.saveBtn);
        Back = findViewById(R.id.backBtn);
        progressBar = findViewById(R.id.proProgressBar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Name.getText().toString().trim();
                String category = Category.getText().toString();
                String about = About.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    Name.setError("Please fill out this field");
                    return;
                }
                if(TextUtils.isEmpty(category)){
                    Category.setError("Please fill out this field");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                userId = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("professionalPending").document(userId);
                Map<String, Object> professional = new HashMap<>();
                professional.put("name", name);
                professional.put("category",category);
                professional.put("about",about);
                documentReference.set(professional).addOnSuccessListener(new OnSuccessListener<Void>() {
                    private static final String TAG = "";

                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: User profile is created for "+ userId);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    private static final String TAG = "";

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+ e.toString());
                    }
                });
                startActivity(new Intent(getApplicationContext(), ConsumerMainActivity.class));
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }
}