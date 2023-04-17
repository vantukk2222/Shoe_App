package com.midterm.shoestore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midterm.shoestore.R;
import com.midterm.shoestore.model.ShoeItem;

import java.util.List;

public class ShoeItemAdapter extends RecyclerView.Adapter<ShoeItemAdapter.ShoeItemViewHolder> {

    private List<ShoeItem> shoeItemList;
    private DatabaseReference shoeRef;
    private ShoeClickedListeners shoeClickedListeners;

    public ShoeItemAdapter(ShoeClickedListeners shoeClickedListeners) {
        this.shoeClickedListeners = shoeClickedListeners;
        this.shoeRef = FirebaseDatabase.getInstance().getReference("shoes");
    }

    public void setShoeItemList(List<ShoeItem> shoeItemList) {
        this.shoeItemList = shoeItemList;
    }

    @NonNull
    @Override
    public ShoeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_shoe, parent, false);
        return new ShoeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeItemViewHolder holder, int position) {
        ShoeItem shoeItem = shoeItemList.get(position);
        holder.shoeNameTv.setText(shoeItem.getShoeName());
        holder.shoeBrandNameTv.setText(shoeItem.getShoeBrand());
        holder.shoePriceTv.setText(String.valueOf(shoeItem.getShoePrice()));
        Glide.with(holder.itemView.getContext()).load(shoeItem.getShoeImage()).into(holder.shoeImageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoeClickedListeners.onCardClicked(shoeItem);
            }
        });

        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoeClickedListeners.onAddToCartBtnClicked(shoeItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (shoeItemList == null) {
            return 0;
        } else {
            return shoeItemList.size();
        }
    }

    public class ShoeItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView shoeImageView, addToCartBtn;
        private TextView shoeNameTv, shoeBrandNameTv, shoePriceTv;
        private CardView cardView;

        public ShoeItemViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.eachShoeCardView);
            addToCartBtn = itemView.findViewById(R.id.eachShoeAddToCartBtn);
            shoeNameTv = itemView.findViewById(R.id.eachShoeName);
            shoeImageView = itemView.findViewById(R.id.eachShoeIv);
            shoeBrandNameTv = itemView.findViewById(R.id.eachShoeBrandNameTv);
            shoePriceTv = itemView.findViewById(R.id.eachShoePriceTv);
        }
    }

    public interface ShoeClickedListeners {
        void onCardClicked(ShoeItem shoe);

        void onAddToCartBtnClicked(ShoeItem shoeItem);
    }
}
