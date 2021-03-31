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

public class PostHiringHelperActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgPhoto;
    EditText edtSalary, edtDescription;
    TextView txtJobKeyword, txtSelectedJobKeyword, txtImageCount, txtSalary, txtPhoto, txtWorkingTime, txtDescription, txtSetTime, txtDone;
    LinearLayout SelectJobKeywordLayout;
    RecyclerView SelectCategoryView, PhotoView;
    ArrayList<TopicKeywordModel> keywordList = new ArrayList<>();
    Dialog dialog;
    String KeywordID = "", Description = "", Salary = "", StartWorkingTime = "", EndWorkingTime = "", UserRange = "";
    private static final int SELECT_IMAGE = 4;
    private int MY_REQUEST_CODE, REQUEST_CODE;
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> pathList = new ArrayList<>();
    ArrayList<Bitmap> photoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_hiring_helper);

        Log.e("Activity", "PostHiringHelperActivity");

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
        txtSetTime = findViewById(R.id.txtSetTime);
        txtJobKeyword = findViewById(R.id.txtJobKeyword);
        txtSelectedJobKeyword = findViewById(R.id.txtSelectedJobKeyword);
        txtSalary = findViewById(R.id.txtSalary);
        txtPhoto = findViewById(R.id.txtPhoto);
        txtWorkingTime = findViewById(R.id.txtWorkingTime);
        txtDescription = findViewById(R.id.txtDescription);
        txtImageCount = findViewById(R.id.txtImageCount);

        edtSalary = findViewById(R.id.edtSalary);
        edtDescription = findViewById(R.id.edtDescription);

        SelectJobKeywordLayout = findViewById(R.id.SelectJobKeywordLayout);

        PhotoView = findViewById(R.id.PhotoView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        imgPhoto.setOnClickListener(this);
        SelectJobKeywordLayout.setOnClickListener(this);
        txtSetTime.setOnClickListener(this);
        txtDone.setOnClickListener(this);
    }

    public void ViewSetText() {
        String JobKeyword = "<font color='#000000'>Job Keyword</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtJobKeyword.setText(Html.fromHtml(JobKeyword));

        String Salary = "<font color='#000000'>Salary</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtSalary.setText(Html.fromHtml(Salary));

        String Photo = "<font color='#000000'>Photos</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtPhoto.setText(Html.fromHtml(Photo));

        String WorkingTime = "<font color='#000000'>Working Time</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtWorkingTime.setText(Html.fromHtml(WorkingTime));

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
                Salary = edtSalary.getText().toString().trim();
                Description = edtDescription.getText().toString().trim();
                UserRange = RangeSettingActivity.UserRange;
                if (KeywordID.equals("")) {
                    Toast.makeText(mContext, "Please Select your Keyword.", Toast.LENGTH_LONG).show();
                } else if (Salary.equals("")) {
                    Toast.makeText(mContext, "Please Enter your Salary.", Toast.LENGTH_LONG).show();
                } else if (photoList.size() == 0) {
                    Toast.makeText(mContext, "Please Select your image.", Toast.LENGTH_LONG).show();
                } else if (StartWorkingTime.equals("")) {
                    Toast.makeText(mContext, "Please Select your working time.", Toast.LENGTH_LONG).show();
                } else if (EndWorkingTime.equals("")) {
                    Toast.makeText(mContext, "Please Select your working time.", Toast.LENGTH_LONG).show();
                } else if (Description.equals("")) {
                    Toast.makeText(mContext, "Please Enter Description.", Toast.LENGTH_LONG).show();
                } else if (UserRange.equals("")) {
                    Toast.makeText(mContext, "Please Select Your Range.", Toast.LENGTH_LONG).show();
                } else {
                    AddPostHireHelperApi(KeywordID, Description, Salary, StartWorkingTime, EndWorkingTime, UserRange);
                }
                break;
            case R.id.SelectJobKeywordLayout:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_category_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                SelectCategoryView = dialog.findViewById(R.id.SelectCategoryView);
                GetJobKeywordListApi();
                break;
            case R.id.txtSetTime:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.set_time_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final EditText edtStartTime = dialog.findViewById(R.id.edtStartTime);
                final EditText edtEndTime = dialog.findViewById(R.id.edtEndTime);
                dialog.findViewById(R.id.txtCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.findViewById(R.id.txtDone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StartWorkingTime = edtStartTime.getText().toString().trim();
                        EndWorkingTime = edtEndTime.getText().toString().trim();
                        if (StartWorkingTime.equals("")) {
                            Toast.makeText(mContext, "Please Enter Start working time.", Toast.LENGTH_LONG).show();
                        } else if (EndWorkingTime.equals("")) {
                            Toast.makeText(mContext, "Please Enter End working time.", Toast.LENGTH_LONG).show();
                        } else {
                            dialog.cancel();
                        }
                    }
                });
                dialog.show();
                break;
        }
    }

    private void GetJobKeywordListApi() {
        String req = "req";
        keywordList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().GET_JOB_KEYWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_JOB_KEYWORD + response);
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
                        String msg = JsonMain.getString("msg");
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_JOB_KEYWORD + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_JOB_KEYWORD);
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

            holder.txtCategoryName.setText("  " + topicKeywordModel.getKeyword());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KeywordID = "";
                    KeywordID = topicKeywordModel.getKeywordID();
                    String Keyword = topicKeywordModel.getKeyword();
                    txtSelectedJobKeyword.setText(Keyword);
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

    private void AddPostHireHelperApi(final String KeywordID, final String Description, final String Salary, final String StartWorkingTime,
                                      final String EndWorkingTime, final String UserRange) {
        progressBar.setVisibility(View.VISIBLE);
        // our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConstant.getInstance().ADD_POST_HIRE_HELPER, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject JsonMain = new JSONObject(new String(response.data));
                    Log.e("RESPONSE ", "" + APIConstant.getInstance().ADD_POST_HIRE_HELPER + JsonMain);
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
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_POST_HIRE_HELPER + params);
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
                params.put("keyword_id", KeywordID);
                params.put("description", Description);
                params.put("salary", Salary);
                params.put("start_working_time", StartWorkingTime);
                params.put("end_working_time", EndWorkingTime);
                params.put("lats", Latitude);
                params.put("longs", Longitude);
                params.put("location_name", LocationName);
                params.put("user_range", UserRange);
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_POST_HIRE_HELPER + params);
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