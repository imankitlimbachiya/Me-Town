package com.app.metown.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SetRangeActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtAreaName;
    Button btnAdd;
    GoogleMap mMap;
    SeekBar RadiusSeekBar;
    SupportMapFragment supportMapFragment;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    List<Place.Field> fields;
    float Latitude, Longitude, Radius = 0;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    String LocationName = "", LocationAddress = "", Range = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_range);

        Log.e("Activity", "SetRangeActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        ViewOnPlaces();

        getLastLocation();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtAreaName = findViewById(R.id.txtAreaName);

        btnAdd = findViewById(R.id.btnAdd);

        RadiusSeekBar = findViewById(R.id.RadiusSeekBar);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    public void ViewOnPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBqT3CqHS34LJFBWsHYjINUKMnHGtiag0Q");
        }
        // PlacesClient placesClient = Places.createClient(mContext);
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(Latitude, Longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        enableUserLocation();
        AddMarker(latLng);
    }

    public void DisplayMarker(final float Radius) {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.clear();
                LatLng latLng = new LatLng(Latitude, Longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                enableUserLocation();
                AddMarker(latLng);
                AddCircle(latLng, Radius);
            }
        });
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtAreaName.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        RadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                if (progress == 1) {
                    Radius = 500;
                } else if (progress == 2) {
                    Radius = 1000;
                } else if (progress == 3) {
                    Radius = 1500;
                } else {
                    Radius = 0;
                }
                Range = String.valueOf(Radius);
                DisplayMarker(Radius);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtAreaName:
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(mContext);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;
            case R.id.btnAdd:
                if (Range.equals("0")) {
                    Toast.makeText(mContext, "Please select your range.", Toast.LENGTH_LONG).show();
                } else {
                    SaveLocationApi(Latitude, Longitude, LocationName, Range);
                }
                break;
        }
    }

    private void SaveLocationApi(final float Latitude, final float Longitude, final String LocationName, final String Range) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SAVE_LOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().SAVE_LOCATION + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, "Location Saved", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        String msg = JsonMain.getString("msg");
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().SAVE_LOCATION + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"location_name\":\"" + LocationName + "\",\"lats\":\"" + Latitude +
                        "\",\"longs\":\"" + Longitude + "\",\"user_range\":\"" + Range + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().SAVE_LOCATION + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SAVE_LOCATION);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                // Log.e("place", "Place: " + place.getName() + ", " + place.getId() + "latlng" + place.getLatLng());
                LocationName = place.getName();
                LocationAddress = place.getName();
                txtAreaName.setText(LocationName);
                String LatLng = String.valueOf(place.getLatLng());
                String[] latLng = LatLng.substring(10, LatLng.length() - 1).split(",");
                Latitude = Float.parseFloat(latLng[0]);
                Longitude = Float.parseFloat(latLng[1]);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            }  // The user canceled the operation.
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Latitude = (float) mLastLocation.getLatitude();
            Longitude = (float) mLastLocation.getLongitude();
            GetLocationAddress();
        }
    };

    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Latitude = (float) location.getLatitude();
                            Longitude = (float) location.getLongitude();
                            GetLocationAddress();
                        }
                    }
                });
            } else {
                Toast.makeText(mContext, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void GetLocationAddress() {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Float.valueOf(Latitude), Float.valueOf(Longitude), 1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        String CityName = addresses.get(0).getAddressLine(0);
        String StateName = addresses.get(0).getAddressLine(1);
        String CountryName = addresses.get(0).getAddressLine(2);
        LocationName = CityName;
        LocationAddress = CityName + ", " + StateName + ", " + CountryName;
        txtAreaName.setText(CityName);
    }

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    private void AddMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);
    }

    private void AddCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 187, 0));
        circleOptions.fillColor(Color.argb(0, 255, 255, 255));
        circleOptions.strokeWidth(5);
        mMap.addCircle(circleOptions);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}