package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.midterm.shoestore.R;
import com.midterm.shoestore.model.Order;
import com.midterm.shoestore.model.ShoeItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class show_list_cart extends AppCompatActivity {
    private Button btn_thanhtoan;
    private ImageView btn_left_cart;
    private GridView recyclerView;
    private TextView tv_tienthanhtoan;
    private Dialog dialog;


    private ArrayList<String> listID;
    private ArrayList<Integer> listquantity;
    private AdapterCustom donhangadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.layout_show_list_cart);
        btn_left_cart = findViewById(R.id.imgleftlistcart);
        btn_thanhtoan = findViewById(R.id.bt_thanhtoantien_list_cart);
        tv_tienthanhtoan = findViewById(R.id.tv_tienthanhtoan);
        dialog = new Dialog(this);
        listquantity = new ArrayList<>();

        getID_quantity();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");
        String userId = uid; // id của user tương ứng
//        Toast.makeText(this, Integer.toString(listID.size()), Toast.LENGTH_SHORT).show();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId).child("shoes");
        DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference("shoes");

        final int[] total = {0};
        cartRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {
                String shoeId = snapshot.getKey();
                int quantity = snapshot.child("quantity").getValue(Integer.class);

                // Get shoe info with the given shoeId
                shoesRef.child(shoeId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String shoeBrandName = snapshot.child("shoeBrandName").getValue(String.class);
                        String shoeImage = snapshot.child("shoeImage").getValue(String.class);
                        String shoeName = snapshot.child("shoeName").getValue(String.class);
                        String str_shoePrice = snapshot.child("shoePrice").getValue(String.class);
                        int shoePrice = Integer.valueOf(str_shoePrice.replace(",",""));

                        total[0] += shoePrice * quantity;
                        tv_tienthanhtoan.setText("$"+total[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin giỏ hàng của user
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("cart").child(userId);
                cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> cartData = (Map<String, Object>) dataSnapshot.getValue();
                        Map<String, Integer> shoeQuantities = new HashMap<>();
                        for (DataSnapshot shoeSnapshot : dataSnapshot.child("shoes").getChildren()) {
                            String shoeId = shoeSnapshot.getKey();
                            int quantity = shoeSnapshot.child("quantity").getValue(Integer.class);
                            shoeQuantities.put(shoeId, quantity);
                        }

                        // Tạo một đối tượng Order
                        String orderId = UUID.randomUUID().toString(); // Tạo một ID ngẫu nhiên cho đơn hàng
                        String status = "pending"; // Trạng thái ban đầu của đơn hàng
                        Long timestamp = System.currentTimeMillis(); // Thời điểm đặt hàng
                        Date date = new Date(timestamp);

                        // Chuyển đổi đối tượng Date thành định dạng ngày tháng năm giờ phút giây
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String formattedDate = dateFormat.format(date);
                        Order order = new Order(orderId, userId, status, Integer.toString(total[0]), shoeQuantities, formattedDate, "");

                        // Lưu đơn hàng vào Realtime Database
                        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");
                        ordersRef.child(orderId).setValue(order)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Đã lưu đơn hàng thành công
                                        Log.e("Create Order", "OK Created!");
                                        cartRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Xóa shoes thành công
                                                        Log.e("Remove Cart", "OK removed!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Xảy ra lỗi khi xóa shoes

                                                        Log.e("Remove Cart", "Failed removed!");
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xảy ra lỗi khi lưu đơn hàng
                                        Log.e("Create Order", "Failed Created!");
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xảy ra lỗi khi lấy thông tin giỏ hàng
                    }
                });

                opendialog();
            }
        });
        btn_left_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getID_quantity()
    {

        listID = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");
        String userId = uid; // id của user tương ứng

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cartRef = database.getReference("cart");

        cartRef.child(userId).child("shoes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Duyệt qua tất cả các node con của "shoes"
                for (DataSnapshot shoeSnapshot : snapshot.getChildren()) {
                    String shoeId = shoeSnapshot.getKey();
                    int shoeQuantity = shoeSnapshot.child("quantity").getValue(Integer.class);

                    listquantity.add(shoeQuantity);
                    listID.add(shoeId);
                }
                donhangadapter = new AdapterCustom(show_list_cart.this, listID, listquantity);
                recyclerView = findViewById(R.id.RecyView_DSShoes_listcart);
                recyclerView.setAdapter(donhangadapter);

                donhangadapter.notifyDataSetChanged();

                // TODO: Sử dụng danh sách shoeItemList để hiển thị sản phẩm trong giỏ hàng của user
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GET_ID_SHOE_IN_CART", "Error retrieving shoes in cart for user", error.toException());
            }
        });
    }
    private void opendialog() {
        dialog.setContentView(R.layout.layout_thanhtoan);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView OkeyText = dialog.findViewById(R.id.okay_text);
        OkeyText.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.show();
    }
    public class AdapterCustom extends BaseAdapter{
        private Context ct;
        private ArrayList<String> List_ID;
        private ArrayList<Integer> List_Quantity;

        public AdapterCustom(Context context, ArrayList<String> list_ID, ArrayList<Integer> list_Quantity)
        {
            this.ct = context;
            this.List_ID = list_ID;
            this.List_Quantity = list_Quantity;
        }

        @Override
        public int getCount() {
            return this.List_ID.size();
        }

        @Override
        public Object getItem(int i) {
            return this.List_ID.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View ConvertView, ViewGroup parent) {

            View view1 = ConvertView;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String uid = preferences.getString("uid", "");
            String userId = uid; // id của user tương ứng
            if(view1 == null)
            {
                LayoutInflater inflater = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ConvertView = inflater.inflate(R.layout.each_cart_item, null);
            }
            if(this.List_ID.size() > 0 )
            {
                ImageView eachCartItemIV = ConvertView.findViewById(R.id.eachCartItemIV);
                ImageView eachCartItemDeleteBtn = ConvertView.findViewById(R.id.eachCartItemDeleteBtn);

                TextView eachCartItemName = ConvertView.findViewById(R.id.eachCartItemName);
                TextView eachCartItemBrandNameTv = ConvertView.findViewById(R.id.eachCartItemBrandNameTv);
                TextView eachCartItemPriceTv = ConvertView.findViewById(R.id.eachCartItemPriceTv);
                TextView eachCartItemQuantityTV = ConvertView.findViewById(R.id.eachCartItemQuantityTV);

                ImageButton eachCartItemMinusQuantityBtn = ConvertView.findViewById(R.id.eachCartItemMinusQuantityBtn);
                ImageButton eachCartItemAddQuantityBtn = ConvertView.findViewById(R.id.eachCartItemAddQuantityBtn);




                eachCartItemMinusQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity_item = Integer.valueOf(eachCartItemQuantityTV.getText().toString().trim());
                        if(quantity_item > 1)
                        {
                            int Price_Item = Integer.valueOf(eachCartItemPriceTv.getText().toString().trim().replace("$",""));

                            String strmoney = tv_tienthanhtoan.getText().toString().trim().replace("$","");
                            int money_all = Integer.valueOf(strmoney);
                            int money_after = money_all - Price_Item;

                            eachCartItemQuantityTV.setError(null);
                            quantity_item = quantity_item - 1;
                            eachCartItemQuantityTV.setText(Integer.toString(quantity_item));

                            tv_tienthanhtoan.setText("$"+Integer.toString(money_after));

                            String shoeId = List_ID.get(position);
                            DatabaseReference cartItemRef = FirebaseDatabase.getInstance().getReference().child("cart").child(userId).child("shoes").child(shoeId);
                            cartItemRef.runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                    Integer currentQuantity = mutableData.child("quantity").getValue(Integer.class);
                                    int newQuantity = currentQuantity >  1 ? currentQuantity - 1 : 1;
                                    mutableData.child("quantity").setValue(newQuantity);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                    if (databaseError != null) {
                                        // Xảy ra lỗi khi cập nhật giá trị
                                        Log.e("Update_Quantity", "Update -1 quantity failed!", databaseError.toException());
                                    } else {
                                        // Cập nhật giá trị thành công
                                        Log.e("Update_Quantity", "Quantity -1 updated successfully!");
                                    }
                                }
                            });

                        }
                        else
                        {
                            eachCartItemQuantityTV.setError("Số lượng lớn hơn 0");
                            eachCartItemQuantityTV.setText("1");
                        }

                    }
                });
                eachCartItemAddQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity_item = Integer.valueOf(eachCartItemQuantityTV.getText().toString().trim());
                        int Price_Item = Integer.valueOf(eachCartItemPriceTv.getText().toString().trim().replace("$",""));

                        String strmoney = tv_tienthanhtoan.getText().toString().trim().replace("$","");
                        int money_all = Integer.valueOf(strmoney);
                        int money_after = money_all + Price_Item;

                        eachCartItemQuantityTV.setError(null);
                        quantity_item = quantity_item + 1;
                        eachCartItemQuantityTV.setText(Integer.toString(quantity_item));
                        String shoeId = List_ID.get(position);
                        DatabaseReference cartItemRef = FirebaseDatabase.getInstance().getReference().child("cart").child(userId).child("shoes").child(shoeId);
                        cartItemRef.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                Integer currentQuantity = mutableData.child("quantity").getValue(Integer.class);
                                int newQuantity = currentQuantity != null ? currentQuantity + 1 : 0;
                                mutableData.child("quantity").setValue(newQuantity);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                if (databaseError != null) {
                                    // Xảy ra lỗi khi cập nhật giá trị
                                    Log.e("Update_Quantity", "Update +1 quantity failed!", databaseError.toException());
                                } else {
                                    // Cập nhật giá trị thành công
                                    Log.e("Update_Quantity", "Quantity +1 updated successfully!");
                                }
                            }
                        });

                        tv_tienthanhtoan.setText("$"+Integer.toString(money_after));
                    }
                });
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference shoesRef = database.getReference("shoes");
                String shoeIdToFind = List_ID.get(position);
                shoesRef.child(shoeIdToFind).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String shoeName = snapshot.child("shoeName").getValue(String.class);
                            String shoeBrandName = snapshot.child("shoeBrandName").getValue(String.class);
                            String shoeImage = snapshot.child("shoeImage").getValue(String.class);
                            String shoePrice = snapshot.child("shoePrice").getValue(String.class);
                            eachCartItemName.setText(shoeName);
                            eachCartItemBrandNameTv.setText(shoeBrandName);
                            eachCartItemPriceTv.setText("$"+shoePrice);
                            eachCartItemQuantityTV.setText(Integer.toString(List_Quantity.get(position)));
                            Glide.with(show_list_cart.this).load(shoeImage).into(eachCartItemIV);

                            // TODO: Sử dụng thông tin của shoe tìm được để hiển thị hoặc thêm vào giỏ hàng của user.
                        } else {
                            Log.e("get_shoe_item_cart", "No shoe found with ID " + shoeIdToFind);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("get_shoe_item_cart", "Error retrieving shoe with ID " + shoeIdToFind, error.toException());
                    }
                });

                eachCartItemDeleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String shoeIdToDel = shoeIdToFind;
                        DatabaseReference cartRef = database.getReference("cart");


                        cartRef.child(userId).child("shoes").child(shoeIdToDel).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error != null) {
                                    Log.e("Remove shoe from cart", "Error removing shoe from cart", error.toException());
                                } else {
                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listID.remove(position);
                                            listquantity.remove(position);
                                            donhangadapter = new AdapterCustom(show_list_cart.this, listID, listquantity);
                                            recyclerView.setAdapter(donhangadapter);

                                        }
                                    });
                                    Log.e("Remove shoe from cart", "Shoe removed successfully from cart");
                                }
                            }
                        });
                    }
                });
            }
            return ConvertView;
        }
    }

}