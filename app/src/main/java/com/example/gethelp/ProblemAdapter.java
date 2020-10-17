package com.example.gethelp;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gethelp.Admin.ServiceItem;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProblemAdapter extends FirestoreRecyclerAdapter<CustomerProblem, ProblemAdapter.ProblemHolder> {

    int selectedPos = 0;
    String id;
    View view;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public ProblemAdapter(@NonNull FirestoreRecyclerOptions<CustomerProblem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ProblemHolder problemHolder, int i, @NonNull final CustomerProblem customerProblem) {
        problemHolder.problemTitle.setText(customerProblem.getTitle());
//        problemHolder.problemDescription.setText(customerProblem.getDescription());
    }

    @NonNull
    @Override
    public ProblemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_problem, parent, false);
        return new ProblemHolder(v);
    }

    class ProblemHolder extends RecyclerView.ViewHolder {

        TextView problemTitle;
//        TextView problemDescription;
        Button respondButton;

        public ProblemHolder(@NonNull View itemView) {
            super(itemView);
            problemTitle = itemView.findViewById(R.id.problem_title);
//            problemDescription = itemView.findViewById(R.id.problem_description);
            respondButton = itemView.findViewById(R.id.respond_button);
            respondButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() == RecyclerView.NO_POSITION) return;
                    notifyItemChanged(selectedPos);
                    selectedPos = getAdapterPosition();
                    notifyItemChanged(selectedPos);

                    id = getSnapshots().getSnapshot(selectedPos).getId();
                    view = v;
                    submitSolutionDlg();
                }
            });
        }
    }

    public void submitSolutionDlg(){
        fAuth = FirebaseAuth.getInstance();
        Log.d("id","User id : " + id);
        final EditText problemSolution = new EditText(view.getContext());
        final AlertDialog.Builder getHelpDialog = new AlertDialog.Builder(view.getContext());
        getHelpDialog.setTitle("Get Help");
        getHelpDialog.setMessage("Submit the solution for the relevant question posted by the customer");
        getHelpDialog.setView(problemSolution);
        fStore = FirebaseFirestore.getInstance();

        getHelpDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CollectionReference serviceRef = FirebaseFirestore.getInstance().collection("problemSolution");
                serviceRef.add(new ServiceItem(problemSolution.getText().toString()));

                dialogInterface.cancel();
            }
        });

        getHelpDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        getHelpDialog.create().show();
    }


}
