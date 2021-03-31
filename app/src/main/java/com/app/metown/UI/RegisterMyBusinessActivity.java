package com.app.metown.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.AppConstants.ConstantFunction;
import com.app.metown.AppConstants.Utility;
import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.TownModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.app.metown.VolleySupport.VolleyMultipartRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterMyBusinessActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgCamera;
    TextView txtTypeOfBusiness, txtSelectedCategory, txtNameOfStore, txtDetailTypeOfBusiness, txtAddress, txtDetailedAddress,
            txtContactNumber, txtPhoto, txtDone, txtSelectedBarangayCategory;
    EditText edtNameOfStore, edtDetailTypeOfBusiness, edtAddress, edtDetailedAddress, edtContactNumber, edtIntroduceYourStore;
    Dialog dialog;
    LinearLayout SelectCategoryLayout;
    RelativeLayout SelectBarangayLayout;
    RecyclerView BarangayCategoryView, TownFromAlphabetView, SelectCategoryView;
    String CategoryType = "1", ParentID = "0", Title = "", BarangayTitle = "", CategoryID = "", BarangayID = "";
    ArrayList<CategoryModel> categoryList = new ArrayList<>();
    ArrayList<TownModel> townList = new ArrayList<>();

    Bitmap bitmap;
    private static final int SELECT_IMAGE = 4;
    private int MY_REQUEST_CODE, REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_my_business);

        Log.e("Activity", "RegisterMyBusinessActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnText();

        ViewSetText();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgCamera = findViewById(R.id.imgCamera);

        txtDone = findViewById(R.id.txtDone);
        txtTypeOfBusiness = findViewById(R.id.txtTypeOfBusiness);
        txtSelectedCategory = findViewById(R.id.txtSelectedCategory);
        txtNameOfStore = findViewById(R.id.txtNameOfStore);
        txtDetailTypeOfBusiness = findViewById(R.id.txtDetailTypeOfBusiness);
        txtAddress = findViewById(R.id.txtAddress);
        txtDetailedAddress = findViewById(R.id.txtDetailedAddress);
        txtContactNumber = findViewById(R.id.txtContactNumber);
        txtPhoto = findViewById(R.id.txtPhoto);
        txtSelectedBarangayCategory = findViewById(R.id.txtSelectedBarangayCategory);

        edtNameOfStore = findViewById(R.id.edtNameOfStore);
        edtDetailTypeOfBusiness = findViewById(R.id.edtDetailTypeOfBusiness);
        edtAddress = findViewById(R.id.edtAddress);
        edtDetailedAddress = findViewById(R.id.edtDetailedAddress);
        edtContactNumber = findViewById(R.id.edtContactNumber);
        edtIntroduceYourStore = findViewById(R.id.edtIntroduceYourStore);

        SelectCategoryLayout = findViewById(R.id.SelectCategoryLayout);
        SelectBarangayLayout = findViewById(R.id.SelectBarangayLayout);
    }

    public void ViewOnText() {
        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        SelectCategoryLayout.setOnClickListener(this);
        SelectBarangayLayout.setOnClickListener(this);
        imgCamera.setOnClickListener(this);
    }

    public void ViewSetText() {
        String TypeOfBusiness = "<font color='#000000'>Type of business</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtTypeOfBusiness.setText(Html.fromHtml(TypeOfBusiness));

        String NameOfStore = "<font color='#000000'>Name of store</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtNameOfStore.setText(Html.fromHtml(NameOfStore));

        String DetailTypeOfBusiness = "<font color='#000000'>Detail type of business</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtDetailTypeOfBusiness.setText(Html.fromHtml(DetailTypeOfBusiness));

        String Address = "<font color='#000000'>Address</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtAddress.setText(Html.fromHtml(Address));

        String DetailedAddress = "<font color='#000000'>Detailed Address</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtDetailedAddress.setText(Html.fromHtml(DetailedAddress));

        String ContactNumber = "<font color='#000000'>Contact number</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtContactNumber.setText(Html.fromHtml(ContactNumber));

        String Photo = "<font color='#000000'>Photos</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtPhoto.setText(Html.fromHtml(Photo));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgCamera:
                SelectImage();
                break;
            case R.id.SelectCategoryLayout:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_category_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                SelectCategoryView = dialog.findViewById(R.id.SelectCategoryView);
                GetCategoryApi(CategoryType, ParentID);
                break;
            case R.id.SelectBarangayLayout:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_barangay_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                BarangayCategoryView = dialog.findViewById(R.id.BarangayCategoryView);
                TownFromAlphabetView = dialog.findViewById(R.id.TownFromAlphabetView);
                GetBarangayApi(CategoryType, ParentID);
                dialog.findViewById(R.id.UnSelectBarangayLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                break;
            case R.id.txtDone:
                String NameOfStore = edtNameOfStore.getText().toString().trim();
                String DetailTypeOfBusiness = edtDetailTypeOfBusiness.getText().toString().trim();
                String Address = edtAddress.getText().toString().trim();
                String DetailedAddress = edtDetailedAddress.getText().toString().trim();
                String ContactNumber = edtContactNumber.getText().toString().trim();
                String IntroduceYourStore = edtIntroduceYourStore.getText().toString().trim();
                if (CategoryID.equals("")) {
                    Toast.makeText(mContext, "Please Select Your Business Category.", Toast.LENGTH_LONG).show();
                } else if (NameOfStore.equals("")) {
                    Toast.makeText(mContext, "Please Choose Your Business Image.", Toast.LENGTH_LONG).show();
                } else if (DetailTypeOfBusiness.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Detail Type Of Business.", Toast.LENGTH_LONG).show();
                } else if (Address.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Address Of Business.", Toast.LENGTH_LONG).show();
                } else if (BarangayID.equals("")) {
                    Toast.makeText(mContext, "Please Select Your Barangay.", Toast.LENGTH_LONG).show();
                } else if (DetailedAddress.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Detailed Address Of Business.", Toast.LENGTH_LONG).show();
                } else if (ContactNumber.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Contact Number Of Business.", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidMobile(ContactNumber)) {
                    Toast.makeText(mContext, "Please Enter Your Valid Contact Number.", Toast.LENGTH_LONG).show();
                } else if (bitmap == null) {
                    Toast.makeText(mContext, "Please Select Image of your Business.", Toast.LENGTH_LONG).show();
                } else if (IntroduceYourStore.equals("")) {
                    Toast.makeText(mContext, "Please Introduce Your Store.", Toast.LENGTH_LONG).show();
                } else {
                    RegisterBusinessApi(CategoryID, NameOfStore, DetailTypeOfBusiness, Address, BarangayID, DetailedAddress, ContactNumber, IntroduceYourStore, bitmap);
                }
                break;
        }
    }

    private void GetCategoryApi(final String CategoryType, final String ParentID) {
        String req = "req";
        categoryList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_CATEGORY + response);
                            dialog.show();
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equals("false")) {
                                JSONObject objectData = JsonMain.getJSONObject("data");
                                JSONArray arrayCategoryList = objectData.getJSONArray("category_list");
                                for (int i = 0; i < arrayCategoryList.length(); i++) {
                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setCategoryID(arrayCategoryList.getJSONObject(i).getString("id"));
                                    categoryModel.setCategoryTitle(arrayCategoryList.getJSONObject(i).getString("category_title"));
                                    categoryList.add(categoryModel);
                                }
                                if (categoryList.size() > 0) {
                                    SelectCategoryAdapter selectCategoryAdapter = new SelectCategoryAdapter(mContext, categoryList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    SelectCategoryView.setLayoutManager(mLayoutManager);
                                    SelectCategoryView.setItemAnimator(new DefaultItemAnimator());
                                    SelectCategoryView.setAdapter(selectCategoryAdapter);
                                    selectCategoryAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"category_type\":\"" + CategoryType + "\",\"parent_id\":\"" + ParentID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_CATEGORY + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_CATEGORY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtCategoryName;

            MyViewHolder(View view) {
                super(view);

                txtCategoryName = view.findViewById(R.id.txtCategoryName);
            }
        }

        public SelectCategoryAdapter(Context mContext, ArrayList<CategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }


        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_category_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final CategoryModel categoryModel = arrayList.get(position);

            holder.txtCategoryName.setText("  " + categoryModel.getCategoryTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CategoryID = categoryModel.getCategoryID();
                    Title = categoryModel.getCategoryTitle();
                    dialog.cancel();
                    txtSelectedCategory.setText(Title);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void GetBarangayApi(final String CategoryType, final String ParentID) {
        String req = "req";
        categoryList.clear();
        townList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_BARANGAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            dialog.show();
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_BARANGAY + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONObject objectData = JsonMain.getJSONObject("data");
                                JSONArray arrayCategoryList = objectData.getJSONArray("category_list");
                                for (int i = 0; i < arrayCategoryList.length(); i++) {
                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setCategoryID(arrayCategoryList.getJSONObject(i).getString("id"));
                                    categoryModel.setCategoryName(arrayCategoryList.getJSONObject(i).getString("name"));
                                    categoryList.add(categoryModel);
                                }
                                if (categoryList.size() > 0) {
                                    SelectBarangayKeywordAdapter selectBarangayKeywordAdapter = new SelectBarangayKeywordAdapter(mContext, categoryList);
                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
                                    BarangayCategoryView.setLayoutManager(mLayoutManager);
                                    BarangayCategoryView.setItemAnimator(new DefaultItemAnimator());
                                    BarangayCategoryView.setAdapter(selectBarangayKeywordAdapter);
                                    selectBarangayKeywordAdapter.notifyDataSetChanged();
                                }

                                JSONArray arrayAllTownList = objectData.getJSONArray("all_town_list");
                                for (int i = 0; i < arrayAllTownList.length(); i++) {
                                    TownModel townModel = new TownModel();
                                    townModel.setAlphabet(arrayAllTownList.getJSONObject(i).getString("alphabet"));
                                    townModel.setTownList(arrayAllTownList.getJSONObject(i).getJSONArray("town_list"));
                                    townList.add(townModel);
                                }
                                if (townList.size() > 0) {
                                    TownFromAlphabetAdapter townFromAlphabetAdapter = new TownFromAlphabetAdapter(mContext, townList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    TownFromAlphabetView.setLayoutManager(mLayoutManager);
                                    TownFromAlphabetView.setItemAnimator(new DefaultItemAnimator());
                                    TownFromAlphabetView.setAdapter(townFromAlphabetAdapter);
                                    townFromAlphabetAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
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
                // params.put("Content-Type", "application/json");
                // params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_BARANGAY + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"category_type\":\"" + CategoryType + "\",\"parent_id\":\"" + ParentID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_BARANGAY + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_BARANGAY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class SelectBarangayKeywordAdapter extends RecyclerView.Adapter<SelectBarangayKeywordAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtCategoryName;

            MyViewHolder(View view) {
                super(view);

                txtCategoryName = view.findViewById(R.id.txtCategoryName);
            }
        }

        public SelectBarangayKeywordAdapter(Context mContext, ArrayList<CategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_barangay_keyword_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final CategoryModel categoryModel = arrayList.get(position);

            holder.txtCategoryName.setText(categoryModel.getCategoryName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BarangayID = "";
                    BarangayTitle = categoryModel.getCategoryName();
                    BarangayID = categoryModel.getCategoryID();
                    dialog.cancel();
                    txtSelectedBarangayCategory.setText(BarangayTitle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class TownFromAlphabetAdapter extends RecyclerView.Adapter<TownFromAlphabetAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<TownModel> arrayList;
        ArrayList<TownModel> townFromSpecificAlphabetList = new ArrayList<>();

        static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtAlphabet;
            RecyclerView TownFromSpecificAlphabetView;

            MyViewHolder(View view) {
                super(view);

                txtAlphabet = view.findViewById(R.id.txtAlphabet);

                TownFromSpecificAlphabetView = view.findViewById(R.id.TownFromSpecificAlphabetView);
            }
        }

        public TownFromAlphabetAdapter(Context mContext, ArrayList<TownModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.town_from_alphabet_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            TownModel townModel = arrayList.get(position);

            holder.txtAlphabet.setText(townModel.getAlphabet());

            JSONArray arrayTownList = townModel.getTownList();
            // Log.e("arrayTownList ","" + arrayTownList);
            try {
                townFromSpecificAlphabetList.clear();
                for (int i = 0; i < arrayTownList.length(); i++) {
                    TownModel model = new TownModel();
                    model.setTownID(arrayTownList.getJSONObject(i).getString("id"));
                    model.setTownName(arrayTownList.getJSONObject(i).getString("name"));
                    townFromSpecificAlphabetList.add(model);
                }
                if (townFromSpecificAlphabetList.size() > 0) {
                    TownFromSpecificAlphabetAdapter townFromSpecificAlphabetAdapter = new TownFromSpecificAlphabetAdapter(mContext, townFromSpecificAlphabetList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    holder.TownFromSpecificAlphabetView.setLayoutManager(mLayoutManager);
                    holder.TownFromSpecificAlphabetView.setItemAnimator(new DefaultItemAnimator());
                    holder.TownFromSpecificAlphabetView.setAdapter(townFromSpecificAlphabetAdapter);
                    townFromSpecificAlphabetAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class TownFromSpecificAlphabetAdapter extends RecyclerView.Adapter<TownFromSpecificAlphabetAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<TownModel> arrayList;

        static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtTown;

            MyViewHolder(View view) {
                super(view);

                txtTown = view.findViewById(R.id.txtTown);
            }
        }

        public TownFromSpecificAlphabetAdapter(Context mContext, ArrayList<TownModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.town_from_specific_alphabet_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            TownModel townModel = arrayList.get(position);

            holder.txtTown.setText(townModel.getTownName());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
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
                Glide.with(mContext).load(bitmap).into(imgCamera);
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
                Glide.with(mContext).load(bitmap).into(imgCamera);
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

    private void RegisterBusinessApi(final String CategoryID, final String Name, final String Details, final String Address, final String BarangayID,
                                     final String DetailedAddress, final String PhoneNumber, final String Description, final Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);
        // our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConstant.getInstance().REGISTER_BUSINESS, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject JsonMain = new JSONObject(new String(response.data));
                    Log.e("RESPONSE ", "" + APIConstant.getInstance().REGISTER_BUSINESS + JsonMain.toString());
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().REGISTER_BUSINESS + params);
                return params;
            }

            // Form data passing
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category_id", CategoryID);
                params.put("name", Name);
                params.put("details", Details);
                params.put("address", Address);
                params.put("barangay_id", BarangayID);
                params.put("detailed_address", DetailedAddress);
                params.put("phone_number", PhoneNumber);
                params.put("description", Description);
                Log.e("PARAMETER", "" + APIConstant.getInstance().REGISTER_BUSINESS + params);
                Log.e("param", params.toString());
                return params;
            }

            // Form data passing
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long ImageName = System.currentTimeMillis();
                params.put("photos[]", new DataPart(ImageName + ".png", getFileDataFromDrawable(bitmap)));
                Log.e("PARAMETER Image", "" + APIConstant.getInstance().REGISTER_BUSINESS + params);
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