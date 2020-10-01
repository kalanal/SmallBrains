package com.example.gethelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "ABC";
    EditText Email,UserName,Age,Phone,Town,Password;
    Button SignUpbtn,Loginbtn;
    RadioButton Consumer,Professional;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Email = findViewById(R.id.emailTxt);
        UserName = findViewById(R.id.usrnmTxt);
        Age = findViewById(R.id.ageTxt);
        Phone = findViewById(R.id.phoneTxt);
        Town = findViewById(R.id.adrsTxt);
        Consumer = findViewById(R.id.consumer);
        Professional = findViewById(R.id.professional);
        Password = findViewById(R.id.pswrd);
        SignUpbtn = findViewById(R.id.signUp);
        Loginbtn = findViewById(R.id.login2);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),ConsumerMainActivity.class));
            finish();
        }

        SignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                final String username = UserName.getText().toString().trim();
                final String phone = Phone.getText().toString().trim();
                final String town = Town.getText().toString().trim();
                final String age = Age.getText().toString().trim();
                final String consumer = Consumer.getText().toString();
                final String professional = Professional.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Password.setError("Password is required");
                    return;
                }
                if(password.length() < 6 ){
                    Password.setError("Require more than 6 characters");
                    return;
                }
//                if(!Consumer.isChecked() || !Professional.isChecked()){
//                    Consumer.setError("Please select a category");
//                    return;
//                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(SignUp.this,"User Created." , Toast.LENGTH_SHORT).show();
                            userId = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userId);
                            Map<String,Object> user = new HashMap<>();
                            if(Consumer.isChecked()){
                                user.put("type",consumer);
                            }
                            if(Professional.isChecked()){
                                user.put("type",professional);
//                                AlertDialog.Builder enterProDetails = new AlertDialog.Builder(SignUp.this);
//                                enterProDetails.setTitle("Alert!");
//                                enterProDetails.setMessage("Do you wish to exit?");
//                                enterProDetails.setCancelable(true);

//                                enterProDetails.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        finish();
//                                    }
//                                });
//
//                                enterProDetails.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    }
//                                });
                            }
                            user.put("email", email);
                            user.put("uname" , username);
                            user.put("age" , age);
                            user.put("phone" , phone);
                            user.put("town" , town);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User profile is created for "+ userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+ e.toString());
                                }
                            });
                            if(Consumer.isChecked()) {
                                startActivity(new Intent(getApplicationContext(), ConsumerMainActivity.class));
                            }
                            else{
                                startActivity(new Intent(getApplicationContext(), ProfessionalDetails.class));
                            }
                        } else{
                            Toast.makeText(SignUp.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}