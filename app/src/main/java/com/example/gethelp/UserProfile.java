package com.example.gethelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gethelp.Admin.MainActivity;
import com.example.gethelp.Consumer.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    EditText userEmail,userName,userAge,userPhone,userTown;
    Button saveBtn, deleteProBtn;
    ImageView profileImage;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent data = getIntent();
        String email = data.getStringExtra("email");
        final String name = data.getStringExtra("uname");
        final String age = data.getStringExtra("age");
        final String phone = data.getStringExtra("phone");
        final String town = data.getStringExtra("town");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        user = fAuth.getCurrentUser();

        userEmail = findViewById(R.id.editEmail);
        userName = findViewById(R.id.editName);
        userAge = findViewById(R.id.editAge);
        userPhone = findViewById(R.id.editPhone);
        userTown = findViewById(R.id.editTown);
        saveBtn = findViewById(R.id.saveBtnU);
        profileImage = findViewById(R.id.profImageU);
        deleteProBtn = findViewById(R.id.deleteProBtn);

        userEmail.setText(email);
        userName.setText(name);
        userAge.setText(age);
        userPhone.setText(phone);
        userTown.setText(town);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userEmail.getText().toString().isEmpty() || userName.getText().toString().isEmpty() || userAge.getText().toString().isEmpty() || userPhone.getText().toString().isEmpty() || userTown.getText().toString().isEmpty()){
                    Toast.makeText(UserProfile.this,"You cannot have empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String userId = user.getUid();
                final String email = userEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference = fStore.collection("users").document(userId);
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email",email);
                        edited.put("uname", userName.getText().toString());
                        edited.put("age",userAge.getText().toString());
                        edited.put("phone",userPhone.getText().toString());
                        edited.put("town",userTown.getText().toString());
                        Toast.makeText(UserProfile.this,"Profile is updated", Toast.LENGTH_SHORT).show();
                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserProfile.this, "Details Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        deleteProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfile.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting the profile will result in completely removing your account and all of its content. This cannot be undone");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        progressBar.setVisibility(View.VISIBLE);
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
//                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(UserProfile.this,"Account Deleted Succesfully",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UserProfile.this,Login.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(UserProfile.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }
}