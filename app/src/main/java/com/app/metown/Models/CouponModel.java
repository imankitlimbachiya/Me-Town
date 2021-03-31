package com.app.metown.Models;

public class CouponModel {

    String CouponID, CouponAdvertiseID, CouponName, CouponImages, CouponCount, DetailOfCoupon;

    public String getCouponID() {
        return CouponID;
    }

    public void setCouponID(String couponID) {
        CouponID = couponID;
    }

    public String getCouponAdvertiseID() {
        return CouponAdvertiseID;
    }

    public void setCouponAdvertiseID(String couponAdvertiseID) {
        CouponAdvertiseID = couponAdvertiseID;
    }

    public String getCouponName() {
        return CouponName;
    }

    public void setCouponName(String couponName) {
        CouponName = couponName;
    }

    public String getCouponImages() {
        return CouponImages;
    }

    public void setCouponImages(String couponImages) {
        CouponImages = couponImages;
    }

    public String getCouponCount() {
        return CouponCount;
    }

    public void setCouponCount(String couponCount) {
        CouponCount = couponCount;
    }

    public String getDetailOfCoupon() {
        return DetailOfCoupon;
    }

    public void setDetailOfCoupon(String detailOfCoupon) {
        DetailOfCoupon = detailOfCoupon;
    }
}