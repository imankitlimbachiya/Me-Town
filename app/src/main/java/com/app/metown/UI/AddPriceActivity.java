package com.app.metown.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddPriceActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtDone;
    EditText edtServiceOrItem, edtInputFixedOrMinimumPrice;
    RadioButton FixedRadioButton, MinimumRadioButton, rbtMainMenu;
    String ID = "", BusinessID = "", ServiceItem = "", PriceKind = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);

        Log.e("Activity", "AddPriceActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetIntentData();

        ViewInitialization();

        ViewSetText();

        ViewOnClick();
    }

    public void GetIntentData() {
        ID = getIntent().getStringExtra("ID");
        BusinessID = getIntent().getStringExtra("BusinessID");
        BusinessID = "4";
        ServiceItem = getIntent().getStringExtra("ServiceItem");
        PriceKind = getIntent().getStringExtra("PriceKind");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtDone = findViewById(R.id.txtDone);

        edtServiceOrItem = findViewById(R.id.edtServiceOrItem);
        edtInputFixedOrMinimumPrice = findViewById(R.id.edtInputFixedOrMinimumPrice);

        FixedRadioButton = findViewById(R.id.FixedRadioButton);
        MinimumRadioButton = findViewById(R.id.MinimumRadioButton);
        rbtMainMenu = findViewById(R.id.rbtMainMenu);
    }

    public void ViewSetText() {
        if (!ID.equals("")) {
            edtServiceOrItem.setText(ServiceItem);
            edtInputFixedOrMinimumPrice.setText(PriceKind);
        }
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);

        FixedRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setSelected(!buttonView.isSelected());
                Drawable img;
                if (isChecked) {
                    img = mContext.getResources().getDrawable(R.drawable.negotiable_selected);
                } else {
                    img = mContext.getResources().getDrawable(R.drawable.negotiable_unselected);
                }
                img.setBounds(0, 0, 50, 50);
                buttonView.setCompoundDrawables(img, null, null, null);
            }
        });

        MinimumRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setSelected(!buttonView.isSelected());
                Drawable img;
                if (isChecked) {
                    img = mContext.getResources().getDrawable(R.drawable.negotiable_selected);
                } else {
                    img = mContext.getResources().getDrawable(R.drawable.negotiable_unselected);
                }
                img.setBounds(0, 0, 50, 50);
                buttonView.setCompoundDrawables(img, null, null, null);
            }
        });

        rbtMainMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setSelected(!buttonView.isSelected());
                Drawable img;
                if (isChecked) {
                    img = mContext.getResources().getDrawable(R.drawable.negotiable_selected);
                } else {
                    img = mContext.getResources().getDrawable(R.drawable.negotiable_unselected);
                }
                img.setBounds(0, 0, 50, 50);
                buttonView.setCompoundDrawables(img, null, null, null);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtDone:
                ServiceItem = edtServiceOrItem.getText().toString().trim();
                PriceKind = edtInputFixedOrMinimumPrice.getText().toString().trim();
                if (ServiceItem.equals("")) {
                    Toast.makeText(mContext, "Please enter Your Service Or Item.", Toast.LENGTH_LONG).show();
                } else if (PriceKind.equals("")) {
                    Toast.makeText(mContext, "Please enter Your Service Or Item Price.", Toast.LENGTH_LONG).show();
                } else {
                    AddEditBusinessItemPriceApi(ID, BusinessID, ServiceItem, PriceKind);
                }
                break;
        }
    }

    private void AddEditBusinessItemPriceApi(final String ID, final String BusinessID, final String ServiceItem, final String PriceKind) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_EDIT_BUSINESS_ITEM_PRICE, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_EDIT_BUSINESS_ITEM_PRICE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String msg = JsonMain.getString("msg");
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        finish();
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
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_EDIT_BUSINESS_ITEM_PRICE + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"id\":\"" + ID + "\",\"business_id\":\"" + BusinessID + "\",\"service_item\":\"" + ServiceItem +
                        "\",\"price_kind\":\"" + PriceKind + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_EDIT_BUSINESS_ITEM_PRICE + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_EDIT_BUSINESS_ITEM_PRICE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}