package com.example.gethelp.Admin;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gethelp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserApprovalAdapter extends FirestoreRecyclerAdapter<UserApprovalItem, UserApprovalAdapter.UserApprovalHolder> {

    int selectedPos = 0;
    String id = "";
    View v;

    public UserApprovalAdapter(@NonNull FirestoreRecyclerOptions<UserApprovalItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final UserApprovalHolder userApprovalHolder, int i, @NonNull UserApprovalItem userApprovalItem) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/"+ getSnapshots().getSnapshot(i).getId() +"profile.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userApprovalHolder.imageViewPic);
            }
        });
        userApprovalHolder.textViewName.setText(userApprovalItem.getName());
        userApprovalHolder.textViewCat.setText(userApprovalItem.getCategory());
    }

    @NonNull
    @Override
    public UserApprovalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_approval_item,parent,false);
        return new UserApprovalAdapter.UserApprovalHolder(v);
    }

    class UserApprovalHolder extends RecyclerView.ViewHolder{
        ImageView imageViewPic;
        TextView textViewName;
        TextView textViewCat;
        Button buttonView;

        public UserApprovalHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPic = itemView.findViewById(R.id.userPic);
            textViewName = itemView.findViewById(R.id.userName);
            textViewCat = itemView.findViewById(R.id.userCat);
            buttonView = itemView.findViewById(R.id.viewApproval);
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getAdapterPosition() == RecyclerView.NO_POSITION) return;
                    notifyItemChanged(selectedPos);
                    selectedPos = getAdapterPosition();
                    notifyItemChanged(selectedPos);
                    id = getSnapshots().getSnapshot(selectedPos).getId();
                    v = view;
                    loadUserApproval();
                }
            });
        }
    }

    public void loadUserApproval(){
        MainActivity activity = (MainActivity) v.getContext();
        ApproveUser newFragment = new ApproveUser();
        Bundle args = new Bundle();
        args.putString("id", id);
        newFragment.setArguments(args);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,newFragment).addToBackStack(null).commit();
    }
}
