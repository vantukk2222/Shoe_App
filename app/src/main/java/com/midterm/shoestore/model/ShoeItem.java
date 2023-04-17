package com.midterm.shoestore.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoeItem implements Parcelable {

    private int shoe_ID;
    private String shoeName, shoeBrandName;
    private int shoeImage;
    private double shoePrice;

    public ShoeItem(int shoe_ID, String shoeName, String shoeBrandName, int shoeImage, double shoePrice) {
        this.shoe_ID = shoe_ID;
        this.shoeName = shoeName;
        this.shoeBrandName = shoeBrandName;
        this.shoeImage = shoeImage;
        this.shoePrice = shoePrice;
    }
    protected ShoeItem(Parcel in)
    {
        shoe_ID = in.readInt();
        shoeName = in.readString();
        shoeBrandName = in.readString();
        shoeImage = in.readInt();
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

    public int getShoe_ID() {
        return shoe_ID;
    }

    public void setShoe_ID(int shoe_ID) {
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

    public int getShoeImage() {
        return shoeImage;
    }

    public void setShoeImage(int shoeImage) {
        this.shoeImage = shoeImage;
    }

    public double getShoePrice() {
        return shoePrice;
    }

    public void setShoePrice(double shoePrice) {
        this.shoePrice = shoePrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(shoe_ID);
        parcel.writeString(shoeName);
        parcel.writeString(shoeBrandName);
        parcel.writeInt(shoeImage);
        parcel.writeDouble(shoePrice);
    }
}
