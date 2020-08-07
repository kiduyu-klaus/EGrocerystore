package com.kiduyu.kevinproject.e_grocerystore.VendorFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.kiduyu.kevinproject.e_grocerystore.CustomerFragments.Customer_ProductsFragment;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.kiduyu.kevinproject.e_grocerystore.model.Grocery;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.squareup.picasso.Picasso;

public class Vendor_GroceriesFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vendor_groceries_add,container,false);
        FloatingActionButton fab_btn = view.findViewById(R.id.fab_add);

        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Add_GroceryFragment()).commit();

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("groceries");
        mDatabase.keepSynced(true);

        recyclerView = view.findViewById(R.id.grocery_recucler_home);
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
            protected void onBindViewHolder(@NonNull final ProductsViewHolder holder, int position, @NonNull Grocery model) {



            } @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                return new ProductsViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView pdate,ptitle,pnote,pamount,pstock;
        ImageView pimage;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            pimage=itemView.findViewById(R.id.item_grocery_image);
            ptitle=itemView.findViewById(R.id.item_grocery_title);
            pnote=itemView.findViewById(R.id.item_grocery_note);
            pamount=itemView.findViewById(R.id.item_grocery_amount);
            pstock=itemView.findViewById(R.id.item_groceryadd_stock);
        }
    }
}
