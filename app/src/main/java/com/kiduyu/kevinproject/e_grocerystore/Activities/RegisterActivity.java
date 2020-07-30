package com.kiduyu.kevinproject.e_grocerystore.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.kiduyu.kevinproject.e_grocerystore.model.Users;
import com.kiduyu.kevinproject.e_grocerystore.model.Vendor;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText Fullname ,PhoneNumber, Password,password_confirm;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView reg_title,signintxt, vendortext,customertxt;
    private String parentDbName = "Users";
    private  String getlocation="";
    private boolean locationSelected =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LoginButton = findViewById(R.id.btn_reg);
        Fullname = findViewById(R.id.fullname);
        PhoneNumber =  findViewById(R.id.phone_number);
        password_confirm =  findViewById(R.id.password_confm);
        Password =findViewById(R.id.password_reg);
        vendortext=findViewById(R.id.vendor_txt);
        customertxt=findViewById(R.id.customer_txt);
        reg_title=findViewById(R.id.reg_title);
        signintxt=findViewById(R.id.signin_txt);
        loadingBar= new ProgressDialog(this);

        final Spinner spinner1 =findViewById(R.id.edt_location);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String firstItem = String.valueOf(spinner1.getSelectedItem());
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (firstItem.equals(String.valueOf(spinner1.getSelectedItem()))) {

                    // ToDo when first item is selected
                    Toast.makeText(parent.getContext(),

                            "You have selected No Location or LandMark Near Your Office",

                            Toast.LENGTH_LONG).show();


                }else{
                    locationSelected=true;
                    getlocation= parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vendortext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Vendor Register");
                reg_title.setText("Vendor Registration");
                parentDbName="Vendors";
                customertxt.setVisibility(View.VISIBLE);
                vendortext.setVisibility(View.INVISIBLE);
            }
        });

        customertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Customer Register");
                reg_title.setText("Customer Registration");
                parentDbName="Users";
                vendortext.setVisibility(View.VISIBLE);
                customertxt.setVisibility(View.INVISIBLE);
            }
        });

        signintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });


    }

    private void CreateAccount()
    {
        final String name = Fullname.getText().toString();
        final String phone = PhoneNumber.getText().toString();
        final String password = Password.getText().toString();
        String password2 = password_confirm.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(password2))
        {
            Toast.makeText(this, "The Passwords Don't Match.", Toast.LENGTH_SHORT).show();
        }else if (locationSelected!=true)
        {
            Toast.makeText(this, "choose location.", Toast.LENGTH_SHORT).show();
        }
        else
        {

                        loadingBar.setTitle("Create Account");
                        loadingBar.setMessage("Please wait, while we are checking the credentials.");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        ValidatephoneNumber(name, phone,getlocation, password);







        }
    }

    private void ValidatephoneNumber(final String name, final String phone, final String getlocation, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (parentDbName.equals("Vendors")){
                    if (!(dataSnapshot.child("Vendors").child(phone).exists()))
                    {
                        Vendor veterinary = new Vendor(name,phone,password,"",getlocation);

                        RootRef.child("Vendors").child(phone).setValue(veterinary)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this, "Congratulations "+name+" your account has been created.", Toast.LENGTH_LONG).show();
                                            loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }else if (parentDbName.equals("Users")){

                    if (!(dataSnapshot.child("Users").child(phone).exists()))
                    {
                        Users users = new Users(name,phone,password,"",getlocation);


                        RootRef.child("Users").child(phone).setValue(users)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this, "Congratulations "+name+", your account has been created.", Toast.LENGTH_LONG).show();
                                            loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
