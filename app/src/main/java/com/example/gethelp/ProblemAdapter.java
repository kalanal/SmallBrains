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

public class ProblemAdapter extends FirestoreRecyclerAdapter<CustomerProblem, ProblemAdapter.ProblemHolder> {

    public ProblemAdapter(@NonNull FirestoreRecyclerOptions<CustomerProblem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ProblemHolder problemHolder, int i, @NonNull final CustomerProblem customerProblem) {
        problemHolder.problemTitle.setText(customerProblem.getTitle());
        problemHolder.problemDescription.setText(customerProblem.getDescription());
    }

    @NonNull
    @Override
    public ProblemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_problem, parent, false);
        return new ProblemHolder(v);
    }

    class ProblemHolder extends RecyclerView.ViewHolder {

        TextView problemTitle;
        TextView problemDescription;
        Button respondButton;

        public ProblemHolder(@NonNull View itemView) {
            super(itemView);
            problemTitle = itemView.findViewById(R.id.problem_title);
            problemDescription = itemView.findViewById(R.id.problem_description);
            respondButton = itemView.findViewById(R.id.respond_button);
        }
    }
}
