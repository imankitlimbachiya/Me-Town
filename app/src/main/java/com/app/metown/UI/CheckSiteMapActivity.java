package com.app.metown.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.metown.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CheckSiteMapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapLongClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    GoogleMap mMap;
    LatLng latLng;
    SupportMapFragment supportMapFragment;
    String Latitude = "", Longitude = "";
    int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_site_map);

        Log.e("Activity", "CheckSiteMapActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetIntentData();

        ViewInitialization();

        ViewOnClick();
    }

    public void GetIntentData() {
        Latitude = getIntent().getStringExtra("Latitude");
        Longitude = getIntent().getStringExtra("Longitude");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        latLng = new LatLng(Float.parseFloat(Latitude), Float.parseFloat(Longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        EnableUserLocation();
        mMap.setOnMapLongClickListener(this);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);
    }

    private void EnableUserLocation() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                HandleMapLongClick(latLng);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
            }
        } else {
            HandleMapLongClick(latLng);
        }
    }

    private void HandleMapLongClick(LatLng latLng) {
        mMap.clear();
        AddMarker(latLng);
        AddCircle(latLng, 400);
    }

    private void AddMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    private void AddCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 187, 0));
        circleOptions.fillColor(Color.argb(0, 255, 255, 255));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}