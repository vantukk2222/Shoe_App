package com.midterm.shoestore.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoeItem implements Parcelable {

    private String shoe_ID;
    private String shoeName, shoeBrandName;
    private String shoeImage;
    private String shoePrice;

    public ShoeItem(String shoe_ID, String shoeName, String shoeBrandName, String shoeImage, String shoePrice) {
        this.shoe_ID = shoe_ID;
        this.shoeName = shoeName;
        this.shoeBrandName = shoeBrandName;
        this.shoeImage = shoeImage;
        this.shoePrice = shoePrice;
    }

    public ShoeItem(){

    }
    protected ShoeItem(Parcel in)
    {
        shoe_ID = in.readString();
        shoeName = in.readString();
        shoeBrandName = in.readString();
        shoeImage = in.readString();
        shoePrice = in.readString();
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

    public String getShoe_ID() {
        return shoe_ID;
    }

    public void setShoe_ID(String shoe_ID) {
        this.shoe_ID = shoe_ID;
    }

    public String getShoeName() {
        return shoeName;
    }

    public void setShoeName(String shoeName) {
        this.shoeName = shoeName;
    }

    public String getShoeBrandName() {
        return shoeBrandName;
    }

    public void setShoeBrandName(String shoeBrandName) {
        this.shoeBrandName = shoeBrandName;
    }

    public String getShoeImage() {
        return shoeImage;
    }

    public void setShoeImage(String shoeImage) {
        this.shoeImage = shoeImage;
    }

    public String getShoePrice() {
        return shoePrice;
    }

    public void setShoePrice(String shoePrice) {
        this.shoePrice = shoePrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shoe_ID);
        parcel.writeString(shoeName);
        parcel.writeString(shoeBrandName);
        parcel.writeString(shoeImage);
        parcel.writeString(shoePrice);
    }
}
