package com.midterm.shoestore.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;
import com.midterm.shoestore.adapter.ShoeItemAdapter;
import com.midterm.shoestore.listener.ShoeItemLoadListener;
import com.midterm.shoestore.model.ShoeItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ShoeItemAdapter.ShoeClickedListeners, ShoeItemLoadListener {

    @BindView(R.id.home_fragment)
    FrameLayout mainLayout;
    @BindView(R.id.mainRecyclerView)
    RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//    private ShoeItemAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<ShoeItem> shoeItemList;
    private List<ShoeItem> shoeItemListFilter;
    private String searchQuery = "";

    private ShoeItemAdapter adapter;

    ShoeItemLoadListener shoeItemLoadListener;


    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeVariables();

        recyclerView = view.findViewById(R.id.mainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ShoeItemAdapter(getActivity(), shoeItemList, this);

        //setUpList();
        loadItemFromFirebase();
        adapter.setShoeItemList(shoeItemList);
        recyclerView.setAdapter(adapter);

    }

    private void loadItemFromFirebase() {
        shoeItemList = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("shoes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot itemSnapshot:snapshot.getChildren()){
                                ShoeItem shoeItem = itemSnapshot.getValue(ShoeItem.class);
                                shoeItem.setShoe_ID(itemSnapshot.getKey());
                                shoeItemList.add(shoeItem);
                            }
                            shoeItemLoadListener.onShoeItemLoadSuccess(shoeItemList);

                        }
                        else
                            shoeItemLoadListener.onShoeItemLoadFailed("Can't find Shoe");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        shoeItemLoadListener.onShoeItemLoadFailed(error.getMessage());

                    }
                });
    }

    private void initializeVariables() {
        shoeItemList = new ArrayList<>();
        shoeItemLoadListener = this;
    }
    /*private void setUpList() {
        shoeItemList.add(new ShoeItem(1,"Nike Revolution", "Nike", R.drawable.nike_revolution_road, 15));
        shoeItemList.add(new ShoeItem(2,"Nike Flex Run 2021", "NIKE", R.drawable.flex_run_road_running, 20));
        shoeItemList.add(new ShoeItem(3,"Court Zoom Vapor", "NIKE", R.drawable.nikecourt_zoom_vapor_cage, 18));
        shoeItemList.add(new ShoeItem(4,"EQ21 Run COLD.RDY", "ADIDAS", R.drawable.adidas_eq_run, 16.5));
        shoeItemList.add(new ShoeItem(5,"Adidas Ozelia", "ADIDAS", R.drawable.adidas_ozelia_shoes_grey, 20));
        shoeItemList.add(new ShoeItem(6,"Adidas Questar", "ADIDAS", R.drawable.adidas_questar_shoes, 22));
        shoeItemList.add(new ShoeItem(7,"Adidas Questar", "ADIDAS", R.drawable.adidas_questar_shoes, 12));
        shoeItemList.add(new ShoeItem(8,"Adidas Ultraboost", "ADIDAS", R.drawable.adidas_ultraboost, 15));


    }*/

    @Override
    public void onCardClicked(ShoeItem shoe) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uid = preferences.getString("uid", "");
        if(uid.equals("admin"))
        {

        }
        else{
            Intent intent = new Intent(getContext(), DetailedActivity.class);
            intent.putExtra("shoeItem", shoe);
            startActivity(intent);
        }
    }


    public void onSearchQueryChanged(String query) {
        searchQuery = query;
        updateItemList();
    }

    private List<ShoeItem> getListOfShoeItems() {
//        setUpList();
        return null;
    }

    private void updateItemList() {
        if (searchQuery.isEmpty()) {
            // Nếu không có từ khóa tìm kiếm, hiển thị toàn bộ danh sách
            shoeItemListFilter = shoeItemList;
        } else {
            // Nếu có từ khóa tìm kiếm, lọc danh sách
            shoeItemListFilter = filterItemListByKeyword(shoeItemList, searchQuery);
        }
        adapter.updateList(shoeItemListFilter);
        recyclerView.setAdapter(adapter);
    }

    private List<ShoeItem> filterItemListByKeyword(List<ShoeItem> originalList, String keyword) {
        List<ShoeItem> filteredList = new ArrayList<>();
        for (ShoeItem item : originalList) {
            if (item.getShoeName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }


    @Override
    public void onShoeItemLoadSuccess(List<ShoeItem> shoeItemList) {
        this.shoeItemList = shoeItemList;
        ShoeItemAdapter shoeItemAdapter = new ShoeItemAdapter(getActivity(), shoeItemList, this);
        recyclerView.setAdapter(shoeItemAdapter);
    }

    @Override
    public void onShoeItemLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();

    }
}