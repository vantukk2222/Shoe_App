package com.midterm.shoestore.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;
import com.midterm.shoestore.model.Checkout;
import com.midterm.shoestore.model.ShoeItem;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemAdapter.CheckoutItemViewHolder>{
    private List<String> IDShoeList;
    private List<Integer> QuantityList;
    private final Context context;

    public CheckoutItemAdapter(List<String> idShoeList, List<Integer> quantityList, Context context) {
        IDShoeList = idShoeList;
        this.QuantityList = quantityList;
        this.context = context;
    }
    public void setCheckoutItemList(List<String> idShoeList, List<Integer> quantityList){
        this.IDShoeList = idShoeList;
        this.QuantityList = quantityList;
    }

    @NonNull
    @Override
    public CheckoutItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_checkout_item , parent , false);

        return new CheckoutItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutItemViewHolder holder, int position) {
        String shoeID = IDShoeList.get(position);
        Integer quantity = QuantityList.get(position);

        DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference("shoes");
        Query query = shoesRef.orderByKey().equalTo(shoeID);
        Integer finalQuantity = quantity;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy ra Shoes được trả về từ dataSnapshot
                    ShoeItem shoes = dataSnapshot.getChildren().iterator().next().getValue(ShoeItem.class);
                    Glide.with(context).load(shoes.getShoeImage()).into(holder.imageview_ordered_product);
                    holder.textview_ordered_product_name.setText(shoes.getShoeName());
                    holder.textview_ordered_product_price.setText("Giá $" + shoes.getShoePrice());
                    holder.textview_ordered_product_quantity.setText("Số lượng: " + finalQuantity);

                } else {
                    Glide.with(context).load("https://icon-library.com/images/empty-icon/empty-icon-19.jpg").into(holder.imageview_ordered_product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CheckoutItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageview_ordered_product;
        private final TextView textview_ordered_product_name;
        private final TextView textview_ordered_product_price;
        private final TextView textview_ordered_product_quantity;

        private final CardView cardView;
        public CheckoutItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview_ordered_product = itemView.findViewById(R.id.detail_imageview_ordered_product);
            textview_ordered_product_name = itemView.findViewById(R.id.detail_textview_ordered_product_name);
            textview_ordered_product_quantity = itemView.findViewById(R.id.detail_textview_ordered_product_quantity);
            textview_ordered_product_price = itemView.findViewById(R.id.detail_textview_ordered_product_price);
            cardView = itemView.findViewById(R.id.checkout_item_adapter);
        }
    }

}
