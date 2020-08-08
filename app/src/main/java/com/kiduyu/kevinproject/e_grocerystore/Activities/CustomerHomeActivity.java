package com.kiduyu.kevinproject.e_grocerystore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.kiduyu.kevinproject.e_grocerystore.CustomerFragments.Customer_HomeFragment;
import com.kiduyu.kevinproject.e_grocerystore.CustomerFragments.Customer_ProductsFragment;
import com.kiduyu.kevinproject.e_grocerystore.CustomerFragments.Customer_SignoutFragment;
import com.kiduyu.kevinproject.e_grocerystore.CustomerFragments.Customer_StallsFragment;
import com.kiduyu.kevinproject.e_grocerystore.R;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;

public class CustomerHomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);




    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }}
}
