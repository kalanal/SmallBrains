package com.example.gethelp.Consumer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gethelp.Admin.ServiceItem;
import com.example.gethelp.Login;
import com.example.gethelp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;

public class ProfessionalAdapter extends FirestoreRecyclerAdapter<ProfessionalItem, ProfessionalAdapter.ProfessionalHolder> {

    int selectedPos = 0;
    String id;
    View view;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public ProfessionalAdapter(@NonNull FirestoreRecyclerOptions<ProfessionalItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ProfessionalHolder serviceHolder, int i, @NonNull final ProfessionalItem professionalItem) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/"+ getSnapshots().getSnapshot(i).getId() +"profile.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(serviceHolder.professionalImage);
            }
        });
        serviceHolder.professionalId.setText(getSnapshots().getSnapshot(i).getId());
        serviceHolder.professionalName.setText(professionalItem.getName());
        serviceHolder.professionalCat.setText(professionalItem.getCategory());
        serviceHolder.professionalAbout.setText(professionalItem.getAbout());
        serviceHolder.professionalRating.setRating(professionalItem.getRating());
    }

    @NonNull
    @Override
    public ProfessionalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.professional_item,parent,false);
        return new ProfessionalHolder(v);
    }

    class ProfessionalHolder extends RecyclerView.ViewHolder {
        ImageView professionalImage;
        TextView professionalId;
        TextView professionalName;
        TextView professionalCat;
        TextView professionalAbout;
        RatingBar professionalRating;
        Button getHelpBtn;
        public ProfessionalHolder(@NonNull View itemView) {
            super(itemView);
            professionalImage = itemView.findViewById(R.id.user_pro_image);
            professionalId = itemView.findViewById(R.id.professional_id);
            professionalName = itemView.findViewById(R.id.service_title);
            professionalCat = itemView.findViewById(R.id.service_category);
            professionalAbout = itemView.findViewById(R.id.service_description);
            professionalRating = itemView.findViewById(R.id.service_rating);
            getHelpBtn = itemView.findViewById(R.id.getHelpBtn);
            getHelpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() == RecyclerView.NO_POSITION) return;
                    notifyItemChanged(selectedPos);
                    selectedPos = getAdapterPosition();
                    notifyItemChanged(selectedPos);

                    id = getSnapshots().getSnapshot(selectedPos).getId();
                    view = v;
                    getHelpDlg();
                }
            });
        }
    }

    public void getHelpDlg(){
        fAuth = FirebaseAuth.getInstance();
        Log.d("id","User id : " + id);
        final EditText explainedProb = new EditText(view.getContext());
        final AlertDialog.Builder getHelpDialog = new AlertDialog.Builder(view.getContext());
        getHelpDialog.setTitle("Get Help");
        getHelpDialog.setMessage("In order to request for help, please explain your problem briefly in this dialog box and send");
        getHelpDialog.setView(explainedProb);
        fStore = FirebaseFirestore.getInstance();

        getHelpDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CollectionReference serviceRef = FirebaseFirestore.getInstance().collection("customerProblem");
                serviceRef.add(new ServiceItem(explainedProb.getText().toString()));
//                serviceRef.add(new ServiceItem(id));
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
