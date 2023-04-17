package com.midterm.shoestore.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class ShoeItem implements Parcelable {

    private String shoeName;
    private int shoeBrand;
    private int shoeCategory;
    private int shoeColor;
    private String shoeImage;
    private int shoeStatus;
    private int shoeSize;
    private double shoePrice;

    public ShoeItem() {}

    public ShoeItem(String shoeName, int shoeBrand, int shoeCategory, int shoeColor, String shoeImage, int shoeStatus, int shoeSize, double shoePrice) {
        this.shoeName = shoeName;
        this.shoeBrand = shoeBrand;
        this.shoeCategory = shoeCategory;
        this.shoeColor = shoeColor;
        this.shoeImage = shoeImage;
        this.shoeStatus = shoeStatus;
        this.shoeSize = shoeSize;
        this.shoePrice = shoePrice;
    }

    protected ShoeItem(Parcel in) {
        shoeName = in.readString();
        shoeBrand = in.readInt();
        shoeCategory = in.readInt();
        shoeColor = in.readInt();
        shoeImage = in.readString();
        shoeStatus = in.readInt();
        shoeSize = in.readInt();
        shoePrice = in.readDouble();
    }

    public static final Creator<ShoeItem> CREATOR = new Creator<ShoeItem>() {
        @Override
        public ShoeItem createFromParcel(Parcel in) {
            return new ShoeItem(in);
        }

        @Override
        public ShoeItem[] newArray(int size) {
            return new ShoeItem[size];
        }
    };

    public String getShoeName() {
        return shoeName;
    }

    public void setShoeName(String shoeName) {
        this.shoeName = shoeName;
    }

    public int getShoeBrand() {
        return shoeBrand;
    }

    public void setShoeBrand(int shoeBrand) {
        this.shoeBrand = shoeBrand;
    }

    public int getShoeCategory() {
        return shoeCategory;
    }

    public void setShoeCategory(int shoeCategory) {
        this.shoeCategory = shoeCategory;
    }

    public int getShoeColor() {
        return shoeColor;
    }

    public void setShoeColor(int shoeColor) {
        this.shoeColor = shoeColor;
    }

    public String getShoeImage() {
        return shoeImage;
    }

    public void setShoeImage(String shoeImage) {
        this.shoeImage = shoeImage;
    }

    public int getShoeStatus() {
        return shoeStatus;
    }

    public void setShoeStatus(int shoeStatus) {
        this.shoeStatus = shoeStatus;
    }

    public int getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(int shoeSize) {
        this.shoeSize = shoeSize;
    }

    public double getShoePrice() {
        return shoePrice;
    }

    public void setShoePrice(double shoePrice) {
        this.shoePrice = shoePrice;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("shoeName", shoeName);
        result.put("shoeBrand", shoeBrand);
        result.put("shoeCategory", shoeCategory);
        result.put("shoeColor", shoeColor);
        result.put("shoeImage", shoeImage);
        result.put("shoeStatus", shoeStatus);
        result.put("shoeSize", shoeSize);
        result.put("shoePrice", shoePrice);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shoeName);
        parcel.writeInt(shoeBrand);
        parcel.writeInt(shoeCategory);
        parcel.writeInt(shoeColor);
        parcel.writeString(shoeImage);
        parcel.writeInt(shoeStatus);
        parcel.writeInt(shoeSize);
        parcel.writeDouble(shoePrice);
    }
}