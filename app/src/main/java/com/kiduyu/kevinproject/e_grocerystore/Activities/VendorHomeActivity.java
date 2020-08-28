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
import com.kiduyu.kevinproject.e_grocerystore.CustomerFragments.Customer_SignoutFragment;
import com.kiduyu.kevinproject.e_grocerystore.VendorFragments.HomeFragment;
import com.kiduyu.kevinproject.e_grocerystore.VendorFragments.Vendor_GroceriesFragment;
import com.kiduyu.kevinproject.e_grocerystore.VendorFragments.Vendor_StallsFragment;
import com.kiduyu.kevinproject.e_grocerystore.model.Prevalent;
import com.kiduyu.kevinproject.e_grocerystore.R;


public class VendorHomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);

        Toolbar toolbar=findViewById(R.id.vendor_toolbar);
        setSupportActionBar(toolbar);

        drawer= findViewById(R.id.vendor_drawer_layout);
        NavigationView navigationView= findViewById(R.id.vendor_nav_view);

        View headerView = navigationView.getHeaderView(0);

        TextView user= headerView.findViewById(R.id.nav_header_name);
        TextView phone= headerView.findViewById(R.id.nav_header_phone);

        user.setText(Prevalent.currentOnlineVendor.getName());
        phone.setText(Prevalent.currentOnlineVendor.getPhone());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                switch (Item.getItemId()) {
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();

                        break;

                    case R.id.nav_signout:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Customer_SignoutFragment()).commit();

                        break;

                    case R.id.nav_stalls:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Vendor_StallsFragment()).commit();

                        break;

                    case R.id.nav_products:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Vendor_GroceriesFragment()).commit();

                        break;

                    case R.id.nav_share:

                        Toast.makeText(VendorHomeActivity.this, "Share this app", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_send:

                        Toast.makeText(VendorHomeActivity.this, "Send this app", Toast.LENGTH_SHORT).show();
                        break;

                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState== null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Vendor_StallsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_stalls);}




    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }}


}
