package com.kiduyu.kevinproject.e_grocerystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.kiduyu.kevinproject.e_grocerystore.model.Users;
import com.kiduyu.kevinproject.e_grocerystore.model.Vendor;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText phonenumber;
    private EditText pass;
    private Button btnLogin;
    private CheckBox chkBoxRememberMe;
    private ProgressDialog loadingBar;
    private TextView signUp,login_title,customer_txt,vendor_txt;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        phonenumber=findViewById(R.id.number_login);
        pass=findViewById(R.id.password_login);
        btnLogin=findViewById(R.id.btn_login);
        signUp=findViewById(R.id.signup_txt);
        login_title=findViewById(R.id.login_title);
        customer_txt=findViewById(R.id.customer_txt);
        vendor_txt=findViewById(R.id.vendor_txt);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        Paper.init(this);


        vendor_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setText("Vendor Login");
                login_title.setText("Vendor Login");
                parentDbName="Vendors";
                customer_txt.setVisibility(View.VISIBLE);
                vendor_txt.setVisibility(View.INVISIBLE);
            }
        });

        customer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setText("Customer Login");
                login_title.setText("Customer Login");
                parentDbName="Users";
                vendor_txt.setVisibility(View.VISIBLE);
                customer_txt.setVisibility(View.INVISIBLE);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }
    private void LoginUser()
    {
        String phone = phonenumber.getText().toString();
        String password = pass.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    if (parentDbName.equals("Vendors"))
                    {
                        Vendor usersData = dataSnapshot.child("Vendors").child(phone).getValue(Vendor.class);
                        if (usersData.getPhone().equals(phone))
                        {
                            if (usersData.getPassword().equals(password))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Vendor, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, VendorHomeActivity.class);
                                Prevalent.currentOnlineVendor = usersData;
                                startActivity(intent);


                        }else {

                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "password is incorrect.", Toast.LENGTH_SHORT).show();
                            }

                            }else {

                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Phone number is incorrect.", Toast.LENGTH_SHORT).show();
                        }


                    }else if (parentDbName.equals("Users"))
                    {
                        Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                        if (usersData.getPhone().equals(phone))
                        {
                            if (usersData.getPassword().equals(password))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome User, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, CustomerHomeActivity.class);
                                Prevalent.currentOnlineUser= usersData;
                                startActivity(intent);


                            }else {

                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "password is incorrect.", Toast.LENGTH_SHORT).show();
                            }

                        }else {

                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Phone number is incorrect.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



