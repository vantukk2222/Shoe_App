package com.midterm.shoestore.model;

import java.util.Map;

public class Order {
    private String orderId;
    private String userId;
    private String status;
    private String totalPrice;
    private Map<String, Integer> shoeQuantities;
    private String timePlaced;
    private String timeDelivered;


    public Order(String orderId, String userId, String status, String totalPrice, Map<String, Integer> shoeQuantities, String timestamp, String timeDelivered) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.shoeQuantities = shoeQuantities;
        this.timePlaced = timestamp;
        this.timeDelivered = timeDelivered;
    }

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
}
