package com.example.gethelp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class CustomerRequestList extends Fragment {

    View view;
    TextView name,email,type;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference problemRef = db.collection("customerProblem");
    private CollectionReference proRef = db.collection("users");
    private CollectionReference user = db.collection("users");

    private ProblemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_customer_request_list,container,false);
        name = view.findViewById(R.id.pro_name);
        type = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        setupPage();
    }


    private void setUpRecyclerView() {
        Query query = problemRef.limit(100);

        FirestoreRecyclerOptions<CustomerProblem> options = new FirestoreRecyclerOptions.Builder<CustomerProblem>()
                .setQuery(query, CustomerProblem.class)
                .build();

        adapter = new ProblemAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.problem_recycler);
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

    public void setupPage(){
        DocumentReference docRef = proRef.document("iCKRDskB5VS36j1ZOGTMiqylwaR2");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.getString("uname"));
                        type.setText(document.getString("type"));
                        email.setText(document.getString("email"));
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