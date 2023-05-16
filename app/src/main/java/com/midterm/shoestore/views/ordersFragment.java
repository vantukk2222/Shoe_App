package com.midterm.shoestore.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;
import com.midterm.shoestore.adapter.OrderItemAdapter;
import com.midterm.shoestore.adapter.ShoeItemAdapter;
import com.midterm.shoestore.listener.OrderItemLoadListener;
import com.midterm.shoestore.listener.ShoeItemLoadListener;
import com.midterm.shoestore.model.Order;
import com.midterm.shoestore.model.ShoeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ordersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ordersFragment extends Fragment implements OrderItemAdapter.OrderClickedListeners, OrderItemLoadListener {

    private static String searchQuery;
    @BindView(R.id.orders_fragment)
    FrameLayout mainLayout;
    @BindView(R.id.main_orders_RecyclerView)
    RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Order> orderItemList;
    private List<Order> orderItemListFilter;
    private String status;

    private OrderItemAdapter adapter;

    OrderItemLoadListener OrderItemLoadListener;


    public ordersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ordersFragment newInstance(String param1, String param2) {
        ordersFragment fragment = new ordersFragment();
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
            status = getArguments().getString("MY_STRING");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeVariables();

        recyclerView = view.findViewById(R.id.main_orders_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter = new OrderItemAdapter(getActivity(), orderItemList, this);

        //setUpList();
        loadItemFromFirebase();
        adapter.setOrderItemList(orderItemList);
        recyclerView.setAdapter(adapter);

    }

    private void loadItemFromFirebase() {
        orderItemList = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uid = preferences.getString("uid", "");
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        Query query = ordersRef.orderByChild("userId").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    orderItemList.add(order);
                }
                // sử dụng orderList ở đây
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // xử lý khi có lỗi
            }
        });


    }

    private void initializeVariables() {
        orderItemList = new ArrayList<>();
        OrderItemLoadListener = this;
    }

    @Override
    public void onCardClicked(Order order) {
    }
    public void onSearchQueryChanged(String query) {
        searchQuery = query;
        updateItemList();
    }

    private void updateItemList() {
        if (searchQuery.isEmpty()) {
            // Nếu không có từ khóa tìm kiếm, hiển thị toàn bộ danh sách
            orderItemList = orderItemListFilter;
        } else {
            // Nếu có từ khóa tìm kiếm, lọc danh sách
            orderItemListFilter = filterItemListByKeyword(orderItemList, searchQuery);
        }
        adapter.updateList(orderItemListFilter);
        recyclerView.setAdapter(adapter);
    }

    private List<Order> filterItemListByKeyword(List<Order> originalList, String keyword) {
        List<Order> filteredList = new ArrayList<>();
        for (Order item : originalList) {
            if (item.getStatus().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    @Override
    public void onOrderItemLoadSuccess(List<Order> ordersItemList) {
        this.orderItemList = ordersItemList;
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(getActivity(), orderItemList, this);
        recyclerView.setAdapter(orderItemAdapter);
    }

    @Override
    public void onOrderItemLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

}