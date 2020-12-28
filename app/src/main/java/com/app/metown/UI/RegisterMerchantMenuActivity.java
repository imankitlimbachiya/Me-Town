package com.app.metown.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.AppConstants.ConstantFunction;
import com.app.metown.AppConstants.Utility;
import com.app.metown.Models.CategoryModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.app.metown.VolleySupport.VolleyMultipartRequest;
import com.bumptech.glide.Glide;

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

public class RegisterMerchantMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgPhoto;
    TextView txtDone, txtNameOfBusiness, txtTypeOfBusiness, txtSelectedCategory;
    EditText edtNameOfBusiness, edtAddress, edtDetailedAddress, edtContactNumber, edtIntroductionOfBusiness;
    LinearLayout SelectBusinessLayout;

    Dialog dialog;
    RecyclerView SelectCategoryView;
    ArrayList<CategoryModel> categoryList = new ArrayList<>();
    String CategoryType = "1", ParentID = "0", Title = "", CategoryID = "";

    private ArrayList<Bitmap> mTempBitmapArray = new ArrayList<Bitmap>();
    private int MY_REQUEST_CODE, REQUEST_CODE;
    private static final int SELECT_IMAGE = 4;
    String mPath = "";
    File photo;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_merchant_menu);

        Log.e("Activity", "RegisterMerchantMenuActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewSetText();

        ViewOnClick();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgPhoto = findViewById(R.id.imgPhoto);

        txtDone = findViewById(R.id.txtDone);
        txtTypeOfBusiness = findViewById(R.id.txtTypeOfBusiness);
        txtNameOfBusiness = findViewById(R.id.txtNameOfBusiness);
        txtSelectedCategory = findViewById(R.id.txtSelectedCategory);

        SelectBusinessLayout = findViewById(R.id.SelectBusinessLayout);

        edtNameOfBusiness = findViewById(R.id.edtNameOfBusiness);
        edtAddress = findViewById(R.id.edtAddress);
        edtDetailedAddress = findViewById(R.id.edtDetailedAddress);
        edtContactNumber = findViewById(R.id.edtContactNumber);
        edtIntroductionOfBusiness = findViewById(R.id.edtIntroductionOfBusiness);
    }

    public void ViewSetText() {
        String NameOfBusiness = "<font color='#000000'>Name of business</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtNameOfBusiness.setText(Html.fromHtml(NameOfBusiness));

        String TypeOfBusiness = "<font color='#000000'>Type of business</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtTypeOfBusiness.setText(Html.fromHtml(TypeOfBusiness));
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        SelectBusinessLayout.setOnClickListener(this);
        imgPhoto.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtDone:
                String NameOfBusiness = edtNameOfBusiness.getText().toString().trim();
                String Address = edtAddress.getText().toString().trim();
                String DetailedAddress = edtDetailedAddress.getText().toString().trim();
                String ContactNumber = edtContactNumber.getText().toString().trim();
                String IntroductionOfBusiness = edtIntroductionOfBusiness.getText().toString().trim();
                if (NameOfBusiness.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Name Of business", Toast.LENGTH_LONG).show();
                } else if (CategoryID.equals("")) {
                    Toast.makeText(mContext, "Please Select Your Type of business", Toast.LENGTH_LONG).show();
                } else if (Address.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Address", Toast.LENGTH_LONG).show();
                } else if (DetailedAddress.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Detailed address", Toast.LENGTH_LONG).show();
                } else if (ContactNumber.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Contact number", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidMobile(ContactNumber)) {
                    Toast.makeText(mContext, "Please Enter Your Valid Contact number", Toast.LENGTH_LONG).show();
                } else if (mBitmap == null) {
                    Toast.makeText(mContext, "Please Select Image of Your business", Toast.LENGTH_LONG).show();
                } else if (IntroductionOfBusiness.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Introduction of business", Toast.LENGTH_LONG).show();
                } else {
                    Intent AdditionalInfo = new Intent(mContext, AdditionalInfoActivity.class);
                    AdditionalInfo.putExtra("NameOfBusiness", NameOfBusiness);
                    AdditionalInfo.putExtra("CategoryID", CategoryID);
                    AdditionalInfo.putExtra("Address", Address);
                    AdditionalInfo.putExtra("DetailedAddress", DetailedAddress);
                    AdditionalInfo.putExtra("ContactNumber", ContactNumber);
                    AdditionalInfo.putExtra("mBitmap", mBitmap);
                    AdditionalInfo.putExtra("IntroductionOfBusiness", IntroductionOfBusiness);
                    startActivity(AdditionalInfo);
                    finish();
                }
                break;
            case R.id.SelectBusinessLayout:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_category_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                SelectCategoryView = dialog.findViewById(R.id.SelectCategoryView);
                GetCategoryApi(CategoryType, ParentID);
                dialog.show();
                break;
            case R.id.imgPhoto:
                SelectImage();
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                // params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_PHRASES + params);
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

    private void SelectImage() {
        String[] str = {"Choose from gallery", "Open Camera", "cancel"};
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
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                        } else {
                            PhotoGallery();
                        }
                    } else {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                        } else {
                            PhotoGallery();
                        }
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

    @SuppressLint("NewApi")
    private void PhotoCameraPerm() {
        REQUEST_CODE = 50;
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            PhotoCamera();
        } else {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    private void PhotoGallery() {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 11);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/jpeg");
            startActivityForResult(galleryIntent, 33);
        }
    }

    private void PhotoCamera() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 11:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String path = getPath(mContext, selectedImage);
                    if (path != null) {
                        mPath = path;
                        try {
                            mBitmap = Utility.decodeSampledBitmap(mContext, Uri.fromFile(new File(mPath)));
                            ExifInterface exif = new ExifInterface(photo.toString());
                            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")) {
                                mBitmap = rotate(mBitmap, 90);
                            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
                                mBitmap = rotate(mBitmap, 270);
                            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
                                mBitmap = rotate(mBitmap, 180);
                            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")) {
                                mBitmap = rotate(mBitmap, 90);
                            }

                            if (mBitmap != null) {
                                Glide.with(mContext).load(mBitmap).into(imgPhoto);
                                /*if (ConstantFunction.isNetworkAvailable(mContext)) {
                                    NickName = edtNickName.getText().toString().trim();
                                    Email = edtEmail.getText().toString().trim();
                                    MobileNumber = edtPhoneNumber.getText().toString().trim();
                                    if (NickName.equals("")) {
                                        Toast.makeText(mContext, "Please Enter Your Nick Name", Toast.LENGTH_LONG).show();
                                    } else if (Email.equals("")) {
                                        Toast.makeText(mContext, "Please Enter Your Email", Toast.LENGTH_LONG).show();
                                    } else if (!ConstantFunction.isValidEmail(Email)) {
                                        Toast.makeText(mContext, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                                    } else if (MobileNumber.equals("")) {
                                        Toast.makeText(mContext, "Please Enter Your MobileNumber", Toast.LENGTH_LONG).show();
                                    } else if (!ConstantFunction.isValidMobile(MobileNumber)) {
                                        Toast.makeText(mContext, "Please Enter Your Valid MobileNumber", Toast.LENGTH_LONG).show();
                                    } else {
                                        ProfileImageUpload(NickName, Email, MobileNumber, mBitmap);
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
                                }*/
                            } else {
                                Toast.makeText(mContext, "Please Choose Again...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
                break;
            case 33:
                if (resultCode == RESULT_OK) {
                    mTempBitmapArray.clear();
                    mPath = "";
                    Uri selectedImageUri = data.getData();
                    try {
                        mPath = getImagePath(selectedImageUri);
                        try {
                            mBitmap = Utility.decodeSampledBitmap(mContext, Uri.fromFile(new File(mPath)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (mBitmap != null) {
                            Glide.with(mContext).load(mBitmap).into(imgPhoto);
                                /*if (ConstantFunction.isNetworkAvailable(mContext)) {
                                    NickName = edtNickName.getText().toString().trim();
                                    Email = edtEmail.getText().toString().trim();
                                    MobileNumber = edtPhoneNumber.getText().toString().trim();
                                    if (NickName.equals("")) {
                                        Toast.makeText(mContext, "Please Enter Your Nick Name", Toast.LENGTH_LONG).show();
                                    } else if (Email.equals("")) {
                                        Toast.makeText(mContext, "Please Enter Your Email", Toast.LENGTH_LONG).show();
                                    } else if (!ConstantFunction.isValidEmail(Email)) {
                                        Toast.makeText(mContext, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                                    } else if (MobileNumber.equals("")) {
                                        Toast.makeText(mContext, "Please Enter Your MobileNumber", Toast.LENGTH_LONG).show();
                                    } else if (!ConstantFunction.isValidMobile(MobileNumber)) {
                                        Toast.makeText(mContext, "Please Enter Your Valid MobileNumber", Toast.LENGTH_LONG).show();
                                    } else {
                                        ProfileImageUpload(NickName, Email, MobileNumber, mBitmap);
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
                                }*/
                        } else {
                            Toast.makeText(mContext, "Please Choose Again...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else if (resultCode == SELECT_IMAGE) {
                    Uri selectedImage = data.getData();
                    String path = getPath(mContext, selectedImage);
                    if (path != null) {
                        mPath = path;
                        try {
                            mBitmap = Utility.decodeSampledBitmap(mContext, Uri.fromFile(new File(mPath)));
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        if (mBitmap != null) {
                            Glide.with(mContext).load(mBitmap).into(imgPhoto);
                                /*if (ConstantFunction.isNetworkAvailable(mContext)) {
                                    NickName = edtNickName.getText().toString().trim();
                                    Email = edtEmail.getText().toString().trim();
                                    MobileNumber = edtPhoneNumber.getText().toString().trim();
                                    if (NickName.equals("")) {
                                        Toast.makeText(mContext, "Please Enter Your Nick Name", Toast.LENGTH_LONG).show();
                                    } else if (Email.equals("")) {
                                        Toast.makeText(mContext, "Please Enter Your Email", Toast.LENGTH_LONG).show();
                                    } else if (!ConstantFunction.isValidEmail(Email)) {
                                        Toast.makeText(mContext, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                                    } else if (MobileNumber.equals("")) {
                                        Toast.makeText(mContext, "Please Enter Your MobileNumber", Toast.LENGTH_LONG).show();
                                    } else if (!ConstantFunction.isValidMobile(MobileNumber)) {
                                        Toast.makeText(mContext, "Please Enter Your Valid MobileNumber", Toast.LENGTH_LONG).show();
                                    } else {
                                        ProfileImageUpload(NickName, Email, MobileNumber, mBitmap);
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
                                }*/
                        } else {
                            Toast.makeText(mContext, "Please Choose Again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    mTempBitmapArray.clear();
                    onCaptureImageResult(data);
                }
                break;
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        mPath = "";
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
        mPath = destination.getAbsolutePath();
        try {
            mBitmap = Utility.decodeSampledBitmap(mContext, Uri.fromFile(new File(mPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mBitmap != null) {
            Glide.with(mContext).load(mBitmap).into(imgPhoto);
            /*if (ConstantFunction.isNetworkAvailable(mContext)) {
                NickName = edtNickName.getText().toString().trim();
                Email = edtEmail.getText().toString().trim();
                MobileNumber = edtPhoneNumber.getText().toString().trim();
                if (NickName.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Nick Name", Toast.LENGTH_LONG).show();
                } else if (Email.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Email", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidEmail(Email)) {
                    Toast.makeText(mContext, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                } else if (MobileNumber.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your MobileNumber", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidMobile(MobileNumber)) {
                    Toast.makeText(mContext, "Please Enter Your Valid MobileNumber", Toast.LENGTH_LONG).show();
                } else {
                    ProfileImageUpload(NickName, Email, MobileNumber, mBitmap);
                }
            } else {
                Toast.makeText(mContext, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
            }*/
        } else {
            Toast.makeText(mContext, "Please Choose Again...", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)

        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private String getImagePath(Uri selectedImageUri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(selectedImageUri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = mContext.getContentResolver().query(selectedImageUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(selectedImageUri.getScheme())) {
            //return selectedImageUri.getImagePath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
