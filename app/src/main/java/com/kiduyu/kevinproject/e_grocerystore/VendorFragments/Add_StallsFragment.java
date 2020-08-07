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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

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
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;

import java.text.DateFormat;
import java.util.Date;

public class Add_StallsFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST=1;
    private Uri MImageURI;
    private StorageReference mStorageRef;
    private EditText name,location,description;
    private Button add_btn;
    private ImageView stall_image;
    private DatabaseReference mDatabase;
    private ProgressDialog loadingBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_vendor_add_stalls,container,false);

        name=view.findViewById(R.id.stall_name);
        final Spinner spinner1 =view.findViewById(R.id.edt_location);
        description=view.findViewById(R.id.stall_description);
        add_btn=view.findViewById(R.id.btn_save);
        stall_image=view.findViewById(R.id.stall_image);
        loadingBar = new ProgressDialog(getActivity());
        mStorageRef= FirebaseStorage.getInstance().getReference().child("Stalls");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("stalls");
        mDatabase.keepSynced(true);

        stall_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String mname=name.getText().toString().trim();
                final String mlocation= Prevalent.currentOnlineVendor.getAddress();
                final String mdescription=description.getText().toString().trim();

                if (TextUtils.isEmpty(mname)){
                    name.setError("Stall Name Cannot be Empty..");
                    return;
                }
                if (TextUtils.isEmpty(mdescription)){
                    description.setError("Description Cannot be Empty..");
                    return;
                }

                loadingBar.setTitle("Adding Stall");
                loadingBar.setMessage("Please wait, while we are Save the Details...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                if (MImageURI!=null){

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
                                    String mdate= DateFormat.getDateInstance().format(new Date());
                                    String mowner= Prevalent.currentOnlineVendor.getName();
                                    final String mphone= Prevalent.currentOnlineVendor.getPhone();
                                    String imge= umageuri.toString();
                                    final Data data= new Data(imge,mname,mowner,mlocation,mdescription,mdate,mphone);

                                    final DatabaseReference RootRef;

                                    RootRef = mDatabase;

                                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!(dataSnapshot.child(mphone).child(mname).exists())){
                                                RootRef.child(mphone).child(mname).setValue(data);
                                                Toast.makeText(getActivity(),mname+" Added Successfully",Toast.LENGTH_SHORT).show();

                                                loadingBar.dismiss();
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                        new Vendor_StallsFragment()).commit();
                                            }else{
                                                Toast.makeText(getActivity(),mname+" Already On the List",Toast.LENGTH_LONG).show();

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
            stall_image.setImageURI(MImageURI);
        }

    }
}
