package com.kiduyu.kevinproject.e_grocerystore.CustomerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.kiduyu.kevinproject.e_grocerystore.model.Grocery;



public class Customer_ProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_products, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("groceries");
        mDatabase.keepSynced(true);

        recyclerView = view.findViewById(R.id.customer_product_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Grocery> option = new FirebaseRecyclerOptions.Builder<Grocery>()
                .setQuery(mDatabase, Grocery.class)
                .build();

        FirebaseRecyclerAdapter<Grocery, ProductsViewHolder> adapter= new FirebaseRecyclerAdapter<Grocery, ProductsViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductsViewHolder holder, int position, @NonNull final Grocery model) {
                final String usersIDs = getRef(position).getKey();
                mDatabase.child(usersIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("name")){
                            String product_name = dataSnapshot.child("name").getValue().toString();
                            String product_description = dataSnapshot.child("description").getValue().toString();
                            String product_date = dataSnapshot.child("date").getValue().toString();
                            String product_price = dataSnapshot.child("price").getValue().toString();


                            holder.ptitle.setText(product_name);
                            holder.pnote.setText(product_description);
                            holder.pdate.setText(product_date);
                            holder.pamount.setText(product_price);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_groceries, parent, false);
                return new ProductsViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView pdate,ptitle,pnote,pamount;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            pdate=itemView.findViewById(R.id.date);
            ptitle=itemView.findViewById(R.id.title);
            pnote=itemView.findViewById(R.id.note);
            pamount=itemView.findViewById(R.id.amount);
        }
    }
}