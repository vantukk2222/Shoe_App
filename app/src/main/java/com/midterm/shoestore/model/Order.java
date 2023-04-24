package com.midterm.shoestore.model;

import java.util.Map;

public class Order {
    private String orderId;
    private String userId;
    private String status;
    private String totalPrice;
    private Map<String, Integer> shoeQuantities;
    private String timestamp;


    public Order(String orderId, String userId, String status, String totalPrice, Map<String, Integer> shoeQuantities, String timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.shoeQuantities = shoeQuantities;
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, Integer> getShoeQuantities() {
        return shoeQuantities;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
