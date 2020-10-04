package com.example.gethelp;

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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewServiceDialog extends AppCompatDialogFragment {

    View view;
    private EditText newServiceId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.new_service_dialog, null);
        newServiceId = view.findViewById(R.id.newServiceId);

        builder.setView(view)
                .setTitle("New Service Type")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveService();
                    }
                });

        return builder.create();
    }

    public void saveService() {
        String title = newServiceId.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter a value for title", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference serviceRef = FirebaseFirestore.getInstance().collection("services");
        serviceRef.add(new ServiceItem(title));
        Toast.makeText(this.getContext(), "Service Added Successfully", Toast.LENGTH_SHORT).show();
    }
}
