package com.kiduyu.kevinproject.e_grocerystore.VendorFragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.kiduyu.kevinproject.e_grocerystore.model.Data;
import com.kiduyu.kevinproject.e_grocerystore.model.Grocery;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class Add_GroceryFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST=1;
    private Uri MImageURI;
    private StorageReference mStorageRef;
    private ProgressDialog loadingBar;
    private  ImageView stall_groceryimage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vendor_add_groceries,container,false);

        final EditText id = view.findViewById(R.id.findstall);

        final EditText stall_name=view.findViewById(R.id.edt_stall_name);
        final EditText stall_description=view.findViewById(R.id.edt_stall_description);
        final ImageView edt_stall_image=view.findViewById(R.id.edt_stall_image);
        stall_name.setEnabled(false);
        stall_description.setEnabled(false);


        final EditText stall_groceryname = view.findViewById(R.id.edt_stall_groceryname);
        final EditText stall_groceryprice = view.findViewById(R.id.edt_stall_groceryprice);
        final EditText stall_grocerystock = view.findViewById(R.id.edt_stall_grocerystock);
        final EditText stall_grocerynote = view.findViewById(R.id.edt_stall_grocerynote);
        stall_groceryimage = view.findViewById(R.id.edt_stall_groceryimage);

        Button find=view.findViewById(R.id.btn_findstall);
        Button btn_stall_grocery_save=view.findViewById(R.id.btn_stallgrocery_save);
        loadingBar = new ProgressDialog(getActivity());

        stall_groceryimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mdetails=id.getText().toString();

                if (TextUtils.isEmpty(mdetails)){
                    id.setError( "Id is Required");
                    return;
                }else {

                    loadingBar.setTitle("Finding Stall");
                    loadingBar.setMessage("Please wait, while we are checking ....");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    final DatabaseReference RootRef;
                    RootRef = FirebaseDatabase.getInstance().getReference().child("stalls").child(Prevalent.currentOnlineUser.getPhone());

                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(mdetails).exists())
                            {
                                Data usersData = dataSnapshot.child(mdetails).getValue(Data.class);

                                String Sname=usersData.getName();
                                stall_name.setText(Sname);

                                String Sdescription=usersData.getDescription();
                                stall_description.setText(Sdescription);

                                String ImageView= usersData.getImage().toString();
                                Picasso.get().load(ImageView).into(edt_stall_image);

                                loadingBar.dismiss();


                            }else {
                                Toast.makeText(getActivity(), "User with this Id " + mdetails + "  do not exists.", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }
        });


        btn_stall_grocery_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=stall_groceryname.getText().toString();
                final String price=stall_groceryprice.getText().toString();
                final String description=stall_grocerynote.getText().toString();
                final String mstall_name=stall_name.getText().toString();
                final String date= DateFormat.getDateInstance().format(new Date());
                final String stock=stall_grocerystock.getText().toString();

                if (TextUtils.isEmpty(mstall_name)){
                    Toast.makeText(getActivity(), "Please Find A Stall First", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(name))
                {
                    stall_groceryname.setError( "required!!");
                    return;
                }
                else if (TextUtils.isEmpty(price))
                {
                    stall_groceryprice.setError( "required!!");
                    return;
                }
                else if (TextUtils.isEmpty(description))
                {
                    stall_grocerynote.setError( "required!!");
                    return;
                }
                else if (TextUtils.isEmpty(stock))
                {
                    stall_grocerystock.setError( "required!!");
                    return;
                }else {

                    loadingBar.setTitle("Saving Changes");
                    loadingBar.setMessage("Please wait, while we prepare Things");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();


                    if (MImageURI!=null){
                        mStorageRef= FirebaseStorage.getInstance().getReference().child("groceries");

                        final StorageReference filereference= mStorageRef.child(System.currentTimeMillis()
                                +"."+ getFileExtension(MImageURI));
                        filereference.putFile(MImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final  Uri umageuri = uri;
                                        String imge= umageuri.toString();
                                        final Grocery grocery = new Grocery(name, price, description, mstall_name, date, stock, imge);

                                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("groceries").child(mstall_name);

                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (!(dataSnapshot.child(name).exists())){

                                                    mDatabase.child(name).setValue(grocery);
                                                    Toast.makeText(getActivity(),name+" Added Successfully",Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                            new Vendor_GroceriesFragment()).commit();

                                                }else{
                                                    Toast.makeText(getActivity(),name+" Not Out of stock yet",Toast.LENGTH_LONG).show();

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
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }


            }
        });






        return  view;
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr=getActivity().getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void OpenFileChooser() {
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
            stall_groceryimage.setImageURI(MImageURI);
        }

    }
}
