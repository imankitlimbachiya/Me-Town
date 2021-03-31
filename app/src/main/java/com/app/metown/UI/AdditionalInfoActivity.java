package com.app.metown.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.AppConstants.Utility;
import com.app.metown.R;
import com.app.metown.VolleySupport.VolleyMultipartRequest;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdditionalInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgPhoto;
    Button btnAttachFiles;
    TextView txtDone, txtBusinessStart, txtBusinessClose, txtDayOff, txtBenefitForMeTownMember, txtWebPage, txtBusinessPermits;
    EditText edtBusinessStart, edtBusinessClose, edtDayOff, edtBenefitForMeTownMember, edtWebPage;
    String NameOfBusiness, CategoryID, Address, DetailedAddress, ContactNumber, IntroductionOfBusiness,
            BusinessStart, BusinessClose, DayOff, BenefitForMeTownMember, WebPage;
    Bitmap bitmap;
    private static final int SELECT_IMAGE = 4;
    private int MY_REQUEST_CODE, REQUEST_CODE;
    static ArrayList<Bitmap> photoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);

        Log.e("Activity", "AdditionalInfoActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        ViewSetText();

        GetIntentData();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgPhoto = findViewById(R.id.imgPhoto);

        btnAttachFiles = findViewById(R.id.btnAttachFiles);

        txtDone = findViewById(R.id.txtDone);
        txtBusinessStart = findViewById(R.id.txtBusinessStart);
        txtBusinessClose = findViewById(R.id.txtBusinessClose);
        txtDayOff = findViewById(R.id.txtDayOff);
        txtBenefitForMeTownMember = findViewById(R.id.txtBenefitForMeTownMember);
        txtWebPage = findViewById(R.id.txtWebPage);
        txtBusinessPermits = findViewById(R.id.txtBusinessPermits);

        edtBusinessStart = findViewById(R.id.edtBusinessStart);
        edtBusinessClose = findViewById(R.id.edtBusinessClose);
        edtDayOff = findViewById(R.id.edtDayOff);
        edtBenefitForMeTownMember = findViewById(R.id.edtBenefitForMeTownMember);
        edtWebPage = findViewById(R.id.edtWebPage);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        btnAttachFiles.setOnClickListener(this);
    }

    public void ViewSetText() {
        String BusinessStart = "<font color='#000000'>Business Start</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtBusinessStart.setText(Html.fromHtml(BusinessStart));

        String BusinessClose = "<font color='#000000'>Business Close</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtBusinessClose.setText(Html.fromHtml(BusinessClose));

        String DayOff = "<font color='#000000'>Day off</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtDayOff.setText(Html.fromHtml(DayOff));

        String BenefitForMeTownMember = "<font color='#000000'>Benefit for MeTown member</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtBenefitForMeTownMember.setText(Html.fromHtml(BenefitForMeTownMember));

        String WebPage = "<font color='#000000'>Web page</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtWebPage.setText(Html.fromHtml(WebPage));

        String BusinessPermits = "<font color='#000000'>Business permits</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtBusinessPermits.setText(Html.fromHtml(BusinessPermits));
    }

    public void GetIntentData() {
        NameOfBusiness = getIntent().getStringExtra("NameOfBusiness");
        CategoryID = getIntent().getStringExtra("CategoryID");
        Address = getIntent().getStringExtra("Address");
        DetailedAddress = getIntent().getStringExtra("DetailedAddress");
        ContactNumber = getIntent().getStringExtra("ContactNumber");
        IntroductionOfBusiness = getIntent().getStringExtra("IntroductionOfBusiness");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtDone:
                BusinessStart = edtBusinessStart.getText().toString().trim();
                BusinessClose = edtBusinessClose.getText().toString().trim();
                DayOff = edtDayOff.getText().toString().trim();
                BenefitForMeTownMember = edtBenefitForMeTownMember.getText().toString().trim();
                WebPage = edtWebPage.getText().toString().trim();
                if (BusinessStart.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Business Start time", Toast.LENGTH_LONG).show();
                } else if (BusinessClose.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Business Close time", Toast.LENGTH_LONG).show();
                } else if (DayOff.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Day off", Toast.LENGTH_LONG).show();
                } else if (BenefitForMeTownMember.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Benefit for MeTown member", Toast.LENGTH_LONG).show();
                } else if (WebPage.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Web page", Toast.LENGTH_LONG).show();
                } else if (bitmap == null) {
                    Toast.makeText(mContext, "Please Attach Your Business permits", Toast.LENGTH_LONG).show();
                } else {
                    RegisterMerchantApi(CategoryID, NameOfBusiness, Address, DetailedAddress, IntroductionOfBusiness, BusinessStart,
                            BusinessClose, DayOff, BenefitForMeTownMember, WebPage, bitmap);
                }
                break;
            case R.id.btnAttachFiles:
                SelectImage();
                break;
        }
    }

    private void SelectImage() {
        String[] str = {"Choose from Gallery", "Open Camera", "Cancel"};
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setItems(str, new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (which == 0) {
                    // gallery
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        REQUEST_CODE = 70;
                    }
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    } else {
                        PhotoGallery();
                    }
                } else if (which == 1) {
                    // camera
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        MY_REQUEST_CODE = 40;
                        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_REQUEST_CODE);
                        } else {
                            PhotoCameraPerm();
                        }
                    } else {
                        PhotoCamera();
                    }
                }
            }
        });
        alert.show();
    }

    private void PhotoGallery() {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 11);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 33);
        }
    }

    private void PhotoCamera() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    private void PhotoCameraPerm() {
        REQUEST_CODE = 50;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                PhotoCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 11:
                if (resultCode == RESULT_OK) {
                    GetPhotoIntentData(data);
                }
                break;
            case 33:
                if (resultCode == RESULT_OK) {
                    GetPhotoIntentData(data);
                } else if (resultCode == SELECT_IMAGE) {
                    GetPhotoIntentData(data);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    onCaptureImageResult(data);
                }
                break;
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = destination.getAbsolutePath();
        if (path.equals("") || path.equals("null") || path.equals(null) || path == null) {
            Toast.makeText(mContext, "Please capture again.", Toast.LENGTH_LONG).show();
        } else {
            try {
                bitmap = Utility.decodeSampledBitmap(mContext, Uri.fromFile(new File(path)));
                Glide.with(mContext).load(bitmap).into(imgPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void GetPhotoIntentData(Intent data) {
        Uri uri = data.getData();
        String path = getRealPathFromURI(uri);
        if (path != null) {
            try {
                bitmap = Utility.decodeSampledBitmap(mContext, Uri.fromFile(new File(path)));
                Glide.with(mContext).load(bitmap).into(imgPhoto);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE || requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == 40) {
                    PhotoCameraPerm();
                } else if (requestCode == 50) {
                    PhotoCamera();
                } else if (requestCode == 70) {
                    PhotoGallery();
                }
            }
        }
    }

    private void RegisterMerchantApi(final String CategoryID, final String NameOfBusiness, final String Address, final String DetailedAddress,
                                     final String IntroductionOfBusiness, final String BusinessStart, final String BusinessClose, final String DayOff,
                                     final String BenefitForMeTownMember, final String WebPage, final Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);
        // our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConstant.getInstance().REGISTER_MERCHANT, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject JsonMain = new JSONObject(new String(response.data));
                    Log.e("RESPONSE", "" + APIConstant.getInstance().REGISTER_MERCHANT + JsonMain);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        photoList = new ArrayList<>();
                        finish();
                    } else {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("error", error.toString());
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                // params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().REGISTER_MERCHANT + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationName = sharedPreferences.getString("LocationName", "");
                String Latitude = sharedPreferences.getString("Latitude", "");
                String Longitude = sharedPreferences.getString("Longitude", "");
                params.put("category_id", CategoryID);
                params.put("name", NameOfBusiness);
                params.put("address", Address);
                params.put("detailed_address", DetailedAddress);
                params.put("description", IntroductionOfBusiness);
                params.put("business_start", BusinessStart);
                params.put("business_end", BusinessClose);
                params.put("day_off", DayOff);
                params.put("benefits", BenefitForMeTownMember);
                params.put("webpage", WebPage);
                params.put("lats", Latitude);
                params.put("longs", Longitude);
                params.put("location_name", LocationName);
                Log.e("PARAMETER", "" + APIConstant.getInstance().REGISTER_MERCHANT + params);
                return params;
            }

            // Form data passing
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long ImageName = System.currentTimeMillis();
                params.put("permit", new DataPart(ImageName + ".png", getFileDataFromDrawable(bitmap)));
                Log.e("PARAMETER Image permit", "" + APIConstant.getInstance().REGISTER_MERCHANT + params);
                return params;
            }

            // Form data passing
            @Override
            protected Map<String, ArrayList<DataPart>> getByteDataArray() {
                Map<String, ArrayList<DataPart>> params = new HashMap<>();
                ArrayList<DataPart> dataPart = new ArrayList<>();
                long ImageName = System.currentTimeMillis();
                for (int i = 0; i < photoList.size(); i++) {
                    VolleyMultipartRequest.DataPart dp = new VolleyMultipartRequest.DataPart(ImageName + ".png", getFileDataFromDrawable(photoList.get(i)));
                    dataPart.add(dp);
                }
                params.put("photos[]", dataPart);
                Log.e("PARAMETER Image photos", "" + APIConstant.getInstance().REGISTER_MERCHANT + params);
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                progressBar.setVisibility(View.GONE);
            }
        });

        //adding the request to volley
        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}