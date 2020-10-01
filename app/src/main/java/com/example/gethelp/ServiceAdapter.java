package com.example.gethelp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ServiceAdapter extends FirestoreRecyclerAdapter<ServiceItem, ServiceAdapter.ServiceHolder> {

    public ServiceAdapter(@NonNull FirestoreRecyclerOptions<ServiceItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ServiceHolder serviceHolder, int i, @NonNull final ServiceItem serviceItem) {
        serviceHolder.professionalName.setText(serviceItem.getName());
        serviceHolder.professionalCat.setText(serviceItem.getCategory());
        serviceHolder.professionalAbout.setText(serviceItem.getAbout());
        serviceHolder.professionalRating.setRating(serviceItem.getRating());
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item,parent,false);
        return new ServiceHolder(v);
    }

    class ServiceHolder extends RecyclerView.ViewHolder {
        ImageView professionalImage;
        TextView professionalName;
        TextView professionalCat;
        TextView professionalAbout;
        RatingBar professionalRating;
        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            professionalImage = itemView.findViewById(R.id.user_pro_image);
            professionalName = itemView.findViewById(R.id.service_title);
            professionalCat = itemView.findViewById(R.id.service_category);
            professionalAbout = itemView.findViewById(R.id.service_description);
            professionalRating = itemView.findViewById(R.id.service_rating);
        }
    }
}
