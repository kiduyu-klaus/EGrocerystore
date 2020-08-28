package com.kiduyu.kevinproject.e_grocerystore.VendorFragments;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.kevinproject.e_grocerystore.model.Data;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.squareup.picasso.Picasso;

public class Vendor_StallsFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    TextView my_stals_titles;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vendor_stalls_add,container,false);

        FloatingActionButton fab_add= view.findViewById(R.id.fab_add);
        my_stals_titles=view.findViewById(R.id.my_stals_titles);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("stalls");
        mDatabase.keepSynced(true);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Add_StallsFragment()).commit();
            }
        });
        recyclerView=view.findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data> option = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mDatabase,Data.class)
                .build();


        FirebaseRecyclerAdapter<Data,StallsViewHolder> adapter= new FirebaseRecyclerAdapter<Data, StallsViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final StallsViewHolder holder, int position, @NonNull final Data model) {

                mDatabase.child(Prevalent.currentOnlineVendor.getPhone()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("name")){
                            String product_name = dataSnapshot.child("name").getValue().toString();
                            String product_description = dataSnapshot.child("description").getValue().toString();
                            String product_location = dataSnapshot.child("location").getValue().toString();
                            String product_image = dataSnapshot.child("image").getValue().toString();


                            holder.name.setText("Stall Name: "+product_name);
                            holder.location.setText("Stall Location: "+product_location);
                            holder.description.setText(product_description);
                            String ImageView= product_image.toString();
                            Picasso.get().load(ImageView).into(holder.imageview);

                            holder.edit_product.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getActivity(), "Edit is Clicked", Toast.LENGTH_SHORT).show();
                                }
                            });

                            holder.view_product.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    Bundle arguments = new Bundle();
                                    arguments.putString("mname",model.getName());
                                    arguments.putString("mlocation",model.getLocation());
                                    arguments.putString("mdescription",model.getDescription());
                                    arguments.putString("mimge",model.getImage());
                                    Stall_DetailsFragment stall_detailsFragment = new Stall_DetailsFragment();
                                    stall_detailsFragment.setArguments(arguments);
                                    fm.beginTransaction().replace(R.id.fragment_container,stall_detailsFragment).commit();

                                }
                            });

                        }
                        else{
                            holder.itemView.setVisibility(View.INVISIBLE);
                            my_stals_titles.setText("No Stalls Added");
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stalls, parent, false);
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
            edit_product=itemView.findViewById(R.id.edit_vendor_stall);
            view_product=itemView.findViewById(R.id.edt_stall_view_products);
        }
    }
}
