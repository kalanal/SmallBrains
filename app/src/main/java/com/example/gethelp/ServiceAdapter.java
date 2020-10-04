package com.example.gethelp;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ServiceAdapter extends FirestoreRecyclerAdapter<ServiceItem, ServiceAdapter.ServiceHolder> {

    int selectedPos = 0;
    String id;

    public ServiceAdapter(@NonNull FirestoreRecyclerOptions<ServiceItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ServiceHolder serviceHolder, int i, @NonNull ServiceItem serviceItem) {
        serviceHolder.textViewTitle.setText(serviceItem.getTitle());
        serviceHolder.textViewTitle.setTextColor(selectedPos == i ? Color.WHITE : Color.GRAY);
        serviceHolder.textViewId.setText(getSnapshots().getSnapshot(i).getId());
        serviceHolder.itemView.setSelected(selectedPos == i);
        Drawable backgroundOff = serviceHolder.itemView.getBackground(); //v is a view
        backgroundOff.setTint(selectedPos == i ? Color.parseColor("#ab4ded") : Color.WHITE); //defaultColor is an int
        serviceHolder.itemView.setBackground(backgroundOff);
        id = getSnapshots().getSnapshot(selectedPos).getId();
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item,parent,false);
        return new ServiceHolder(v);
    }

    class ServiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewTitle;
        TextView textViewId;

        public ServiceHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.service_item_value);
            textViewId = itemView.findViewById(R.id.service_item_id);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if(getAdapterPosition() == RecyclerView.NO_POSITION) return;

            notifyItemChanged(selectedPos);
            selectedPos = getAdapterPosition();
            notifyItemChanged(selectedPos);
        }
    }

    public String getId() {
        return id;
    }
}
