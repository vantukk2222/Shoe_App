package com.midterm.shoestore.listener;

import com.midterm.shoestore.model.ShoeItem;

import java.util.List;

public interface ShoeItemLoadListener {
    void onShoeItemLoadSuccess(List<ShoeItem> shoeItemList);
    void onShoeItemLoadFailed(String message);
}
