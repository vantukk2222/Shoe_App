package com.midterm.shoestore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midterm.shoestore.R;
import com.midterm.shoestore.model.ShoeItem;
import com.midterm.shoestore.views.HomeFragment;
import com.midterm.shoestore.views.LoginActivity;
import com.midterm.shoestore.views.MainActivity;
import com.midterm.shoestore.views.Main_Infor;
import com.midterm.shoestore.views.ShareFragment;
import com.midterm.shoestore.views.show_list_cart;

import java.util.List;
import java.util.NavigableMap;


public class ShoeItemAdapter extends RecyclerView.Adapter<ShoeItemAdapter.ShoeItemViewHolder> {


    private List<ShoeItem> shoeItemList;
    private final Context context;
    private final ShoeClickedListeners shoeClickedListeners;
    private List<ShoeItem> filteredList; // danh sách đã được lọc
    private String uid;
    private boolean isDeleteBtnVisible;
    private MenuItem menuItem;


    public ShoeItemAdapter(Context context, List<ShoeItem> shoeItemList, ShoeClickedListeners shoeClickedListeners){
        this.context = context;
        this.shoeItemList = shoeItemList;
        this.shoeClickedListeners = shoeClickedListeners;
        this.isDeleteBtnVisible = false;

    }
    public void setShoeItemList(List<ShoeItem> shoeItemList){
        this.shoeItemList = shoeItemList;
    }

    @NonNull
    @Override
    public ShoeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_shoe , parent , false);
        return new ShoeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ShoeItem shoeItem = shoeItemList.get(position);


        //holder.shoeNameTv.setText(shoeItem.getShoeName());
        //holder.shoeBrandNameTv.setText(shoeItem.getShoeBrandName());
        //holder.shoePriceTv.setText(String.valueOf(shoeItem.getShoePrice()));
        //holder.shoeImageView.setImageResource(shoeItem.getShoeImage());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shoeClickedListeners.onCardClicked(shoeItem);
            }
        });


        if (shoeItemList != null && shoeItemList.size() > position && shoeItemList.get(position) != null && shoeItemList.get(position).getShoeImage() != null) {
            //Glide.with(context).load(shoeItemList.get(position).getShoeImage()).into(holder.shoeImageView);
            Glide.with(context).load(shoeItemList.get(position).getShoeImage() != null ? shoeItemList.get(position).getShoeImage() : "https://icon-library.com/images/empty-icon/empty-icon-19.jpg").into(holder.shoeImageView);
        } else {

        }


        //Glide.with(context).load(shoeItemList.get(position).getShoeImage()).into(holder.shoeImageView);

        holder.shoePriceTv.setText(new StringBuilder("$").append(shoeItemList.get(position).getShoePrice()));
        holder.shoeNameTv.setText(new StringBuilder().append(shoeItemList.get(position).getShoeName()));
        holder.shoeBrandNameTv.setText(new StringBuilder().append(shoeItemList.get(position).getShoeBrandName()));

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference shoeRef = FirebaseDatabase.getInstance().getReference("shoes").child(shoeItem.getShoeID());

                // Gọi phương thức remove() để xóa giày đó khỏi database
                shoeRef.removeValue();
                shoeItemList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, shoeItemList.size());
            }
        });
        /*holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoeClickedListeners.onAddToCartBtnClicked(shoeItem);
            }
        });*/
        /*if(isDeleteBtnVisible) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context.getApplicationContext());
        this.uid = preferences.getString("uid", "");
        if (uid.equals("admin")) {
            holder.addToCartBtn.setVisibility(View.GONE);
            //holder.deleteBtn.setVisibility(View.GONE);
            /*switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    holder.deleteBtn.setVisibility(View.GONE);
                    break;

                case R.id.nav_admin:
                    holder.deleteBtn.setVisibility(View.VISIBLE);
                    break;
            }*/
        }
        else if(!uid.isEmpty()) {
            holder.addToCartBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (shoeItemList == null){
            return 0;
        }else{
            return shoeItemList.size();
        }
    }

    public void setDeleteBtnVisible(boolean deleteBtnVisible) {
        isDeleteBtnVisible = deleteBtnVisible;
        notifyDataSetChanged();
    }


    public class ShoeItemViewHolder extends RecyclerView.ViewHolder{
        private final ImageView shoeImageView;
        private ImageView addToCartBtn;
        private final TextView shoeNameTv;
        private final TextView shoeBrandNameTv;
        private final TextView shoePriceTv;
        private final CardView cardView;
        public ImageButton deleteBtn;

        public ShoeItemViewHolder(@NonNull View itemView) {
            super(itemView);

            shoeImageView = itemView.findViewById(R.id.eachShoeIv);
            shoeNameTv = itemView.findViewById(R.id.eachShoeName);
            shoePriceTv = itemView.findViewById(R.id.eachShoePriceTv);
            shoeBrandNameTv = itemView.findViewById(R.id.eachShoeBrandNameTv);

            cardView = itemView.findViewById(R.id.eachShoeCardView);
            addToCartBtn = itemView.findViewById(R.id.eachShoeAddToCartBtn);
            deleteBtn = itemView.findViewById(R.id.eachShoeDeleteBtn);
        }
    }

    public interface ShoeClickedListeners{
        void onCardClicked(ShoeItem shoe);
        //void onAddToCartBtnClicked(ShoeItem shoeItem);
    }
    public void updateList(List<ShoeItem> newItems) {
        shoeItemList = newItems;
        notifyDataSetChanged();
    }
    public void setMenuItem(MenuItem item){
        this.menuItem = item;
    }


}
