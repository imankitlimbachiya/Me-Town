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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.app.metown.Adapters.PhotoAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.AppConstants.Utility;
import com.app.metown.Models.TopicKeywordModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.app.metown.VolleySupport.VolleyMultipartRequest;
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

public class PostInMyCommunityActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgPhoto;
    EditText edtDescription;
    TextView txtChooseTopic, txtSelectedTopic, txtChooseKeyword, txtSelectedKeyword, txtPhoto, txtDescription, txtDone, txtImageCount;
    LinearLayout SelectTopicLayout, SelectKeywordLayout, RangeSettingLayout;
    RecyclerView SelectCategoryView, PhotoView;
    Dialog dialog;
    String TopicID = "", Topic = "", KeywordID = "", Keyword = "", Description = "", WhereFrom = "", UserRange = "";
    ArrayList<TopicKeywordModel> keywordList = new ArrayList<>();
    ArrayList<TopicKeywordModel> topicList = new ArrayList<>();
    private static final int SELECT_IMAGE = 4;
    private int MY_REQUEST_CODE, REQUEST_CODE;
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> pathList = new ArrayList<>();
    ArrayList<Bitmap> photoList = new ArrayList<>();

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
        txtImageCount = findViewById(R.id.txtImageCount);

        edtDescription = findViewById(R.id.edtDescription);

        SelectTopicLayout = findViewById(R.id.SelectTopicLayout);
        SelectKeywordLayout = findViewById(R.id.SelectKeywordLayout);
        RangeSettingLayout = findViewById(R.id.RangeSettingLayout);

        PhotoView = findViewById(R.id.PhotoView);
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
                UserRange = RangeSettingActivity.UserRange;
                if (TopicID.equals("")) {
                    Toast.makeText(mContext, "Please Choose Topic for Community.", Toast.LENGTH_LONG).show();
                } else if (Keyword.equals("")) {
                    Toast.makeText(mContext, "Please Choose Keyword for Community.", Toast.LENGTH_LONG).show();
                } else if (photoList.size() == 0) {
                    Toast.makeText(mContext, "Please Select Photo for Community.", Toast.LENGTH_LONG).show();
                } else if (Description.equals("")) {
                    Toast.makeText(mContext, "Please Enter Description for Community.", Toast.LENGTH_LONG).show();
                } else if (UserRange.equals("")) {
                    Toast.makeText(mContext, "Please Select Your Range.", Toast.LENGTH_LONG).show();
                } else {
                    AddCommunityApi(TopicID, Keyword, Description, UserRange);
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

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final TopicKeywordModel topicKeywordModel = arrayList.get(position);

            if (WhereFrom.equals("Topic")) {
                holder.txtCategoryName.setText("  " + topicKeywordModel.getTopic());
            } else if (WhereFrom.equals("Keyword")) {
                holder.txtCategoryName.setText("  " + topicKeywordModel.getKeyword());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (WhereFrom.equals("Topic")) {
                        TopicID = "";
                        Topic = "";
                        TopicID = topicKeywordModel.getTopicID();
                        Topic = topicKeywordModel.getTopic();
                        txtSelectedTopic.setText(Topic);
                    } else if (WhereFrom.equals("Keyword")) {
                        KeywordID = "";
                        Keyword = "";
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

    private void AddCommunityApi(final String TopicID, final String Keyword, final String Description, final String UserRange) {
        progressBar.setVisibility(View.VISIBLE);
        // our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConstant.getInstance().ADD_COMMUNITY, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject JsonMain = new JSONObject(new String(response.data));
                    Log.e("RESPONSE ", "" + APIConstant.getInstance().ADD_COMMUNITY + JsonMain);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                        RangeSettingActivity.UserRange = "";
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                // params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_COMMUNITY + params);
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
                params.put("topic_id", TopicID);
                params.put("keyword", Keyword);
                params.put("description", Description);
                params.put("lats", Latitude);
                params.put("longs", Longitude);
                params.put("location_name", LocationName);
                params.put("user_range", UserRange);
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_COMMUNITY + params);
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