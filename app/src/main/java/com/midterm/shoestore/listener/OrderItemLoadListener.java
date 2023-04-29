package com.midterm.shoestore.listener;

import com.midterm.shoestore.model.Order;
import com.midterm.shoestore.model.ShoeItem;

import java.util.List;

public interface OrderItemLoadListener {
    void onOrderItemLoadSuccess(List<Order> ordersItemList);
    void onOrderItemLoadFailed(String message);
}
