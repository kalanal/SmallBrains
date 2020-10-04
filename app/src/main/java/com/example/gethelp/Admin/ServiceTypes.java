package com.example.gethelp.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gethelp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ServiceTypes extends Fragment implements View.OnClickListener{

    View view;
    FloatingActionButton newServiceBtn,editServiceBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference serviceRef = db.collection("services");
    private ServiceAdapter adapter;
    String title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.service_types,container,false);
        newServiceBtn = view.findViewById(R.id.newServiceBtn);
        editServiceBtn = view.findViewById(R.id.editServiceBtn);
        newServiceBtn.setOnClickListener(this);
        editServiceBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = serviceRef.limit(500);

        FirestoreRecyclerOptions<ServiceItem> options = new FirestoreRecyclerOptions.Builder<ServiceItem>()
                .setQuery(query,ServiceItem.class)
                .build();

        adapter = new ServiceAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.service_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onClick(View view) {
        DialogFragment dialog;
        switch (view.getId()){
            case R.id.newServiceBtn:
                dialog = new NewServiceDialog();
                dialog.show(getChildFragmentManager(), "NewServiceDialog");
                break;
            case R.id.editServiceBtn:
                getTitle();
                break;
        }
    }

    public void getTitle(){
        DocumentReference docRef = db.collection("services").document(adapter.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        title = document.getString("title");
                        DialogFragment dialog = new EditServiceDialog(adapter.getId(),title);
                        dialog.show(getChildFragmentManager(), "EditServiceDialog");
                    } else {
                        Log.d("title", "No such document");
                    }
                } else {
                    Log.d("title", "get failed with ", task.getException());
                }
            }
        });
    }
}
