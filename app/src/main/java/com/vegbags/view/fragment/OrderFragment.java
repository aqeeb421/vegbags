package com.vegbags.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vegbags.ResponseModels.OrderResponseModel;
import com.vegbags.adapter.OrderAdapter;
import com.vegbags.adapter.OrderClickInterface;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.databinding.FragmentOrderBinding;
import com.vegbags.utils.CommonUtils;
import com.vegbags.view.activity.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment implements OrderClickInterface {


    public OrderFragment() {
        // Required empty public constructor
    }

    FragmentOrderBinding orderBinding;
    OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        orderBinding = FragmentOrderBinding.inflate(inflater, container, false);
        orderBinding.shimmerFrameLayout.startShimmerAnimation();

        GetOrders();

        return orderBinding.getRoot();
    }

    private void GetOrders() {

        String token = CommonUtils.getToken();

        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<List<OrderResponseModel>> modelCall = uploadData.getOrders(token);

        modelCall.enqueue(new Callback<List<OrderResponseModel>>() {
            @Override
            public void onResponse(Call<List<OrderResponseModel>> call, Response<List<OrderResponseModel>> response) {
                orderBinding.shimmerFrameLayout.stopShimmerAnimation();
                orderBinding.shimmerFrameLayout.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().size() > 0) {
                    List<OrderResponseModel> ordersModels = response.body();
                    setAdapter(ordersModels);
                } else {
                    orderBinding.orderRecyclerView.setVisibility(View.GONE);
                    orderBinding.noDara.setVisibility(View.VISIBLE);
                }
                if (response.code() == 401) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponseModel>> call, Throwable t) {
                orderBinding.shimmerFrameLayout.stopShimmerAnimation();
                orderBinding.shimmerFrameLayout.setVisibility(View.GONE);
                orderBinding.noDara.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapter(List<OrderResponseModel> ordersModels) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        orderBinding.orderRecyclerView.setLayoutManager(manager);
        orderAdapter = new OrderAdapter(ordersModels, this::onItemClick);
        orderBinding.orderRecyclerView.setHasFixedSize(true);
        orderBinding.orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderBinding.orderRecyclerView.setAdapter(orderAdapter);
    }


    @Override
    public void onItemClick(OrderResponseModel ordersModel) {

        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetFragment(ordersModel);

        Bundle args = new Bundle();
        args.putString("orderID", ordersModel.getOrderId());
        args.putString("date", ordersModel.getUpdatedAt());
        args.putString("status", ordersModel.getStatus());

        bottomSheetDialogFragment.setArguments(args);
        bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}