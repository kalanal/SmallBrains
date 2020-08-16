package com.example.gethelp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class ServiceTypes extends Fragment implements View.OnClickListener{

    View v;
    ImageView newServiceBtn,editServiceBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.service_types,container,false);
        newServiceBtn = (ImageView) v.findViewById(R.id.newServiceBtn);
        editServiceBtn = (ImageView) v.findViewById(R.id.editServiceBtn);
        newServiceBtn.setOnClickListener(this);
        editServiceBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onPressDropdown(View view){

    }

    @Override
    public void onClick(View view) {
        DialogFragment dialog;
        switch (view.getId()){
            case R.id.newServiceBtn:
                dialog = new NewServiceDialog();
                dialog.show(getFragmentManager(), "NewServiceDialog");
                break;
            case R.id.editServiceBtn:
                dialog = new EditServiceDialog();
                dialog.show(getFragmentManager(), "EditServiceDialog");
                break;
        }

    }
}
