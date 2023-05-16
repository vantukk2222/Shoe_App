package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.midterm.shoestore.R;
import com.midterm.shoestore.adapter.CheckoutItemAdapter;
import com.midterm.shoestore.adapter.OrderItemAdapter;
import com.midterm.shoestore.model.Order;
import com.midterm.shoestore.model.ShoeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class detail_checkoutActivity extends AppCompatActivity {


    private Order order;
    private Map<String, Integer> shoeQuantities;
    private TextView tvleft_co_item, textview_order_number, textview_order_date, textview_placed_date, textview_order_status, textview_order_total;
    private ImageView img_checkcart_co_item;
    private Button cancel_order;
    private GridView RecyView_DSShoes_detail_checkout;


    private List<Integer> QuantityList;
    private List<String> ShoeIDList;

    private CheckoutItemAdapterCustom adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_checkout);
        order = getIntent().getParcelableExtra("Order");
        Bundle bundle = getIntent().getBundleExtra("bundle_key");
        shoeQuantities = (Map<String, Integer>) bundle.getSerializable("shoe_quantities");

        QuantityList = new ArrayList<>(shoeQuantities.values());
        ShoeIDList = new ArrayList<>(shoeQuantities.keySet());

        tvleft_co_item = findViewById(R.id.tvleft_co_item);
        textview_order_number = findViewById(R.id.textview_order_number);
        textview_order_date = findViewById(R.id.textview_order_date);
        textview_placed_date = findViewById(R.id.textview_placed_date);
        textview_order_status = findViewById(R.id.textview_order_status);
        textview_order_total = findViewById(R.id.textview_order_total);
        img_checkcart_co_item = findViewById(R.id.img_checkcart_co_item);
        cancel_order = findViewById(R.id.cancel_order);


        RecyView_DSShoes_detail_checkout = findViewById(R.id.RecyView_DSShoes_detail_checkout);

        adapter = new CheckoutItemAdapterCustom(ShoeIDList, QuantityList, this);
        RecyView_DSShoes_detail_checkout.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        textview_order_number.setText("Mã đơn hàng: " +order.getOrderId());
        textview_order_date.setText("Thời gian đặt hàng: " +order.getTimePlaced());
        textview_placed_date.setText("Thời gian giao hàng: " +order.getTimeDelivered());
        textview_order_status.setText("Trạng thái giao hàng: " +order.getStatus());
        textview_order_total.setText("Tổng tiền: " +order.getTotalPrice());

        tvleft_co_item.setOnClickListener(view -> finish());

        img_checkcart_co_item.setOnClickListener(view ->{
            Intent intent_cart = new Intent(getApplicationContext(), show_list_cart.class);
            startActivity(intent_cart);
        });
        cancel_order.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hủy đơn hàng");
            builder.setMessage("Bạn có chắc muốn hủy đơn hàng chứ?");

            // bấm Yes
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String IDOrder = order.getOrderId();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ordersRef = database.getReference("orders");

                    ordersRef.child(IDOrder).child("status").setValue("cancelled", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error != null) {
                                // Xử lý khi có lỗi xảy ra
                            } else {
                                // Xử lý khi sửa giá trị thuộc tính thành công
                                Toast.makeText(detail_checkoutActivity.this, "Hủy thành công!", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
            });

            // No
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });


            builder.show();
        });

    }


    private class CheckoutItemAdapterCustom extends BaseAdapter {

        private List<String> IDShoeList;
        private List<Integer> QuantityList;
        private  Context context;

        public CheckoutItemAdapterCustom(List<String> IDShoeList, List<Integer> quantityList, Context context) {
            this.IDShoeList = IDShoeList;
            QuantityList = quantityList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return this.QuantityList.size();
        }

        @Override
        public Object getItem(int i) {
            return this.QuantityList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View ConvertView, ViewGroup parent) {
            View view1 = ConvertView;
            if(view1 == null)
            {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ConvertView = inflater.inflate(R.layout.detail_checkout_item, null);
            }
            if(QuantityList.size() > 0)
            {


                ImageView imageview_ordered_product = ConvertView.findViewById(R.id.detail_imageview_ordered_product);
                TextView textview_ordered_product_name =  ConvertView.findViewById(R.id.detail_textview_ordered_product_name);
                TextView textview_ordered_product_price = ConvertView.findViewById(R.id.detail_textview_ordered_product_price);
                TextView textview_ordered_product_quantity =  ConvertView.findViewById(R.id.detail_textview_ordered_product_quantity);
                String shoeID = IDShoeList.get(i);
                Integer quantity = QuantityList.get(i);

                DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference("shoes");
                Query query = shoesRef.orderByKey().equalTo(shoeID);
                Integer finalQuantity = quantity;
                View finalConvertView = ConvertView;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Lấy ra Shoes được trả về từ dataSnapshot
                            ShoeItem shoes = dataSnapshot.getChildren().iterator().next().getValue(ShoeItem.class);
                            Glide.with(context).load(shoes.getShoeImage()).into(imageview_ordered_product);
                            textview_ordered_product_name.setText(shoes.getShoeName());
                            textview_ordered_product_price.setText("Giá $" + shoes.getShoePrice());
                            textview_ordered_product_quantity.setText("Số lượng: " + finalQuantity);
                            finalConvertView.setOnClickListener(view -> {
                                Intent intent = new Intent(context, DetailedActivity.class);
                                intent.putExtra("shoeItem", shoes);
                                startActivity(intent);
                            });

                        } else {
                            Toast.makeText(context, "Không tìm thấy giày nào", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
            }
            return ConvertView;
        }
    }
}