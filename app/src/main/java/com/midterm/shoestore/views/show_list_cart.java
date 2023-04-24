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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;
import com.midterm.shoestore.model.ShoeItem;

import java.util.ArrayList;
import java.util.List;

public class show_list_cart extends AppCompatActivity {
    private Button btn_thanhtoan;
    private ImageView btn_left_cart;
    private GridView recyclerView;
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
        dialog = new Dialog(this);
        listquantity = new ArrayList<>();

        getID_quantity();

//        Toast.makeText(this, Integer.toString(listID.size()), Toast.LENGTH_SHORT).show();


        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(show_list_cart.this, Integer.toString(listID.size()), Toast.LENGTH_SHORT).show();
//                for(int i = 0; i < listID.size(); i++)
//                {
//                    Log.e("So Luong: " + i, Integer.toString(listquantity.get(i)));
//                    Log.e("ID :"+ i, listShoeCart.get(i).getShoe_ID());
//                }
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

            }
            return ConvertView;
        }
    }
}