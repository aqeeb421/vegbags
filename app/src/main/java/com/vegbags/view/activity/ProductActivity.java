package com.vegbags.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.vegbags.Models.CartItem;
import com.vegbags.Models.ListCart;
import com.vegbags.R;
import com.vegbags.ResponseModels.Medium;
import com.vegbags.ResponseModels.ProductModel;
import com.vegbags.ResponseModels.ResponseProductTypeModel;
import com.vegbags.adapter.ItemAdapter;
import com.vegbags.adapter.TypeRecyclerInterface;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ProductActivity extends AppCompatActivity implements TypeRecyclerInterface {

    TextView product_name, product_desc;
    LinearLayout product_lnr1, product_lnr2;
    CarouselView customCarouselView;
    int NUMBER_OF_PAGES = 3;
    List<Medium> media;
    int count = 1, productID = 0;
    Button addToCart;

    ShimmerFrameLayout shimmerProductLayout;
    LinearLayoutCompat llCounter;
    RecyclerView item_recyclerView;
    Map<Integer, CartItem> itemList = new HashMap<>();
    ImageView img_back, product_image01;
    ProgressBar progress_circular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        product_name = findViewById(R.id.product_name);
        product_desc = findViewById(R.id.product_desc);
        product_lnr1 = findViewById(R.id.product_lnr1);
        product_lnr2 = findViewById(R.id.product_lnr2);
        addToCart = findViewById(R.id.addToCart);
        img_back = findViewById(R.id.img_back);
        progress_circular = findViewById(R.id.progress_circular);
        item_recyclerView = findViewById(R.id.item_recyclerView);
        customCarouselView = (CarouselView) findViewById(R.id.product_image);
        product_image01 = findViewById(R.id.product_image01);
        shimmerProductLayout = findViewById(R.id.shimmerProductLayout);
        shimmerProductLayout.startShimmerAnimation();

        img_back.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("home", true);
            startActivity(intent);
            ProductActivity.this.finish(); // if the activity running has it's own context

        });

        addToCart.setOnClickListener(view -> {
            List<CartItem> list = new ArrayList<CartItem>(itemList.values());

            if (list.size() > 0) {
                AddToCart(list);
            } else {
                Toast.makeText(this, "Please add any item", Toast.LENGTH_SHORT).show();
            }

        });

        String categoryId1 = getIntent().getStringExtra("categoryID");
        int categoryId = Integer.parseInt(categoryId1);

        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<ProductModel> modelCall = uploadData.getProduct(categoryId1);
        modelCall.enqueue(new Callback<ProductModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    shimmerProductLayout.stopShimmerAnimation();
                    shimmerProductLayout.setVisibility(View.GONE);

                    ProductModel productModels = response.body();
                    assert productModels != null;
                    product_name.setText(productModels.getName());
                    product_desc.setText(productModels.getDescription());
                    CommonUtils.setProductId(productModels.getId());
                    productID = productModels.getId();
                    media = productModels.getMedia();

                    // set ViewListener for custom view
                    customCarouselView.setViewListener(viewListener);
                    NUMBER_OF_PAGES = productModels.getMedia().size();
                    customCarouselView.setPageCount(NUMBER_OF_PAGES);

                    if (NUMBER_OF_PAGES == 0) {
                        customCarouselView.setVisibility(View.GONE);
                        product_image01.setVisibility(View.VISIBLE);
                        Picasso.get().load(productModels.getImage()).into(product_image01, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }


                    //item recycler view
                    List<ResponseProductTypeModel> responseProductTypeModelList = productModels.getResponseProductTypeModels();

                    ItemAdapter itemAdapter = new ItemAdapter(responseProductTypeModelList, ProductActivity.this);
                    item_recyclerView.setHasFixedSize(true);
                    item_recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
                    item_recyclerView.setAdapter(itemAdapter);
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Toast.makeText(ProductActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ViewListener viewListener = position -> {
        View customView = getLayoutInflater().inflate(R.layout.item_media_image, null);
        //set view attributes here
        ImageView imageView = customView.findViewById(R.id.media_img);
        VideoView simpleVideoView = customView.findViewById(R.id.media_video);
        ProgressBar progress_circular = customView.findViewById(R.id.progress_circular);

        imageView.setVisibility(View.VISIBLE);
        simpleVideoView.setVisibility(View.GONE);

        if (media.get(position).getMediaType().equalsIgnoreCase("I")) {
            Picasso.get().load(media.get(position).getMediaUrl()).into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    progress_circular.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    progress_circular.setVisibility(View.GONE);
                }
            });
        } else {
            imageView.setVisibility(View.GONE);
            simpleVideoView.setVisibility(View.VISIBLE);

            //Creating MediaController
            // MediaController mediaController = new MediaController(this);
            // mediaController.setAnchorView(simpleVideoView);

            //Setting MediaController and URI, then starting the videoView
            //  simpleVideoView.setMediaController(mediaController);
            simpleVideoView.setVideoPath(media.get(position).getMediaUrl());
            simpleVideoView.requestFocus();
            simpleVideoView.start();


            simpleVideoView.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.start();
                mediaPlayer.setOnVideoSizeChangedListener((mediaPlayer1, i, i1) -> {
                    progress_circular.setVisibility(View.GONE);
                    mediaPlayer1.start();
                });
            });
        }


        return customView;
    };

    private void AddToCart(List<CartItem> list) {

        String token = CommonUtils.getToken();
        ListCart listCart = new ListCart();
        listCart.setCartItems(list);

        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<ListCart> modelCall = uploadData.AddToCart(token, listCart);

        modelCall.enqueue(new Callback<ListCart>() {
            @Override
            public void onResponse(Call<ListCart> call, Response<ListCart> response) {
                if (response.isSuccessful()) {
                    listCart.setCartItems(null);
                    Toast.makeText(ProductActivity.this, "Product added successfully...", Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 401) {
                    startActivity(new Intent(ProductActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ListCart> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAddClick(CartItem cartItem, int itemId) {
        if (cartItem != null) {
            cartItem.setProduct("" + productID);
            itemList.put(itemId, cartItem);
            Log.d(TAG, "onBindViewHolder: " + itemList.size());
        }
    }

    @Override
    public void onPlusClick(CartItem cartItem, int itemId, int count) {
        cartItem.setProduct("" + productID);
        itemList.put(itemId, cartItem);
        Log.d(TAG, "onBindViewHolder: " + itemList.size());
    }

    @Override
    public void onMinusClick(CartItem cartItem, int itemId, int count) {
        cartItem.setProduct("" + productID);
        if (count > 1) {
            itemList.put(itemId, cartItem);
        } else {
            itemList.remove(cartItem);
        }
        Log.d(TAG, "onBindViewHolder: " + itemList.size());

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("home", true);
        startActivity(intent);
        ProductActivity.this.finish(); // if the activity running has it's own context

    }
}