package com.kiduyu.kevinproject.e_grocerystore.CustomerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kiduyu.kevinproject.e_grocerystore.Activities.CustomerHomeActivity;
import com.kiduyu.kevinproject.e_grocerystore.Activities.LoginActivity;
import com.kiduyu.kevinproject.e_grocerystore.R;

public class Customer_SignoutFragment extends Fragment {
    private Button signout , cancel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview= inflater.inflate(R.layout.signout,container,false);

        signout=myview.findViewById(R.id.signout);
        cancel=myview.findViewById(R.id.back);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext().getApplicationContext(), CustomerHomeActivity.class));

            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext().getApplicationContext(), LoginActivity.class));

            }
        });


        return myview;
    }
}
