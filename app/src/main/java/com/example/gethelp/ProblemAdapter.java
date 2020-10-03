package com.example.gethelp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProblemAdapter extends FirestoreRecyclerAdapter<CustomerProblem, ProblemAdapter.ServiceHolder> {

    public ProblemAdapter(@NonNull FirestoreRecyclerOptions<CustomerProblem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ServiceHolder serviceHolder, int i, @NonNull final CustomerProblem customerProblem) {
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile.jpg");
//        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(serviceHolder.professionalImage);
//            }
//        });
        serviceHolder.problemTitle.setText(customerProblem.getTitle());
        serviceHolder.problemDescription.setText(customerProblem.getDescription());
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item,parent,false);
        return new ServiceHolder(v);
    }

    class ServiceHolder extends RecyclerView.ViewHolder {

        TextView problemTitle;
        TextView problemDescription;
        Button respondButton;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            problemTitle = itemView.findViewById(R.id.problem_title);
            problemDescription = itemView.findViewById(R.id.problem_description);
            respondButton = itemView.findViewById(R.id.respond_button);
        }
    }
}
