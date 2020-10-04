package com.example.gethelp.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gethelp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ApproveUser extends Fragment implements View.OnClickListener{

    View view;
    ImageView userPic;
    TextView userName,userCat,userDesc;
    Button approveBtn, rejectBtn;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.approve_user,container,false);
        id = getArguments().getString("id");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPage();
    }

    public void setupPage(){

        userPic = view.findViewById(R.id.userPic);
        userName = view.findViewById(R.id.userName);
        userCat = view.findViewById(R.id.userCat);
        userDesc = view.findViewById(R.id.userDescription);
        approveBtn = view.findViewById(R.id.approveBtn);
        rejectBtn = view.findViewById(R.id.rejectBtn);
        approveBtn.setOnClickListener(this);
        rejectBtn.setOnClickListener(this);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("professionals").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/"+ id +"profile.jpg");
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(userPic);
                            }
                        });
                        userName.setText(document.getString("name"));
                        userCat.setText(document.getString("category"));
                        userDesc.setText(document.getString("about"));
                    }
                }
            }
        });

    }

    public void onClickApprove(){
//        DocumentReference newDocRef = FirebaseFirestore.getInstance().collection("professionals").document(id);
        DocumentReference newDocRef = FirebaseFirestore.getInstance().collection("professionals").document(id);
        Map<String, Object> professional = new HashMap<>();
        professional.put("name", userName.getText());
        professional.put("category",userCat.getText());
        professional.put("about",userDesc.getText());
//        newDocRef.update("about",userDesc.getText(),"category",userCat.getText(),"name",userName.getText())
        newDocRef.set(professional).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(), "User has been approved successfully", Toast.LENGTH_SHORT).show();
                        DocumentReference oldDocRef = FirebaseFirestore.getInstance().collection("professionalPending").document(id);
                        oldDocRef.delete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "Failed to Approve User", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    private void onClickReject() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setMessage("Rejecting a user will result in their details being deleted, Do you want to continue?");
        builder.setTitle("Are you sure you want to reject this user?");

        builder.setCancelable(false);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface
                        .OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("professionalPending").document(id);
                        docRef.delete();
                    }
                });

        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.approveBtn:
                onClickApprove();
                break;
            case R.id.rejectBtn:
                onClickReject();
                break;
        }
    }
}
