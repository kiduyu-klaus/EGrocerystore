package com.kiduyu.kevinproject.e_grocerystore.VendorFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.squareup.picasso.Picasso;

public class Stall_DetailsFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private Button stall_map_location;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vendor_view_products,container,false);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("stalls").child(Prevalent.currentOnlineVendor.getPhone());
        mDatabase.keepSynced(true);

        recyclerView=view.findViewById(R.id.recycler_product_stalls_detail);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        stall_map_location=view.findViewById(R.id.stall_map_location);

        stall_map_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data> option = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mDatabase,Data.class)
                .build();


        FirebaseRecyclerAdapter<Data,StallsViewHolder> adapter= new FirebaseRecyclerAdapter<Data, StallsViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final StallsViewHolder holder, int position, @NonNull Data model) {
                holder.name.setText("Stall Name: "+model.getName());
                holder.location.setText("Stall Location: "+model.getLocation());
                holder.description.setText(model.getDescription());
                String ImageView= model.getImage().toString();
                Picasso.get().load(ImageView).into(holder.imageview);

            }

            @NonNull
            @Override
            public StallsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stall_detail, parent, false);
                return new StallsViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class StallsViewHolder extends RecyclerView.ViewHolder{
        TextView name,location,description;
        ImageView imageview;
        Button edit_product,view_product;

        public StallsViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.fetch_stallname);
            location=itemView.findViewById(R.id.fetch_stalllocation);
            description=itemView.findViewById(R.id.fetch_stalldescription);
            imageview=itemView.findViewById(R.id.fetch_stallimage);
        }
    }
}

