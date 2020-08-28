package com.kiduyu.kevinproject.e_grocerystore.CustomerFragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.kevinproject.e_grocerystore.model.Cart;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.kiduyu.kevinproject.e_grocerystore.R;




public class Customer_CartFragment extends Fragment {

    Daraja daraja;
    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String CONSUMER_KEY = "XUoqTzugqww0ew1wuLGGgpkwaN6BERW2";
    public static final String CONSUMER_SECRET = "fhnmOKzU2dYl6vk4";
    public static final String CALLBACK_URL = "https://2f50f430.ngrok.io/callback.php?key=kiduyuKLAUS1995";
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private RecyclerView.LayoutManager layoutManager;
    private Button netxt_btn_crt;
    private ProgressDialog loadingBar;
    private Button NextProcessBtn;
    private TextView txtTotalAmount, txtMsg1;
    public static final String SHARED_PREFERENCES = "com.kiduyu.kevinproject.e_grocerystore.mpesa";
    String p;
    private int overTotalPrice = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.cart_fragment,container,false);



        loadingBar = new ProgressDialog(getActivity());
        recyclerView = view.findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        netxt_btn_crt=view.findViewById(R.id.next_btn_cart);
        daraja = Daraja.with("XUoqTzugqww0ew1wuLGGgpkwaN6BERW2", "fhnmOKzU2dYl6vk4", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(getActivity().getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(getActivity(), "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(getActivity().getClass().getSimpleName(), error);
            }
        });


        netxt_btn_crt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Sending Mpesa Request");
                loadingBar.setMessage("Please wait, while we are Verifying the Transaction.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                String overT=String.valueOf(overTotalPrice);

                LNMExpress lnmExpress = new LNMExpress(
                        "174379",
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                        TransactionType.CustomerPayBillOnline,
                        overT,
                        "254799018516",
                        "174379",
                        "254719629173",
                        "http://meal.shulemall.com/api",
                        "E Grocery",
                        "Goods Payment"
                );

                daraja.requestMPESAExpress(lnmExpress,
                        new DarajaListener<LNMResult>() {
                            @Override
                            public void onResult(@NonNull LNMResult lnmResult) {
                                Log.i(getActivity().getClass().getSimpleName(), lnmResult.ResponseDescription);
                                loadingBar.dismiss();
                            }

                            @Override
                            public void onError(String error) {
                                Log.i(getActivity().getClass().getSimpleName(), error);
                                loadingBar.dismiss();
                            }
                        }
                );



            }
        });






        txtTotalAmount =  view.findViewById(R.id.total_price);
        txtMsg1 = view.findViewById(R.id.msg1);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child(Prevalent.currentOnlineUser.getPhone());
        mDatabase.keepSynced(true);

        return view;

    }




    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(mDatabase, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter= new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model) {
                final String usersIDs = getRef(position).getKey();
                mDatabase.child(usersIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String product_name = dataSnapshot.child("pname").getValue().toString();
                        String product_description = dataSnapshot.child("quantity").getValue().toString();
                        String product_price = dataSnapshot.child("price").getValue().toString();


                        holder.ptitle.setText(product_name);
                        holder.pquantity.setText(product_description);
                        holder.pamount.setText(product_price);

                        int oneTyprProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                        overTotalPrice = overTotalPrice + oneTyprProductTPrice;
                        holder.ptotal.setText(String.valueOf(oneTyprProductTPrice));
                        txtTotalAmount.setText("Total Price = Ksh." + String.valueOf(overTotalPrice));








                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_cartlist, parent, false);
                return new CartViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class CartViewHolder extends RecyclerView.ViewHolder{
        TextView ptitle,pquantity,pamount,ptotal;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);


            ptitle=itemView.findViewById(R.id.cart_product_name);
            pquantity=itemView.findViewById(R.id.cart_product_quantity);
            pamount=itemView.findViewById(R.id.cart_product_price);
            ptotal=itemView.findViewById(R.id.cart_product_total);
        }
    }
}


