package com.vegbags.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.vegbags.R;
import com.vegbags.ResponseModels.CartResponseModel;
import com.vegbags.ResponseModels.ProductModel;
import com.vegbags.adapter.ProductAdapter;
import com.vegbags.adapter.RecyclerInterface;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.utils.CommonUtils;
import com.vegbags.view.activity.CartActivity;
import com.vegbags.view.activity.LoginActivity;
import com.vegbags.view.activity.ProductActivity;

import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements RecyclerInterface {


    public HomeFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "HomeFragment";
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ImageView notification, cart;
    CarouselView customCarouselView;
    int NUMBER_OF_PAGES = 3;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView cartCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        cart = view.findViewById(R.id.cart);
        notification = view.findViewById(R.id.notification);
        cartCount = view.findViewById(R.id.cartCount);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout.startShimmerAnimation();
        GetCartItems();


        notification.setOnClickListener(view1 -> Toast.makeText(getContext(), "Notification", Toast.LENGTH_SHORT).show());
        cart.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), CartActivity.class));
        });


        customCarouselView = (CarouselView) view.findViewById(R.id.CarouselView);
        customCarouselView.setPageCount(NUMBER_OF_PAGES);
        // set ViewListener for custom view
        customCarouselView.setViewListener(viewListener);

        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<List<ProductModel>> modelCall = uploadData.getProducts();
        modelCall.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(manager);

                    List<ProductModel> productModelList = response.body();

                    productAdapter = new ProductAdapter(productModelList, HomeFragment.this::onItemClick);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.e(TAG, "onResponse: " + t.getMessage());
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

        return view;
    }

    ViewListener viewListener = position -> {
        View customView = getLayoutInflater().inflate(R.layout.view_custom, null);
        //set view attributes here
        TextView offer_tittle, offer_desc;
        ImageView offer_img;

        offer_tittle = customView.findViewById(R.id.offer_tittle);
        offer_desc = customView.findViewById(R.id.offer_desc);
        offer_img = customView.findViewById(R.id.offer_img);

        switch (position) {
            case 0:
               /* offer_tittle.setText("bchbchbc");
                offer_desc.setText("ncbhcbhjcn");*/
                Picasso.get().load(R.drawable.offer1).fit().transform(new BlurTransformation(getActivity(), 3)).into(offer_img);
                break;
            case 1:
                Picasso.get().load(R.drawable.offer2).transform(new BlurTransformation(getActivity(), 3)).fit().into(offer_img);
                break;
            case 2:
                Picasso.get().load(R.drawable.offer3).transform(new BlurTransformation(getActivity(), 3)).fit().into(offer_img);
                break;
        }

        return customView;
    };

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ProductActivity.class);
        intent.putExtra("categoryID", String.valueOf(position));
        startActivity(intent);
    }

    private void GetCartItems() {
        String token = CommonUtils.getToken();
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<List<CartResponseModel>> modelCall = uploadData.getCartItems(token);
        modelCall.enqueue(new Callback<List<CartResponseModel>>() {
            @Override
            public void onResponse(Call<List<CartResponseModel>> call, Response<List<CartResponseModel>> response) {
                try {
                    if (response.code() == 200 && response.body().get(0).getCartItems().size() > 0) {
                        Log.d(TAG, "onResponse: " + response.body().get(0).getCartItems().size());
                        CommonUtils.setCartCount(String.valueOf(response.body().get(0).getCartItems().size()));
                    } else {
                        CommonUtils.setCartCount("0");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: "+e.getMessage() );
                }
                if (response.code() == 401) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }

                if (!CommonUtils.getCartCount().equalsIgnoreCase(""))
                    cartCount.setText(CommonUtils.getCartCount());
                else
                    cartCount.setText("0");
            }

            @Override
            public void onFailure(Call<List<CartResponseModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
                if (!CommonUtils.getCartCount().equalsIgnoreCase(""))
                    cartCount.setText(CommonUtils.getCartCount());
                else
                    cartCount.setText("0");
            }
        });
    }
}