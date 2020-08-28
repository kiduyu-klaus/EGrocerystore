package com.kiduyu.kevinproject.e_grocerystore.CustomerFragments;

import android.app.ProgressDialog;
import android.graphics.Paint;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.kevinproject.e_grocerystore.model.Grocery;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class Customer_ProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase,mstock;
    TextView textView;
    TextView name,location,description;
    ImageView imageview;
    private ProgressDialog loadingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_products, container, false);

        name=view.findViewById(R.id.fetch_stallname);
        location=view.findViewById(R.id.fetch_stalllocation);
        description=view.findViewById(R.id.fetch_stalldescription);
        imageview=view.findViewById(R.id.fetch_stallimage);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("stalls").child(Prevalent.currentOnlineUser.getPhone()).child("Products");
        mstock = FirebaseDatabase.getInstance().getReference().child("stalls");
        mDatabase.keepSynced(true);

        recyclerView = view.findViewById(R.id.customer_product_recycler);
        Bundle bundle=this.getArguments();
        if (bundle!=null){
            String stallname=bundle.getString("stallname");
            String stalloc=bundle.getString("stalllocation");
            String stalldes=bundle.getString("stalldescription");
            String stallimg=bundle.getString("stallimage");
            textView=view.findViewById(R.id.customer_products_title);
            textView.setText("Products For "+stallname);
            name.setText(stallname);
            location.setText(stalloc);
            description.setText(stalldes);
            Picasso.get().load(stallimg).into(imageview);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



        }
        loadingBar = new ProgressDialog(getActivity());



        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
/*
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
                            final String product_name = dataSnapshot.child("name").getValue().toString();
                            String product_description = dataSnapshot.child("description").getValue().toString();
                            String product_date = dataSnapshot.child("date").getValue().toString();
                            final String product_price = dataSnapshot.child("price").getValue().toString();


                            holder.name.setText(product_name);
                            holder.price.setText("Ksh. "+product_price);
                            holder.imagedescr.setText(product_description);
                            String ImageView= model.getImage().toString();
                            Picasso.get().load(ImageView).into(holder.imageview);

                            holder.add_cart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert);

                                    LayoutInflater inflater=LayoutInflater.from(getActivity());
                                    View myview=inflater.inflate(R.layout.quantity,null);

                                    final AlertDialog dialog=mydialog.create();

                                    dialog.setView(myview);
                                    final ElegantNumberButton quantity_btn = myview.findViewById(R.id.number_btn);
                                    Button cartAdd = myview.findViewById(R.id.pd_add_to_cart_button);

                                    cartAdd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String saveCurrentTime, saveCurrentDate;
                                            Calendar calForDate = Calendar.getInstance();
                                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                            saveCurrentDate = currentDate.format(calForDate.getTime());

                                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                            saveCurrentTime = currentDate.format(calForDate.getTime());

                                            loadingBar.setTitle("Adding ");
                                            loadingBar.setMessage("Please wait, ...");
                                            loadingBar.setCanceledOnTouchOutside(false);
                                            loadingBar.show();

                                            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
                                            final HashMap<String, Object> cartMap = new HashMap<>();
                                            cartMap.put("pname", product_name);
                                            cartMap.put("price", product_price);
                                            cartMap.put("date", saveCurrentDate);
                                            cartMap.put("time", saveCurrentTime);
                                            cartMap.put("quantity", quantity_btn.getNumber());
                                            cartMap.put("discount", "");






                                            cartListRef.child(Prevalent.currentOnlineUser.getPhone()).child(product_name)
                                                    .updateChildren(cartMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(getActivity(), "Added to Cart List.", Toast.LENGTH_SHORT).show();
                                                                int newstock=Integer.parseInt(model.getStock())-Integer.parseInt(quantity_btn.getNumber());
                                                                final HashMap<String, Object> Stock = new HashMap<>();
                                                                Stock.put("stock", String.valueOf(newstock));

                                                                mDatabase.child(product_name).updateChildren(Stock)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful())
                                                                                {
                                                                                    Toast.makeText(getActivity(), "Stock Deducted.", Toast.LENGTH_SHORT).show();
                                                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                                                            new Customer_CartFragment()).commit();
                                                                                    loadingBar.dismiss();
                                                                                    dialog.dismiss();

                                                                                }
                                                                            }
                                                                        });
                                                                loadingBar.dismiss();
                                                                dialog.dismiss();

                                                            }
                                                        }
                                                    });





                                        }
                                    });



                                    dialog.show();

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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                return new ProductsViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

 */

    public static class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView name,price,imagedescr;
        ImageView imageview;
        Button add_cart;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.item_product_name);
            price=itemView.findViewById(R.id.item_product_price);
            imageview=itemView.findViewById(R.id.item_product_image);
            imagedescr=itemView.findViewById(R.id.item_product_desc);
            add_cart=itemView.findViewById(R.id.item_cust_product_addtocart);
        }
    }
}