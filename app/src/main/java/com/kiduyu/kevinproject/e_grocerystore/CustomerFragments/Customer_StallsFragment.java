package com.kiduyu.kevinproject.e_grocerystore.CustomerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kiduyu.kevinproject.e_grocerystore.Activities.MapsActivity;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.kiduyu.kevinproject.e_grocerystore.model.Data;

public class Customer_StallsFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_stalls, container, false);

        recyclerView = view.findViewById(R.id.customer_stalls_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("stalls");
        mDatabase.keepSynced(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> option = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mDatabase, Data.class)
                .build();


        FirebaseRecyclerAdapter<Data, StallsViewHolder> adapter = new FirebaseRecyclerAdapter<Data, StallsViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final StallsViewHolder holder, int position, @NonNull final Data model) {



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), MapsActivity.class));
                    }
                });




            }

            @NonNull
            @Override
            public StallsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stalls, parent, false);
                return new StallsViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class StallsViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, description;

        public StallsViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.fetch_stallname);
            location = itemView.findViewById(R.id.fetch_stalllocation);
            description = itemView.findViewById(R.id.fetch_stalldescription);
        }
    }
}