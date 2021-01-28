package com.app.metown.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.app.metown.VolleySupport.VolleyMultipartRequest;
import com.bumptech.glide.Glide;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    TextView txtDone;
    ImageView imgBack;
    CircleImageView imgProfile, imgCamera;
    EditText edtNickName, edtEmail, edtPhoneNumber;

    SharedPreferences sharedPreferences;

    private ArrayList<Bitmap> mTempBitmapArray = new ArrayList<Bitmap>();
    private int MY_REQUEST_CODE, REQUEST_CODE;
    private static final int SELECT_IMAGE = 4;
    int PERMISSION_ID = 44;
    String mPath = "";
    File photo;
    Bitmap mBitmap;

    String NickName = "", Email = "", MobileNumber = "", ProfileImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Log.e("Activity", "ProfileEditActivity");

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

        imgProfile = findViewById(R.id.imgProfile);
        imgCamera = findViewById(R.id.imgCamera);

        edtNickName = findViewById(R.id.edtNickName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);

        txtDone = findViewById(R.id.txtDone);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        imgCamera.setOnClickListener(this);
    }

    public void ViewSetText() {
        sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
        Glide.with(mContext).load(sharedPreferences.getString("ProfilePicture", "")).into(imgProfile);
        edtNickName.setText(sharedPreferences.getString("NickName", ""));
        edtEmail.setText(sharedPreferences.getString("Email", ""));
        edtPhoneNumber.setText(sharedPreferences.getString("PhoneNumber", ""));
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
            case R.id.txtDone:
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
                    if (mBitmap != null) {
                        ProfileImageUpload(NickName, Email, MobileNumber, mBitmap);
                    } else {
                        String ProfilePicture = sharedPreferences.getString("ProfilePicture", "");
                        if (ProfilePicture.equals("") || ProfilePicture.equals("null")) {
                            Toast.makeText(mContext, "Please Choose Your Profile...", Toast.LENGTH_SHORT).show();
                        } else {
                            EditProfileApi(NickName, Email, MobileNumber, ProfilePicture);
                        }
                    }
                }
                break;
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
                                Glide.with(mContext).load(mBitmap).into(imgProfile);
                                if (ConstantFunction.isNetworkAvailable(mContext)) {
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
                                }
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
                            Glide.with(mContext).load(mBitmap).into(imgProfile);
                            if (ConstantFunction.isNetworkAvailable(mContext)) {
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
                            }
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
                            Glide.with(mContext).load(mBitmap).into(imgProfile);
                            if (ConstantFunction.isNetworkAvailable(mContext)) {
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
                            }
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

    /*public int getOrientation(Uri selectedImage) {
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
    }*/

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
            Glide.with(mContext).load(mBitmap).into(imgProfile);
            if (ConstantFunction.isNetworkAvailable(mContext)) {
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
            }
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void ProfileImageUpload(final String NickName, final String Email, final String MobileNumber, final Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);
        // our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConstant.getInstance().EDIT_PROFILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject JsonMain = new JSONObject(new String(response.data));
                            Log.e("RESPONSE", "" + APIConstant.getInstance().EDIT_PROFILE + JsonMain.toString());
                            String HAS_ERROR = JsonMain.getString("has_error");
                            String Message = JsonMain.getString("msg");
                            if (HAS_ERROR.equals("false")) {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();

                                JSONObject objectData = JsonMain.getJSONObject("data");

                                JSONObject objectUser = objectData.getJSONObject("user");

                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                sharedPreferencesEditor.putString("UserID", objectUser.getString("user_id"));
                                sharedPreferencesEditor.putString("UniqueID", objectUser.getString("unique_id"));
                                sharedPreferencesEditor.putString("NickName", objectUser.getString("nick_name"));
                                sharedPreferencesEditor.putString("Email", objectUser.getString("email"));
                                sharedPreferencesEditor.putString("SocialID", objectUser.getString("social_id"));
                                sharedPreferencesEditor.putString("PhoneNumber", objectUser.getString("phone_number"));
                                sharedPreferencesEditor.putString("InvitationCode", objectUser.getString("invitation_code"));
                                sharedPreferencesEditor.putString("Status", objectUser.getString("status"));
                                sharedPreferencesEditor.putString("EmailVerify", objectUser.getString("email_verify"));
                                sharedPreferencesEditor.putString("ProfilePicture", objectUser.getString("profile_pic"));
                                sharedPreferencesEditor.apply();
                                sharedPreferencesEditor.commit();

                                ViewSetText();
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().EDIT_PROFILE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nick_name", NickName);
                params.put("email", Email);
                params.put("phone_number", MobileNumber);
                Log.e("PARAMETER", "" + APIConstant.getInstance().EDIT_PROFILE + params);
                return params;
            }

            // Form data passing (For Image)
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long ImageName = System.currentTimeMillis();
                params.put("profile_pic", new DataPart(ImageName + ".png", getFileDataFromDrawable(bitmap)));
                Log.e("PARAMETER Image", "" + APIConstant.getInstance().EDIT_PROFILE + params);
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

    private void EditProfileApi(final String NickName, final String Email, final String MobileNumber, final String ProfilePicture) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().EDIT_PROFILE,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().EDIT_PROFILE + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String SuccessMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();

                                JSONObject objectData = JsonMain.getJSONObject("data");
                                JSONObject objectUser = objectData.getJSONObject("user");

                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                sharedPreferencesEditor.putString("UserID", objectUser.getString("user_id"));
                                sharedPreferencesEditor.putString("UniqueID", objectUser.getString("unique_id"));
                                sharedPreferencesEditor.putString("NickName", objectUser.getString("nick_name"));
                                sharedPreferencesEditor.putString("Email", objectUser.getString("email"));
                                sharedPreferencesEditor.putString("SocialID", objectUser.getString("social_id"));
                                sharedPreferencesEditor.putString("PhoneNumber", objectUser.getString("phone_number"));
                                sharedPreferencesEditor.putString("InvitationCode", objectUser.getString("invitation_code"));
                                sharedPreferencesEditor.putString("Status", objectUser.getString("status"));
                                sharedPreferencesEditor.putString("EmailVerify", objectUser.getString("email_verify"));
                                sharedPreferencesEditor.putString("ProfilePicture", objectUser.getString("profile_pic"));
                                sharedPreferencesEditor.apply();
                                sharedPreferencesEditor.commit();

                                ViewSetText();
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
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().EDIT_PROFILE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                long ImageName = System.currentTimeMillis();
                params.put("nick_name", NickName);
                params.put("email", Email);
                params.put("phone_number", MobileNumber);
                params.put("profile_pic", ProfilePicture);
                Log.e("PARAMETER", "" + APIConstant.getInstance().EDIT_PROFILE + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().EDIT_PROFILE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}