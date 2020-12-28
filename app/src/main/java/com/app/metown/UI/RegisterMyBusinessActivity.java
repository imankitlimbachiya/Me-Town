package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RegisterMyBusinessActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtTypeOfBusiness, txtNameOfStore, txtDetailTypeOfBusiness, txtAddress, txtDetailedAddress,
            txtContactNumber, txtPhoto, txtDone;
    RelativeLayout SelectBarangayLayout;
    RecyclerView BarangayKeywordView, BarangayKeywordFromAlphabetView;
    ArrayList<CategoryMainModel> selectBarangayKeywordList = new ArrayList<>();
    ArrayList<CategoryMainModel> selectBarangayKeywordFromAlphabetList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_my_business);

        Log.e("Activity","RegisterMyBusinessActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtTypeOfBusiness = findViewById(R.id.txtTypeOfBusiness);
        txtNameOfStore = findViewById(R.id.txtNameOfStore);
        txtDetailTypeOfBusiness = findViewById(R.id.txtDetailTypeOfBusiness);
        txtAddress = findViewById(R.id.txtAddress);
        txtDetailedAddress = findViewById(R.id.txtDetailedAddress);
        txtContactNumber = findViewById(R.id.txtContactNumber);
        txtPhoto = findViewById(R.id.txtPhoto);

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

        SelectBarangayLayout = findViewById(R.id.SelectBarangayLayout);

        txtDone = findViewById(R.id.txtDone);

        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        SelectBarangayLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtDone:
                /*Intent PostForSale = new Intent(mContext, PostForSaleActivity.class);
                startActivity(PostForSale);
                finish();*/
                break;
            case R.id.SelectBarangayLayout:
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_barangay_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                BarangayKeywordView = dialog.findViewById(R.id.BarangayKeywordView);
                BarangayKeywordFromAlphabetView = dialog.findViewById(R.id.BarangayKeywordFromAlphabetView);
                AddSelectBarangayKeywordItems();
                AddSelectBarangayKeywordFromAlphabetViewItems();
                dialog.findViewById(R.id.UnSelectBarangayLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
        }
    }

    public void AddSelectBarangayKeywordItems() {
        selectBarangayKeywordList.clear();
        for (int i = 1; i <= 10; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Item name");
            selectBarangayKeywordList.add(categoryMainModel);
        }

        if (selectBarangayKeywordList.size() > 0) {
            SelectBarangayKeywordAdapter selectBarangayKeywordAdapter = new SelectBarangayKeywordAdapter(mContext, selectBarangayKeywordList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
            BarangayKeywordView.setLayoutManager(mLayoutManager);
            BarangayKeywordView.setItemAnimator(new DefaultItemAnimator());
            BarangayKeywordView.setAdapter(selectBarangayKeywordAdapter);
            selectBarangayKeywordAdapter.notifyDataSetChanged();
        }
    }

    public static class SelectBarangayKeywordAdapter extends RecyclerView.Adapter<SelectBarangayKeywordAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryMainModel> arrayList;

        static class MyViewHolder extends RecyclerView.ViewHolder {

            // RadioButton btnSelect;

            MyViewHolder(View view) {
                super(view);

                // btnSelect = view.findViewById(R.id.btnSelect);
            }
        }

        public SelectBarangayKeywordAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
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
            CategoryMainModel categoryMainModel = arrayList.get(position);

            // holder.btnSelect.setText("  " + categoryModel.getCategoryName());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public void AddSelectBarangayKeywordFromAlphabetViewItems() {
        selectBarangayKeywordFromAlphabetList.clear();
        for (int i = 1; i <= 4; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Item name");
            selectBarangayKeywordFromAlphabetList.add(categoryMainModel);
        }

        if (selectBarangayKeywordFromAlphabetList.size() > 0) {
            SelectBarangayKeywordFromAlphabetAdapter selectBarangayKeywordFromAlphabetAdapter = new
                    SelectBarangayKeywordFromAlphabetAdapter(mContext, selectBarangayKeywordFromAlphabetList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            BarangayKeywordFromAlphabetView.setLayoutManager(mLayoutManager);
            BarangayKeywordFromAlphabetView.setItemAnimator(new DefaultItemAnimator());
            BarangayKeywordFromAlphabetView.setAdapter(selectBarangayKeywordFromAlphabetAdapter);
            selectBarangayKeywordFromAlphabetAdapter.notifyDataSetChanged();
        }
    }

    public static class SelectBarangayKeywordFromAlphabetAdapter extends RecyclerView.Adapter<SelectBarangayKeywordFromAlphabetAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryMainModel> arrayList;
        ArrayList<CategoryMainModel> selectBarangayKeywordFromSpecificAlphabetList = new ArrayList<>();

        static class MyViewHolder extends RecyclerView.ViewHolder {

            RecyclerView BarangayKeywordFromSpecificAlphabetView;

            MyViewHolder(View view) {
                super(view);

                BarangayKeywordFromSpecificAlphabetView = view.findViewById(R.id.BarangayKeywordFromSpecificAlphabetView);
            }
        }

        public SelectBarangayKeywordFromAlphabetAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }


        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_barangay_keyword_from_alphabet_adapter,parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            CategoryMainModel categoryMainModel = arrayList.get(position);

            selectBarangayKeywordFromSpecificAlphabetList.clear();
            for (int i = 1; i <= 4; i++) {
                CategoryMainModel CategoryMainModel = new CategoryMainModel(String.valueOf(i), "Item name");
                selectBarangayKeywordFromSpecificAlphabetList.add(CategoryMainModel);
            }

            if (selectBarangayKeywordFromSpecificAlphabetList.size() > 0) {
                SelectBarangayKeywordFromSpecificAlphabetAdapter selectBarangayKeywordFromSpecificAlphabetAdapter = new
                        SelectBarangayKeywordFromSpecificAlphabetAdapter(mContext, selectBarangayKeywordFromSpecificAlphabetList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                holder.BarangayKeywordFromSpecificAlphabetView.setLayoutManager(mLayoutManager);
                holder.BarangayKeywordFromSpecificAlphabetView.setItemAnimator(new DefaultItemAnimator());
                holder.BarangayKeywordFromSpecificAlphabetView.setAdapter(selectBarangayKeywordFromSpecificAlphabetAdapter);
                selectBarangayKeywordFromSpecificAlphabetAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class SelectBarangayKeywordFromSpecificAlphabetAdapter extends RecyclerView.Adapter<SelectBarangayKeywordFromSpecificAlphabetAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryMainModel> arrayList;

        static class MyViewHolder extends RecyclerView.ViewHolder {

            //

            MyViewHolder(View view) {
                super(view);

                //
            }
        }

        public SelectBarangayKeywordFromSpecificAlphabetAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_barangay_keyword_from_specific_alphabet_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            CategoryMainModel categoryMainModel = arrayList.get(position);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
