package com.kiduyu.kevinproject.e_grocerystore.VendorFragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kiduyu.kevinproject.e_grocerystore.model.Data;
import com.kiduyu.kevinproject.e_grocerystore.model.Grocery;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.squareup.picasso.Picasso;
import com.kiduyu.kevinproject.e_grocerystore.Activities.MapsActivity;

import java.text.DateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class Stall_DetailsFragment extends Fragment {
    private ProgressDialog loadingBar;
    private static final int PICK_IMAGE_REQUEST=1;
    private RecyclerView recyclerView,recyclerView_product;
    private Uri MImageURI;
    private DatabaseReference mDatabase,msaveProduct;
    private Button stall_map_location;
    private StorageReference mStorageRef;
    private FloatingActionButton fab_add;
    TextView name,location,description;
    ImageView imageview;
    public  String mname="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vendor_view_products,container,false);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("stalls");
        mDatabase.keepSynced(true);

        name=view.findViewById(R.id.fetch_stallname);
        location=view.findViewById(R.id.fetch_stalllocation);
        description=view.findViewById(R.id.fetch_stalldescription);
        imageview=view.findViewById(R.id.fetch_stallimage);

        Bundle bundle=this.getArguments();
        if (bundle!=null){
            String title=bundle.getString("mname");
            String descr=bundle.getString("mdescription");
            String locat=bundle.getString("mlocation");
            String imagevv=bundle.getString("mimge");
            String vetName=bundle.getString("mname");

            mname=vetName;

            name.setText(title);
            description.setText(descr);
            location.setText(locat);
            Picasso.get().load(imagevv).into(imageview);

        }

        fab_add= view.findViewById(R.id.fab_add_products);
        mStorageRef= FirebaseStorage.getInstance().getReference().child("Animals");
        loadingBar = new ProgressDialog(getActivity());



        msaveProduct= FirebaseDatabase.getInstance().getReference().child("stalls").child(Prevalent.currentOnlineVendor.getPhone());

        recyclerView_product=view.findViewById(R.id.recycler_home_products);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(),2);
        recyclerView_product.setLayoutManager(gridLayoutManager);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert);

                LayoutInflater inflater=LayoutInflater.from(getActivity());
                View myview=inflater.inflate(R.layout.add_product,null);

                final AlertDialog dialog=mydialog.create();

                dialog.setView(myview);

                final EditText name=myview.findViewById(R.id.edt_name);
                final EditText price=myview.findViewById(R.id.edt_breed);
                final EditText description=myview.findViewById(R.id.edt_description);
                final EditText product_stock=myview.findViewById(R.id.edt_product_stock);
                final Button image=myview.findViewById(R.id.edt_image);
                Button btnSave=myview.findViewById(R.id.btn_save_add_products);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenFileChooser();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String mname=name.getText().toString().trim();
                        final String mprice=price.getText().toString().trim();
                        final String mdescription=description.getText().toString().trim();
                        final String mstock=product_stock.getText().toString().trim();




                        if (TextUtils.isEmpty(mname)){
                            name.setError("Required Field..");
                            return;
                        }
                        if (TextUtils.isEmpty(mprice)){
                            price.setError("Required Field..");
                            return;
                        }
                        if (TextUtils.isEmpty(mdescription)){
                            description.setError("Required Field..");
                            return;
                        }else{
                            if (MImageURI!=null){
                                loadingBar.setTitle("Adding Product");
                                loadingBar.setMessage("Please wait...");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();

                                final StorageReference filereference= mStorageRef.child(System.currentTimeMillis()
                                        +"."+ getFileExtension(MImageURI));
                                filereference.putFile(MImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(getActivity(),"image Uploaded",Toast.LENGTH_LONG).show();
                                        filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                final  Uri umageuri = uri;
                                                String imge= umageuri.toString();
                                                String date= DateFormat.getDateInstance().format(new Date());



                                                final Grocery animal= new Grocery(mname,mprice,mdescription,date,mstock,imge);

                                                final DatabaseReference RootRef;

                                                RootRef = msaveProduct;
                                                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (!(dataSnapshot.child("Products").child(mname).exists())){
                                                            RootRef.child("Products").child(mname).setValue(animal);
                                                            Toast.makeText(getActivity(),"Product Added Successfully",Toast.LENGTH_SHORT).show();

                                                            dialog.dismiss();
                                                            loadingBar.dismiss();

                                                        }else {
                                                            Toast.makeText(getActivity(),"Product Already Added",Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                            loadingBar.dismiss();

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        });
                                    }
                                });


                            }else{
                                Toast.makeText(getActivity(),"no image Selected", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


                dialog.show();
            }
        });

        stall_map_location=view.findViewById(R.id.stall_map_location);

        stall_map_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("location",Prevalent.currentOnlineVendor.getAddress());
                intent.putExtra("owner",Prevalent.currentOnlineVendor.getName());
                startActivity(intent);
            }
        });

        return  view;
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr=getActivity().getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void OpenFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK &&
                data!= null && data.getData()!=null){
            MImageURI= data.getData();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Grocery> option1 = new FirebaseRecyclerOptions.Builder<Grocery>()
                .setQuery(msaveProduct.child("Products"),Grocery.class)
                .build();


        FirebaseRecyclerAdapter<Grocery,ProductViewHolder> adapter1= new FirebaseRecyclerAdapter<Grocery, ProductViewHolder>(option1) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull Grocery model) {
                holder.name.setText(model.getName());
                holder.price.setText("Ksh. "+model.getPrice());
                holder.edt_stock.setText("Stock: "+model.getStock());
                String ImageView= model.getImage().toString();
                Picasso.get().load(ImageView).into(holder.imageview);



            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_vet, parent, false);
                return new ProductViewHolder(view);
            }
        };
        recyclerView_product.setAdapter(adapter1);
        adapter1.startListening();


    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView name,price,edt_stock;
        ImageView imageview;
        Button add_cart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.item_product_name);
            price=itemView.findViewById(R.id.item_product_price);
            imageview=itemView.findViewById(R.id.item_product_image);
            edt_stock=itemView.findViewById(R.id.item_product_stock);

        }
    }
}

