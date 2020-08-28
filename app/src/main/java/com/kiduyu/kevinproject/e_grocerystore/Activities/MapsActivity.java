package com.kiduyu.kevinproject.e_grocerystore.Activities;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kiduyu.kevinproject.e_grocerystore.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    private LocationManager locationManager;
    private String provider;
    //FusedLocationProviderClient fusedLocationProviderClient;
    public static final int REQUEST_CODE = 101;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        LatLng latL = new LatLng(-0.393794,36.955898);
        mMap.addMarker(new MarkerOptions().position(latL).title("current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));



        String OfficeLocation = getIntent().getStringExtra("location");
        String OfficeOwner = getIntent().getStringExtra("owner");

        if (OfficeLocation.equals("Dedan Kimathi")) {

            String O_location = getString(R.string.dedan_kimathi);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Office"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        } else if (OfficeLocation.equals("Kingongo")) {

            String O_location = getString(R.string.kingongo);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Stall"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        } else if (OfficeLocation.equals("Kamakwa")) {

            String O_location = getString(R.string.kamakwa);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Stall"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        } else if (OfficeLocation.equals("Skuta")) {

            String O_location = getString(R.string.skuta);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Stall"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        } else if (OfficeLocation.equals("Nyeri Town")) {

            String O_location = getString(R.string.nyeri);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Stall"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        } else if (OfficeLocation.equals("Mweiga")) {

            String O_location = getString(R.string.muiga);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Stall"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        } else if (OfficeLocation.equals("Murigato")) {

            String O_location = getString(R.string.murigato);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Stall"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        } else if (OfficeLocation.equals("Nyeri Uptown")) {

            String O_location = getString(R.string.stage_kimathi);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Stall"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        } else if (OfficeLocation.equals("Near kingongo Prisons")) {

            String O_location = getString(R.string.near_prisons);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Stall"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        } else if (OfficeLocation.equals("Near Naivas Supermarket")) {

            String O_location = getString(R.string.near_naivas);
            String[] afterSplitLoc = O_location.split(",");
            double latitude = Double.parseDouble(afterSplitLoc[0]);
            double longitude = Double.parseDouble(afterSplitLoc[1]);

            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title("This Is " + OfficeOwner + "'s Stall"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        }

    }
}
