package com.app.metown.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SetLocationActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    Button btnDone, btnUseCurrentLocation;
    ImageView imgBack;
    TextView txtSearchYourLocationHere;
    List<Place.Field> fields;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    FusedLocationProviderClient mFusedLocationClient;
    String Latitude = "", Longitude = "", LocationName = "", LocationAddress = "";
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        Log.e("Activity", "SetLocationActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewOnPlaces();

        ViewInitialization();

        ViewOnClick();
    }

    public void ViewOnPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBqT3CqHS34LJFBWsHYjINUKMnHGtiag0Q");
        }
        // PlacesClient placesClient = Places.createClient(mContext);
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtSearchYourLocationHere = findViewById(R.id.txtSearchYourLocationHere);

        btnDone = findViewById(R.id.btnDone);
        btnUseCurrentLocation = findViewById(R.id.btnUseCurrentLocation);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnUseCurrentLocation.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        txtSearchYourLocationHere.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
            case R.id.btnDone:
                String LocationName = txtSearchYourLocationHere.getText().toString().trim();
                if (LocationName.equals("")) {
                    Toast.makeText(mContext, "Please Enter your Search your location here.", Toast.LENGTH_LONG).show();
                } else {
                    TownStatusApi(LocationName);
                }
                break;
            case R.id.txtSearchYourLocationHere:
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(mContext);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;
            case R.id.btnUseCurrentLocation:
                getLastLocation();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                // Log.e("place", "Place: " + place.getName() + ", " + place.getId() + "latlng" + place.getLatLng());
                LocationName = place.getName();
                LocationAddress = place.getName();
                txtSearchYourLocationHere.setText(LocationName);
                String LatLng = String.valueOf(place.getLatLng());
                String[] latLng = LatLng.substring(10, LatLng.length() - 1).split(",");
                Latitude = latLng[0];
                Longitude = latLng[1];
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
            Latitude = String.valueOf(mLastLocation.getLatitude());
            Longitude = String.valueOf(mLastLocation.getLongitude());
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
                            Latitude = String.valueOf(location.getLatitude());
                            Longitude = String.valueOf(location.getLongitude());
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
        txtSearchYourLocationHere.setText(CityName);
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

    private void TownStatusApi(final String LocationName) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().TOWN_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().TOWN_STATUS + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        String TownID = objectData.getString("id");
                        String TownName = objectData.getString("name");
                        String TownIsOpen = objectData.getString("is_open");
                        String TownRequirePeople = objectData.getString("requiredPeoples");
                        String TownPeople = objectData.getString("peoples");

                        editor.putString("LocationID", TownID);
                        editor.putString("LocationName", LocationName);
                        editor.putString("Latitude", Latitude);
                        editor.putString("Longitude", Longitude);
                        editor.putString("LocationAddress", LocationAddress);
                        Intent intent;
                        if (TownIsOpen.equals("1")) {
                            editor.putString("IsLocation", "1");
                            intent = new Intent(mContext, HomeActivity.class);
                        } else {
                            editor.putString("IsLocation", "0");
                            intent = new Intent(mContext, LocationActivity.class);
                        }
                        editor.apply();
                        editor.commit();
                        startActivity(intent);
                        finish();
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                        editor.putString("LocationID", "");
                        editor.putString("LocationName", "");
                        editor.putString("Latitude", "");
                        editor.putString("Longitude", "");
                        editor.putString("LocationAddress", "");
                        editor.putString("IsLocation", "");
                        editor.apply();
                        editor.commit();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().TOWN_STATUS + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"name\":\"" + LocationName + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().TOWN_STATUS + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().TOWN_STATUS);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
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