package com.app.metown.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.app.metown.Adapters.PhotoAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.AppConstants.Utility;
import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.PhraseModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.app.metown.VolleySupport.VolleyMultipartRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostForSaleActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    TextView txtPhoto, txtTitle, txtCategories, txtSelectedCategory, txtPrice, txtDescription, txtImageCount, txtDone;
    ImageView imgBack, imgPhoto, imgPhrase;
    EditText edtTitle, edtPrice, edtDescription, edtEnterPhrase;
    RelativeLayout SelectCategoryLayout;
    LinearLayout RangeSettingLayout;
    RecyclerView PhraseView, SelectCategoryView, PhotoView;
    ArrayList<PhraseModel> phraseList = new ArrayList<>();
    ArrayList<CategoryModel> categoryList = new ArrayList<>();
    Dialog dialog;
    RadioButton rbtNegotiable;
    String CategoryType = "1", ParentID = "0", Title = "", CategoryID = "", Price = "", Negotiable = "0", Description = "", UserRange = "";
    private static final int SELECT_IMAGE = 4;
    private int MY_REQUEST_CODE, REQUEST_CODE;
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> pathList = new ArrayList<>();
    ArrayList<Bitmap> photoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_for_sale);

        Log.e("Activity", "PostForSaleActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        ViewSetText();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgPhoto = findViewById(R.id.imgPhoto);
        imgPhrase = findViewById(R.id.imgPhrase);

        edtTitle = findViewById(R.id.edtTitle);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);

        txtDone = findViewById(R.id.txtDone);
        txtPhoto = findViewById(R.id.txtPhoto);
        txtImageCount = findViewById(R.id.txtImageCount);
        txtTitle = findViewById(R.id.txtTitle);
        txtCategories = findViewById(R.id.txtCategories);
        txtSelectedCategory = findViewById(R.id.txtSelectedCategory);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDescription);

        SelectCategoryLayout = findViewById(R.id.SelectCategoryLayout);
        RangeSettingLayout = findViewById(R.id.RangeSettingLayout);

        rbtNegotiable = findViewById(R.id.rbtNegotiable);

        PhotoView = findViewById(R.id.PhotoView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        imgPhoto.setOnClickListener(this);
        imgPhrase.setOnClickListener(this);
        SelectCategoryLayout.setOnClickListener(this);
        RangeSettingLayout.setOnClickListener(this);

        rbtNegotiable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setSelected(!buttonView.isSelected());
                Negotiable = "1";
                Drawable img;
                if (isChecked) {
                    img = mContext.getResources().getDrawable(R.drawable.negotiable_selected);
                } else {
                    img = mContext.getResources().getDrawable(R.drawable.negotiable_unselected);
                }
                img.setBounds(0, 0, 50, 50);
                buttonView.setCompoundDrawables(null, null, img, null);
            }
        });
    }

    public void ViewSetText() {
        String Photo = "<font color='#000000'>Photos</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtPhoto.setText(Html.fromHtml(Photo));

        String Title = "<font color='#000000'>Title</font>" + " " + "<font color='#FFCE5D'><small>● : </small></font>";
        txtTitle.setText(Html.fromHtml(Title));

        String Categories = "<font color='#000000'>Categories</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtCategories.setText(Html.fromHtml(Categories));

        String Price = "<font color='#000000'>Price</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtPrice.setText(Html.fromHtml(Price));

        String Description = "<font color='#000000'>Description</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtDescription.setText(Html.fromHtml(Description));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgPhoto:
                SelectImage();
                break;
            case R.id.txtDone:
                Title = edtTitle.getText().toString().trim();
                Price = edtPrice.getText().toString().trim();
                Description = edtDescription.getText().toString().trim();
                UserRange = RangeSettingActivity.UserRange;
                if (photoList.size() == 0) {
                    Toast.makeText(mContext, "Please Choose Your Business Image.", Toast.LENGTH_LONG).show();
                } else if (Title.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Business Title.", Toast.LENGTH_LONG).show();
                } else if (CategoryID.equals("")) {
                    Toast.makeText(mContext, "Please Select Your Business Category.", Toast.LENGTH_LONG).show();
                } else if (Price.equals("")) {
                    Toast.makeText(mContext, "Please Select Your Business Price.", Toast.LENGTH_LONG).show();
                } else if (Description.equals("")) {
                    Toast.makeText(mContext, "Please Select Your Business Description.", Toast.LENGTH_LONG).show();
                } else if (UserRange.equals("")) {
                    Toast.makeText(mContext, "Please Select Your Range.", Toast.LENGTH_LONG).show();
                } else {
                    PostSaleApi(Title, CategoryID, Price, Negotiable, Description, UserRange);
                }
                break;
            case R.id.imgPhrase:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.phrase_choose_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                PhraseView = dialog.findViewById(R.id.PhraseView);
                edtEnterPhrase = dialog.findViewById(R.id.edtEnterPhrase);
                GetPhrasesApi();
                dialog.findViewById(R.id.txtAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Phrase = edtEnterPhrase.getText().toString().trim();
                        if (Phrase.equals("")) {
                            Toast.makeText(mContext, "Please Enter a Phrase", Toast.LENGTH_LONG).show();
                        } else {
                            SetPhrasesApi(Phrase);
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.SelectCategoryLayout:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_category_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                SelectCategoryView = dialog.findViewById(R.id.SelectCategoryView);
                GetCategoryApi(CategoryType, ParentID);
                break;
            case R.id.RangeSettingLayout:
                Intent RangeSetting = new Intent(mContext, RangeSettingActivity.class);
                startActivity(RangeSetting);
                break;
        }
    }

    private void GetCategoryApi(final String CategoryType, final String ParentID) {
        String req = "req";
        categoryList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_CATEGORY + response);
                    dialog.show();
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
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
        }, new Response.ErrorListener() {
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

    private void GetPhrasesApi() {
        String req = "req";
        phraseList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().GET_PHRASES, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_PHRASES + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            PhraseModel phraseModel = new PhraseModel();
                            phraseModel.setPhraseID(arrayData.getJSONObject(i).getString("id"));
                            phraseModel.setPhraseName(arrayData.getJSONObject(i).getString("name"));
                            phraseList.add(phraseModel);
                        }
                        if (phraseList.size() > 0) {
                            PhraseAdapter phraseAdapter = new PhraseAdapter(mContext, phraseList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            PhraseView.setLayoutManager(mLayoutManager);
                            PhraseView.setItemAnimator(new DefaultItemAnimator());
                            PhraseView.setAdapter(phraseAdapter);
                            phraseAdapter.notifyDataSetChanged();
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_PHRASES + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_PHRASES);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void SetPhrasesApi(final String Name) {
        String req = "req";
        phraseList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SET_PHRASES, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().SET_PHRASES + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        String SuccessMessage = JsonMain.getString("msg");
                        edtEnterPhrase.setText("");
                        Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                        GetPhrasesApi();
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
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
                Log.e("HEADER", "" + APIConstant.getInstance().SET_PHRASES + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"name\":\"" + Name + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().SET_PHRASES + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SET_PHRASES);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<PhraseModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtPhraseName;
            ImageView imgClose;

            MyViewHolder(View view) {
                super(view);

                txtPhraseName = view.findViewById(R.id.txtPhraseName);

                imgClose = view.findViewById(R.id.imgClose);
            }
        }

        public PhraseAdapter(Context mContext, ArrayList<PhraseModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phrase_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final PhraseModel phraseModel = arrayList.get(position);

            holder.txtPhraseName.setText(phraseModel.getPhraseName());

            holder.imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeletePhrasesApi(phraseModel.getPhraseID());
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void DeletePhrasesApi(final String ID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().DELETE_PHRASES + ID, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().DELETE_PHRASES + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        String SuccessMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                        GetPhrasesApi();
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
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
                Log.e("HEADER", "" + APIConstant.getInstance().DELETE_PHRASES + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().DELETE_PHRASES);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 11);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, 33);
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
                Bitmap bitmap = Utility.decodeSampledBitmap(mContext, Uri.fromFile(new File(path)));
                photoList.add(bitmap);
                if (photoList.size() > 0) {
                    SetPhotoAdapter();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void GetPhotoIntentData(Intent data) {
        uriList.clear();
        pathList.clear();
        photoList.clear();
        if (data.getClipData().getItemCount() > 10) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.imgPhoto), "You can not select more than 10 images.", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                REQUEST_CODE = 70;
                            }
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                            } else {
                                PhotoGallery();
                            }
                        }
                    });
            snackbar.setActionTextColor(Color.WHITE);
            View view = snackbar.getView();
            TextView textView = view.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            txtImageCount.setText(data.getClipData().getItemCount() + "/10");
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    uriList.add(uri);
                    String path = getRealPathFromURI(uri);
                    pathList.add(path);
                    try {
                        Bitmap bitmap = Utility.decodeSampledBitmap(mContext, Uri.fromFile(new File(path)));
                        photoList.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                uriList.add(uri);
                String path = getRealPathFromURI(uri);
                pathList.add(path);
                try {
                    Bitmap bitmap = Utility.decodeSampledBitmap(mContext, Uri.fromFile(new File(path)));
                    photoList.add(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (photoList.size() > 0) {
                SetPhotoAdapter();
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

    public void SetPhotoAdapter() {
        PhotoAdapter photoAdapter = new PhotoAdapter(mContext, photoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        PhotoView.setLayoutManager(mLayoutManager);
        PhotoView.setItemAnimator(new DefaultItemAnimator());
        PhotoView.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
    }

    private void PostSaleApi(final String Title, final String CategoryID, final String Price, final String Negotiable, final String Description, final String UserRange) {
        progressBar.setVisibility(View.VISIBLE);
        // our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConstant.getInstance().POST_SALE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject JsonMain = new JSONObject(new String(response.data));
                    Log.e("RESPONSE ", "" + APIConstant.getInstance().POST_SALE + JsonMain);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                        RangeSettingActivity.UserRange = "";
                        finish();
                    } else {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().POST_SALE + params);
                return params;
            }

            // Form data passing
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Latitude = sharedPreferences.getString("Latitude", "");
                String Longitude = sharedPreferences.getString("Longitude", "");
                String LocationName = sharedPreferences.getString("LocationName", "");
                String LocationAddress = sharedPreferences.getString("LocationName", "");
                params.put("title", Title);
                params.put("category_id", CategoryID);
                params.put("price", Price);
                params.put("negotiable", Negotiable);
                params.put("description", Description);
                params.put("lats", Latitude);
                params.put("longs", Longitude);
                params.put("locationname", LocationName);
                params.put("user_range", UserRange);
                params.put("address", LocationAddress);
                Log.e("PARAMETER", "" + APIConstant.getInstance().POST_SALE + params);
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
                Log.e("PARAMETER Image", "" + APIConstant.getInstance().POST_SALE + params);
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