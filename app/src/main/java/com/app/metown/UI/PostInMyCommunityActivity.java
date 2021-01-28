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
import com.app.metown.Adapters.SelectCategoryMainAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.AppConstants.Utility;
import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.Models.TopicKeywordModel;
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

public class PostInMyCommunityActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgPhoto;
    EditText edtDescription;
    TextView txtChooseTopic, txtSelectedTopic, txtChooseKeyword, txtSelectedKeyword, txtPhoto, txtDescription, txtDone;
    LinearLayout SelectTopicLayout, SelectKeywordLayout, RangeSettingLayout;
    RecyclerView SelectCategoryView;
    Dialog dialog;
    String TopicID = "", Topic = "", KeywordID = "", Keyword = "", Description = "", Latitude = "23.112659", Longitude = "72.547752",
            WhereFrom = "";
    ArrayList<TopicKeywordModel> keywordList = new ArrayList<>();
    ArrayList<TopicKeywordModel> topicList = new ArrayList<>();

    String mPath = "";
    private static final int SELECT_IMAGE = 4;
    private int MY_REQUEST_CODE, REQUEST_CODE;
    File photo;
    Bitmap mBitmap;
    private ArrayList<Bitmap> mTempBitmapArray = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_in_my_community);

        Log.e("Activity", "PostInMyCommunityActivity");

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

        txtDone = findViewById(R.id.txtDone);
        txtChooseTopic = findViewById(R.id.txtChooseTopic);
        txtSelectedTopic = findViewById(R.id.txtSelectedTopic);
        txtChooseKeyword = findViewById(R.id.txtChooseKeyword);
        txtSelectedKeyword = findViewById(R.id.txtSelectedKeyword);
        txtPhoto = findViewById(R.id.txtPhoto);
        txtDescription = findViewById(R.id.txtDescription);

        edtDescription = findViewById(R.id.edtDescription);

        SelectTopicLayout = findViewById(R.id.SelectTopicLayout);
        SelectKeywordLayout = findViewById(R.id.SelectKeywordLayout);
        RangeSettingLayout = findViewById(R.id.RangeSettingLayout);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        imgPhoto.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        SelectTopicLayout.setOnClickListener(this);
        SelectKeywordLayout.setOnClickListener(this);
    }

    public void ViewSetText() {
        String ChooseTopic = "<font color='#000000'>Choose topic</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtChooseTopic.setText(Html.fromHtml(ChooseTopic));

        String ChooseKeyword = "<font color='#000000'>Choose Keyword</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtChooseKeyword.setText(Html.fromHtml(ChooseKeyword));

        String Photo = "<font color='#000000'>Photos</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtPhoto.setText(Html.fromHtml(Photo));

        String Description = "<font color='#000000'>Description</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtDescription.setText(Html.fromHtml(Description));
    }

    @SuppressLint("NonConstantResourceId")
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
                Description = edtDescription.getText().toString().trim();
                if (TopicID.equals("")) {
                    Toast.makeText(mContext, "Please Choose Topic for Community.", Toast.LENGTH_LONG).show();
                } else if (Keyword.equals("")) {
                    Toast.makeText(mContext, "Please Choose Keyword for Community.", Toast.LENGTH_LONG).show();
                } else if (mBitmap == null) {
                    Toast.makeText(mContext, "Please Select Photo for Community.", Toast.LENGTH_LONG).show();
                } else if (Description.equals("")) {
                    Toast.makeText(mContext, "Please Enter Description for Community.", Toast.LENGTH_LONG).show();
                } else {
                    AddCommunityApi(TopicID, Keyword, Description, Latitude, Longitude, mBitmap);
                }
                break;
            case R.id.SelectTopicLayout:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_category_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                SelectCategoryView = dialog.findViewById(R.id.SelectCategoryView);
                WhereFrom = "Topic";
                GetTopicApi();
                break;
            case R.id.SelectKeywordLayout:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_category_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                SelectCategoryView = dialog.findViewById(R.id.SelectCategoryView);
                WhereFrom = "Keyword";
                GetKeywordApi();
                break;
            case R.id.RangeSettingLayout:
                Intent SetRange = new Intent(mContext, SetRangeActivity.class);
                startActivity(SetRange);
                break;
        }
    }

    private void GetTopicApi() {
        String req = "req";
        topicList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().GET_TOPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_TOPIC + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                dialog.show();
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    TopicKeywordModel topicKeywordModel = new TopicKeywordModel();
                                    topicKeywordModel.setTopicID(arrayData.getJSONObject(i).getString("id"));
                                    topicKeywordModel.setTopic(arrayData.getJSONObject(i).getString("title"));
                                    topicList.add(topicKeywordModel);
                                }
                                if (topicList.size() > 0) {
                                    SelectCategoryAdapter selectCategoryAdapter = new SelectCategoryAdapter(mContext, topicList);
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
                        } catch (Exception exception) {
                            exception.printStackTrace();
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
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_TOPIC + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_TOPIC);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetKeywordApi() {
        String req = "req";
        keywordList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().GET_KEYWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_KEYWORD + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                dialog.show();
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    TopicKeywordModel topicKeywordModel = new TopicKeywordModel();
                                    topicKeywordModel.setKeywordID(arrayData.getJSONObject(i).getString("id"));
                                    topicKeywordModel.setKeyword(arrayData.getJSONObject(i).getString("keyword"));
                                    keywordList.add(topicKeywordModel);
                                }
                                if (keywordList.size() > 0) {
                                    SelectCategoryAdapter selectCategoryAdapter = new SelectCategoryAdapter(mContext, keywordList);
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
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_KEYWORD + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_KEYWORD);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<TopicKeywordModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtCategoryName;

            MyViewHolder(View view) {
                super(view);

                txtCategoryName = view.findViewById(R.id.txtCategoryName);
            }
        }

        public SelectCategoryAdapter(Context mContext, ArrayList<TopicKeywordModel> arrayList) {
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
            final TopicKeywordModel topicKeywordModel = arrayList.get(position);

            holder.txtCategoryName.setText("  " + topicKeywordModel.getTopic());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*TopicID = "";
                    Topic = "";
                    KeywordID = "";
                    Keyword = "";*/
                    if (WhereFrom.equals("Topic")) {
                        TopicID = topicKeywordModel.getTopicID();
                        Topic = topicKeywordModel.getTopic();
                        txtSelectedTopic.setText(Topic);
                    } else if (WhereFrom.equals("Keyword")) {
                        KeywordID = topicKeywordModel.getKeywordID();
                        Keyword = topicKeywordModel.getKeyword();
                        txtSelectedKeyword.setText(Keyword);
                    }
                    WhereFrom = "";
                    dialog.cancel();
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
                            Glide.with(mContext).load(mBitmap).into(imgPhoto);
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
                            Glide.with(mContext).load(mBitmap).into(imgPhoto);
                        } catch (IOException e) {
                            e.printStackTrace();
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
                            Glide.with(mContext).load(mBitmap).into(imgPhoto);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
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

    public int getOrientation(Uri selectedImage) {
        int orientation = 0;
        final String[] projection = new String[]{MediaStore.Images.Media.ORIENTATION};
        final Cursor cursor = mContext.getContentResolver().query(selectedImage, projection, null, null, null);
        if (cursor != null) {
            final int orientationColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
            if (cursor.moveToFirst()) {
                orientation = cursor.isNull(orientationColumnIndex) ? 0 : cursor.getInt(orientationColumnIndex);
            }
            cursor.close();
        }
        return orientation;
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
            Glide.with(mContext).load(mBitmap).into(imgPhoto);
        } catch (IOException e) {
            e.printStackTrace();
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
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
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

    private void AddCommunityApi(final String TopicID, final String Keyword, final String Description, final String Latitude,
                                 final String Longitude, final Bitmap mBitmap) {
        progressBar.setVisibility(View.VISIBLE);
        // our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConstant.getInstance().ADD_COMMUNITY,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject JsonMain = new JSONObject(new String(response.data));
                            Log.e("RESPONSE ", "" + APIConstant.getInstance().ADD_COMMUNITY + JsonMain);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            String Message = JsonMain.getString("msg");
                            if (HAS_ERROR.equals("false")) {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("error", error.toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                // params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_COMMUNITY + params);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("topic_id", TopicID);
                params.put("keyword", Keyword);
                params.put("description", Description);
                params.put("lats", Latitude);
                params.put("longs", Longitude);
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_COMMUNITY + params);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long ImageName = System.currentTimeMillis();
                params.put("photos", new DataPart(ImageName + ".png", getFileDataFromDrawable(mBitmap)));
                Log.e("PARAMETER Image", "" + APIConstant.getInstance().ADD_COMMUNITY + params);
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
                Log.e("ErrorVolley", error.toString());
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
