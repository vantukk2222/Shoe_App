package com.midterm.shoestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;
import com.midterm.shoestore.model.Order;
import com.midterm.shoestore.model.ShoeItem;
import com.midterm.shoestore.views.detail_checkoutActivity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<Order> OrderItemList;
    private final Context context;
    private final OrderItemAdapter.OrderClickedListeners OrderClickedListeners;
    private String uid;


    public OrderItemAdapter(Context context, List<Order> shoeItemList, OrderItemAdapter.OrderClickedListeners OrderClickedListeners){
        this.context = context;
        this.OrderItemList = shoeItemList;
        this.OrderClickedListeners = OrderClickedListeners;
    }
    public void setOrderItemList(List<Order> OrderItemList){
        this.OrderItemList = OrderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.checkout_item , parent , false);
//        TextView textview_ordered_product_detail = view.findViewById(R.id.textview_ordered_product_detail);
//        textview_ordered_product_detail.setVisibility(View.GONE);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Order OrderItem = OrderItemList.get(position);

        //holder.shoeNameTv.setText(shoeItem.getShoeName());
        //holder.shoeBrandNameTv.setText(shoeItem.getShoeBrandName());
        //holder.shoePriceTv.setText(String.valueOf(shoeItem.getShoePrice()));
        //holder.shoeImageView.setImageResource(shoeItem.getShoeImage());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderClickedListeners.onCardClicked(OrderItem);
            }
        });

        holder.textview_ordered_product_detail.setOnClickListener(view -> {
            Intent intent = new Intent(context, detail_checkoutActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("shoe_quantities", (Serializable) OrderItem.getShoeQuantities());
            intent.putExtra("bundle_key", bundle);
            intent.putExtra("Order", OrderItem);
            context.startActivity(intent);
        });
        String shoeID = "";
        Integer quantity = 0;
        Map.Entry<String, Integer> lastEntry = null;
        Iterator<Map.Entry<String, Integer>> it = OrderItem.getShoeQuantities().entrySet().iterator();
        while (it.hasNext()) {
            lastEntry = it.next();
        }

        if (lastEntry != null) {
            // Lấy key của entry cuối cùng trong Map
             shoeID = lastEntry.getKey();
            // Lấy value của entry cuối cùng trong Map
             quantity = lastEntry.getValue();
        }

        DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference("shoes");
        Query query = shoesRef.orderByKey().equalTo(shoeID);
        Integer finalQuantity = quantity;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy ra Shoes được trả về từ dataSnapshot
                    ShoeItem shoes = dataSnapshot.getChildren().iterator().next().getValue(ShoeItem.class);
                    holder.textview_ordered_ID_cart.setText("ID: "+ OrderItem.getOrderId());
                    holder.detail_textview_ordered_userID.setText("UID: " + OrderItem.getUserId());
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

//        holder.imageview_ordered_product

// Image


//        if (OrderItemList != null && OrderItemList.size() > position && OrderItemList.get(position) != null && OrderItemList.get(position).getOrderImage() != null) {
//            //Glide.with(context).load(shoeItemList.get(position).getShoeImage()).into(holder.shoeImageView);
////            image
////            Glide.with(context).load(OrderItemList.get(position).getOrderImage() != null ? OrderItemList.get(position).getOrderImage() : "https://icon-library.com/images/empty-icon/empty-icon-19.jpg").into(holder.OrderImageView);
//        } else {
//
//        }


        //Glide.with(context).load(shoeItemList.get(position).getShoeImage()).into(holder.shoeImageView);

//        holder.shoePriceTv.setText(new StringBuilder("đ").append(shoeItemList.get(position).getShoePrice()));
//        holder.shoeNameTv.setText(new StringBuilder().append(shoeItemList.get(position).getShoeName()));
//        holder.shoeBrandNameTv.setText(new StringBuilder().append(shoeItemList.get(position).getShoeBrandName()));

        /*holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoeClickedListeners.onAddToCartBtnClicked(shoeItem);
            }
        });*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context.getApplicationContext());
        this.uid = preferences.getString("uid", "");
        if (!uid.equals("admin")) {
            holder.detail_textview_ordered_userID.setVisibility(View.GONE);


        }
    }

    @Override
    public int getItemCount() {
        if (OrderItemList == null){
            return 0;
        }else{
            return OrderItemList.size();
        }
    }


    public class OrderItemViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageview_ordered_product;
        private final TextView textview_ordered_product_name;
        private final TextView textview_ordered_product_price;
        private final TextView textview_ordered_product_quantity;
        private final TextView textview_ordered_product_detail;
        private final TextView textview_ordered_ID_cart;
        private final TextView detail_textview_ordered_userID;
        private final CardView cardView;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textview_ordered_ID_cart = itemView.findViewById(R.id.textview_ordered_ID_cart);
            detail_textview_ordered_userID = itemView.findViewById(R.id.detail_textview_ordered_userID);
            imageview_ordered_product = itemView.findViewById(R.id.imageview_ordered_product);
            textview_ordered_product_name = itemView.findViewById(R.id.textview_ordered_product_name);
            textview_ordered_product_quantity = itemView.findViewById(R.id.textview_ordered_product_quantity);
            textview_ordered_product_price = itemView.findViewById(R.id.textview_ordered_product_price);
            textview_ordered_product_detail = itemView.findViewById(R.id.textview_ordered_product_detail);

            cardView = itemView.findViewById(R.id.checkout_item_adapter);


        }
    }

    public interface OrderClickedListeners{
        void onCardClicked(Order order);
        //void onAddToCartBtnClicked(ShoeItem shoeItem);
    }
    public void updateList(List<Order> newItems) {
        OrderItemList = newItems;
        notifyDataSetChanged();
    }

}
