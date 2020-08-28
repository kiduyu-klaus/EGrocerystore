package com.kiduyu.kevinproject.e_grocerystore.CustomerFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.kevinproject.e_grocerystore.Activities.MapsActivity;
import com.kiduyu.kevinproject.e_grocerystore.model.Data;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.squareup.picasso.Picasso;

public class Customer_StallsFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private ProgressDialog loadingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_stalls, container, false);

        recyclerView = view.findViewById(R.id.customer_stalls_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("stalls");
        mDatabase.keepSynced(true);
        loadingBar = new ProgressDialog(getActivity());

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


                final Query query= mDatabase.orderByChild("location")
                        .equalTo(Prevalent.currentOnlineUser.getAddress());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            holder.name.setText(model.getName());
                            holder.location.setText("Location: " + model.getLocation());
                            holder.description.setText(model.getDescription());
                            String ImageView = model.getImage().toString();
                            Picasso.get().load(ImageView).into(holder.imageview);

                            holder.button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    Bundle arguments = new Bundle();
                                    arguments.putString("stallname", model.getName());
                                    arguments.putString("stalllocation", model.getLocation());
                                    arguments.putString("stalldescription", model.getDescription());
                                    arguments.putString("stallimage", model.getImage());
                                    Customer_ProductsFragment customer_productsFragment = new Customer_ProductsFragment();
                                    customer_productsFragment.setArguments(arguments);
                                    fm.beginTransaction().replace(R.id.fragment_container, customer_productsFragment).commit();


                                }
                            });


                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                                    intent.putExtra("location", model.getLocation());
                                    intent.putExtra("owner", model.getOwner());
                                    startActivity(intent);

                                }
                            });

                        }else{
                            holder.itemView.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "No Stalls in this Location", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @NonNull
            @Override
            public StallsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stalls_customer, parent, false);
                return new StallsViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class StallsViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, description;
        Button button;
        ImageView imageview;

        public StallsViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.edt_stall_viewfetch_stallname);
            location = itemView.findViewById(R.id.edt_stall_viewfetch_stalllocation);
            description = itemView.findViewById(R.id.edt_stall_viewfetch_stalldescription);
            button = itemView.findViewById(R.id.edt_stall_view_products);
            imageview = itemView.findViewById(R.id.edt_stall_viewfetch_stallimage);
        }
    }
}