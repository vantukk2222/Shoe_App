package com.midterm.shoestore.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Map;

public class Order implements Parcelable {
    private String orderId;
    private Map<String, Integer> shoeQuantities;
    private String status;
    private String timeDelivered;
    private String timePlaced;
    private String timeCancelled;
    private String totalPrice;
    private String userId;



    public Order(String orderId, String userId, String status, String totalPrice, Map<String, Integer> shoeQuantities, String timestamp, String timeDelivered, String timeCancelled) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.shoeQuantities = shoeQuantities;
        this.timePlaced = timestamp;
        this.timeDelivered = timeDelivered;
        this.timeCancelled = timeCancelled;
    }
    public Order()
    {

    }

    protected Order(Parcel in) {
        orderId = in.readString();
        userId = in.readString();
        status = in.readString();
        totalPrice = in.readString();
        timePlaced = in.readString();
        timeDelivered = in.readString();
        timeCancelled = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Map<String, Integer> getShoeQuantities() {
        return shoeQuantities;
    }

    public void setShoeQuantities(Map<String, Integer> shoeQuantities) {
        this.shoeQuantities = shoeQuantities;
    }

    public String getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(String timePlaced) {
        this.timePlaced = timePlaced;
    }

    public String getTimeDelivered() {
        return timeDelivered;
    }

    public void setTimeDelivered(String timeDelivered) {
        this.timeDelivered = timeDelivered;
    }
    public String getTimeCancelled() {
        return timeCancelled;
    }

    public void setTimeCancelled(String timeCancelled) {
        this.timeCancelled = timeCancelled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(userId);
        parcel.writeString(status);
        parcel.writeString(totalPrice);
        parcel.writeString(timePlaced);
        parcel.writeString(timeDelivered);
        parcel.writeString(timeCancelled);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", shoeQuantities=" + shoeQuantities +
                ", status='" + status + '\'' +
                ", timeDelivered='" + timeDelivered + '\'' +
                ", timeCancelled='" + timeCancelled + '\'' +
                ", timePlaced='" + timePlaced + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
