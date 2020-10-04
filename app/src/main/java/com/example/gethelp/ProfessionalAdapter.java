package com.example.gethelp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProfessionalAdapter extends FirestoreRecyclerAdapter<ProfessionalItem, ProfessionalAdapter.ProfessionalHolder> {

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
        public ProfessionalHolder(@NonNull View itemView) {
            super(itemView);
            professionalImage = itemView.findViewById(R.id.user_pro_image);
            professionalId = itemView.findViewById(R.id.professional_id);
            professionalName = itemView.findViewById(R.id.service_title);
            professionalCat = itemView.findViewById(R.id.service_category);
            professionalAbout = itemView.findViewById(R.id.service_description);
            professionalRating = itemView.findViewById(R.id.service_rating);
        }
    }
}
