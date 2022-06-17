package com.risibleapps.mywallet.bottomNavFragments.addRecord;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.risibleapps.mywallet.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int GPS_REQUEST_CODE = 10;
    //string to save location address
    String locationAddress;
    MarkerOptions markerOptions;
    /*
        this object contains the current marker position
        on location change, the previous marker is removed and new marker is inserted
    */
    Marker marker;
    private Toolbar toolbar;
    private TextView textViewLocationAddress;
    private AppCompatButton buttonSelectLocation;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;

    private ImageView imgViewCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //setting the toolbar
        setToolbar();

        //initialize views
        initializeView();

        //instantiating marker options
        markerOptions = new MarkerOptions();

        //button select location click listener
        buttonSelectLocation.setOnClickListener(view -> {

            Intent locationIntent = new Intent(LocationActivity.this, ActivityAddTransactionDetail.class);
            locationIntent.putExtra("location", locationAddress);
            setResult(Activity.RESULT_OK, locationIntent);
            finish();
        });

        if (isGpsEnabled()) {
            //getting the current location
            getCurrentLocation();
        }

        //move to current location on click
        imgViewCurrentLocation.setOnClickListener(view -> {

            if(isGpsEnabled())
            {
                //this removes the previous marker
                if(marker != null)
                {
                    marker.remove();
                }
                getCurrentLocation();
            }
        });

    }

    private void setToolbar() {
        //initializing toolbar
        toolbar = findViewById(R.id.toolbar_location);
        setSupportActionBar(toolbar);

        //setting the back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initializeView() {
        textViewLocationAddress = findViewById(R.id.text_address);
        buttonSelectLocation = findViewById(R.id.btn_location);
        imgViewCurrentLocation = findViewById(R.id.img_current_location);

        //map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this::onMapReady);

        mLocationClient = new FusedLocationProviderClient(this);

        //bringing views to front on map
        textViewLocationAddress.bringToFront();
        buttonSelectLocation.bringToFront();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //OnMapReadyCallback interface
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //indicating the user's current location  (location icon on status bar)
        mGoogleMap.setMyLocationEnabled(true);

        //map click listener
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //this removes the previous marker
                if(marker != null)
                {
                    marker.remove();
                }

                marker = mGoogleMap.addMarker(markerOptions
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker()));


                //getting the address of current LatLng
                Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    locationAddress = addresses.get(0).getAddressLine(0);

                    //setting the current address to textview
                    textViewLocationAddress.setText(locationAddress);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //GoogleApiClient interface functions
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        mLocationClient.getLastLocation().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Location location = task.getResult();

                try {

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                    mGoogleMap.moveCamera(cameraUpdate);
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker()));

                    //getting the address of current location
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());


                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    locationAddress = addresses.get(0).getAddressLine(0);

                    //setting the current address to textview
                    textViewLocationAddress.setText(locationAddress);

                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();

                    //jugaru method for handling the NullPointerException of getLastLocation() where getLatitude() & getLongitude() have null values
                    getCurrentLocation();
                }

            }
        });
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean isProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isProviderEnabled) {
            return true;
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS Permission")
                    .setMessage("To get current Location, GPS is required. Enable GPS?")
                    .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST_CODE) {
            //getting the current location
            getCurrentLocation();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}