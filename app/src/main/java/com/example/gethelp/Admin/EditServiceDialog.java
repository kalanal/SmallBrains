package com.example.gethelp.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gethelp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditServiceDialog extends AppCompatDialogFragment {

    private EditText editServiceId;
    private String id,title;
    View view;

    public EditServiceDialog(String id,String title) {
        this.id = id;
        this.title = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.edit_service_dialog, null);
        editServiceId = view.findViewById(R.id.editServiceId);
        editServiceId.setText(title);

        builder.setView(view)
                .setTitle("Edit Service Type")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editService();
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteService();
                    }
                });

        return builder.create();
    }

    public void editService() {
        String title = editServiceId.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter a value for title", Toast.LENGTH_SHORT).show();
            return;
        }
        DocumentReference serviceRef = FirebaseFirestore.getInstance().collection("services").document(id);
        serviceRef.update("title",title)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(), "Service Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "Service Failed to Update", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteService(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setMessage("Deleting this service will result in its professionals being moved to category `other`");
        builder.setTitle("Are you sure you want to delete this service?");

        builder.setCancelable(false);

        builder.setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                DocumentReference serviceRef = FirebaseFirestore.getInstance().collection("services").document(id);
                                serviceRef.delete();
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
}
