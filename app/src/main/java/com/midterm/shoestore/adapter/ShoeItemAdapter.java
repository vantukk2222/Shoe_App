package com.midterm.shoestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midterm.shoestore.R;
import com.midterm.shoestore.model.ShoeItem;
import com.midterm.shoestore.views.HomeFragment;

import java.util.List;


public class ShoeItemAdapter extends RecyclerView.Adapter<ShoeItemAdapter.ShoeItemViewHolder> {


    private List<ShoeItem> shoeItemList;
    private final Context context;
    private final ShoeClickedListeners shoeClickedListeners;
    private List<ShoeItem> filteredList; // danh sách đã được lọc

    public ShoeItemAdapter(Context context, List<ShoeItem> shoeItemList, ShoeClickedListeners shoeClickedListeners){
        this.context = context;
        this.shoeItemList = shoeItemList;
        this.shoeClickedListeners = shoeClickedListeners;
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
    public void onBindViewHolder(@NonNull ShoeItemViewHolder holder, int position) {
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

        /*holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoeClickedListeners.onAddToCartBtnClicked(shoeItem);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        if (shoeItemList == null){
            return 0;
        }else{
            return shoeItemList.size();
        }
    }


    public class ShoeItemViewHolder extends RecyclerView.ViewHolder{
        private final ImageView shoeImageView;
        private ImageView addToCartBtn;
        private final TextView shoeNameTv;
        private final TextView shoeBrandNameTv;
        private final TextView shoePriceTv;
        private final CardView cardView;
        public ShoeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            shoeImageView = itemView.findViewById(R.id.eachShoeIv);
            shoeNameTv = itemView.findViewById(R.id.eachShoeName);
            shoePriceTv = itemView.findViewById(R.id.eachShoePriceTv);
            shoeBrandNameTv = itemView.findViewById(R.id.eachShoeBrandNameTv);

            cardView = itemView.findViewById(R.id.eachShoeCardView);
            //addToCartBtn = itemView.findViewById(R.id.eachShoeAddToCartBtn);
            //shoeNameTv = itemView.findViewById(R.id.eachShoeName);
            //shoeImageView = itemView.findViewById(R.id.eachShoeIv);
            //shoeBrandNameTv = itemView.findViewById(R.id.eachShoeBrandNameTv);
            //shoePriceTv = itemView.findViewById(R.id.eachShoePriceTv);
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


}
