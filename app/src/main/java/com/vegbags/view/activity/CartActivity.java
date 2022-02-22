package com.vegbags.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.vegbags.Models.CartItem;
import com.vegbags.Models.ListCart;
import com.vegbags.Models.ListOrderCart;
import com.vegbags.R;
import com.vegbags.ResponseModels.AddressModel;
import com.vegbags.ResponseModels.CartResponseModel;
import com.vegbags.ResponseModels.ResponseCartItemModel;
import com.vegbags.adapter.AddressAdapter;
import com.vegbags.adapter.CartAdapter;
import com.vegbags.adapter.TypeRecyclerInterface;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.databinding.ActivityCartBinding;
import com.vegbags.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements TypeRecyclerInterface {

    private static final String TAG = "CartActivity";
    private ActivityCartBinding mBinding;
    List<CartItem> items = new ArrayList<>();
    ListCart listCart = new ListCart();
    List<ResponseCartItemModel> cartModelList = new ArrayList<>();
    CartAdapter cartAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    AddressAdapter addressAdapter;
//    public static int billingAddress = CommonUtils.getBillingAddress();
//    public static int deliveryAddress = CommonUtils.getBillingAddress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        mBinding.shimmerFrameLayout.startShimmerAnimation();

        mBinding.shimmerFrameLayout.startShimmerAnimation();
        mBinding.imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CartActivity.this.finish(); // if the activity running has it's own context


// view.getContext().finish() for fragments etc.
        });

        mBinding.btnCheckOut.setOnClickListener(view -> {

            if (items.size() > 0)
                showDialog();
            else
                Toast.makeText(this, "please add any item to cart", Toast.LENGTH_SHORT).show();
            //CreateOrder();
        });

        GetCartItems();

        mBinding.btnEditItem.setOnClickListener(view -> {

            if (items.size() > 0) {
                if (mBinding.btnEditItem.getText().toString().equalsIgnoreCase("Update Items")) {
                    mBinding.btnEditItem.setText("Edit Items");
                    mBinding.btnCheckOut.setClickable(true);
                    List<CartItem> list = new ArrayList<CartItem>(itemList.values());

                    if (list.size() > 0) {
                        AddToCart(list);
                    } else {
                        Toast.makeText(this, "Please add any item", Toast.LENGTH_SHORT).show();
                    }
                    setAdapter(false);
                } else {
                    setAdapter(true);
                    mBinding.btnEditItem.setText("Update Items");
                    mBinding.btnCheckOut.setClickable(false);
                }
            } else
                Toast.makeText(this, "please add any item to cart", Toast.LENGTH_SHORT).show();


        });

    }

    TextView no_address;

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.addresses_layout);

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        Button manageAddress = dialog.findViewById(R.id.manage_address);
        Button PlaceOrder = dialog.findViewById(R.id.PlaceOrder);
        ImageView ic_close = dialog.findViewById(R.id.ic_close);
        no_address = dialog.findViewById(R.id.no_address);

        getAddresses(recyclerView, true);

        PlaceOrder.setOnClickListener(view -> {
            //deliveryAddress = billingAddress;
            if (CommonUtils.getBillingAddress() == 0)
                Toast.makeText(this, "Please select address", Toast.LENGTH_SHORT).show();
            else {
                CreateOrder(CommonUtils.getBillingAddress(), CommonUtils.getBillingAddress());
            }
        });
        manageAddress.setOnClickListener(view -> {
            dialog.dismiss();
            startActivity(new Intent(this, ManageAddressActivity.class));
        });

        ic_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    private void getAddresses(RecyclerView recyclerView, boolean isBilling) {
        mBinding.progressCircular.setVisibility(View.VISIBLE);
        String token = CommonUtils.getToken();
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<List<AddressModel>> modelCall = uploadData.getAddresses(token);

        modelCall.enqueue(new Callback<List<AddressModel>>() {
            @Override
            public void onResponse(Call<List<AddressModel>> call, Response<List<AddressModel>> response) {
                mBinding.progressCircular.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().size() > 0) {
                    List<AddressModel> addressModelList = response.body();

                    RecyclerView.LayoutManager manager = new LinearLayoutManager(CartActivity.this);
                    recyclerView.setLayoutManager(manager);
                    if (isBilling)
                        addressAdapter = new AddressAdapter(addressModelList, true, true);
                    else
                        addressAdapter = new AddressAdapter(addressModelList, true, false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    recyclerView.setAdapter(addressAdapter);
                    no_address.setVisibility(View.GONE);

                } else
                    no_address.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<AddressModel>> call, Throwable t) {
                mBinding.progressCircular.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CreateOrder(int billingAddress, int deliveryAddress) {
        String token = CommonUtils.getToken();
        ListOrderCart listCart = new ListOrderCart();
        listCart.setItems(items);
        listCart.setBillingAddress(String.valueOf(billingAddress));
        listCart.setShippingAddress(String.valueOf(deliveryAddress));
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<ListOrderCart> modelCall = uploadData.CreateOrder(token, listCart);

        modelCall.enqueue(new Callback<ListOrderCart>() {
            @Override
            public void onResponse(Call<ListOrderCart> call, Response<ListOrderCart> response) {
                mBinding.shimmerFrameLayout.stopShimmerAnimation();
                mBinding.shimmerFrameLayout.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    startActivity(new Intent(CartActivity.this, OrderConfirmActivity.class));
                    finish();
                }
                if (response.code() == 401) {
                    startActivity(new Intent(CartActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ListOrderCart> call, Throwable t) {
                mBinding.shimmerFrameLayout.stopShimmerAnimation();
                mBinding.shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void GetCartItems() {

        String token = CommonUtils.getToken();

        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<List<CartResponseModel>> modelCall = uploadData.getCartItems(token);
        modelCall.enqueue(new Callback<List<CartResponseModel>>() {
            @Override
            public void onResponse(Call<List<CartResponseModel>> call, Response<List<CartResponseModel>> response) {
                mBinding.shimmerFrameLayout.stopShimmerAnimation();
                mBinding.shimmerFrameLayout.setVisibility(View.GONE);
                assert response.body() != null;
                if (response.body().size() > 0 && response.isSuccessful()) {
                    try {
                        if (response.code() == 200 && response.body().get(0).getCartItems().size() > 0) {
                            mBinding.noDara.setVisibility(View.GONE);
                            cartModelList = response.body().get(0).getCartItems();
                            setAdapter(false);
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                            itemTouchHelper.attachToRecyclerView(mBinding.cartRecyclerView);

                            for (ResponseCartItemModel itemModel : cartModelList) {
                                CartItem cartItem = new CartItem();
                                cartItem.setProduct(itemModel.getProduct().getId().toString());
                                cartItem.setProductType(itemModel.getProductType().getId().toString());
                                cartItem.setQty(itemModel.getQty());
                                cartItem.setUnit(itemModel.getUnit());
                                items.add(cartItem);
                            }
                            listCart.setCartItems(items);
                            Log.d("TAG", "onResponse: " + items);
                        } else {
                            mBinding.noDara.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        Toast.makeText(CartActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                if (response.code() == 401) {
                    startActivity(new Intent(CartActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<CartResponseModel>> call, Throwable t) {
                mBinding.shimmerFrameLayout.stopShimmerAnimation();
                mBinding.shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AddToCart(List<CartItem> list) {
        mBinding.progressCircular.setVisibility(View.VISIBLE);
        String token = CommonUtils.getToken();
        ListCart listCart = new ListCart();
        listCart.setCartItems(list);
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<ListCart> modelCall = uploadData.AddToCart(token, listCart);

        modelCall.enqueue(new Callback<ListCart>() {
            @Override
            public void onResponse(Call<ListCart> call, Response<ListCart> response) {
                mBinding.progressCircular.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    listCart.setCartItems(null);
                    finish();
                    startActivity(getIntent());
                }
                if (response.code() == 401) {
                    startActivity(new Intent(CartActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ListCart> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                mBinding.progressCircular.setVisibility(View.GONE);
            }
        });
    }

    private void setAdapter(boolean isEditable) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(CartActivity.this);
        mBinding.cartRecyclerView.setLayoutManager(manager);
        cartAdapter = new CartAdapter(this, cartModelList, isEditable);
        mBinding.cartRecyclerView.setHasFixedSize(true);
        mBinding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        mBinding.cartRecyclerView.setAdapter(cartAdapter);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            ResponseCartItemModel deletedCart = cartModelList.get(position);
            cartModelList.remove(position);
            DeletedCart(deletedCart.getId());
            cartAdapter.notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.color_red))
                    .addActionIcon(R.drawable.ic_delete_white)
                    .create()
                    .decorate();


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };

    private void DeletedCart(Integer id) {
        String token = CommonUtils.getToken();
        mBinding.progressCircular.setVisibility(View.VISIBLE);
        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<Void> modelCall = uploadData.DeleteCart(token, String.valueOf(id));
        modelCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mBinding.progressCircular.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    cartAdapter.notifyDataSetChanged();
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mBinding.progressCircular.setVisibility(View.GONE);
                Toast.makeText(CartActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAddClick(CartItem cartItem, int ItemId) {

    }

    Map<Integer, CartItem> itemList = new HashMap<>();

    @Override
    public void onPlusClick(CartItem cartItem, int ItemId, int count) {
        //cartItem.setProduct("" + productID);
        itemList.put(ItemId, cartItem);
        Log.d(TAG, "onBindViewHolder: " + itemList.size());
    }

    @Override
    public void onMinusClick(CartItem cartItem, int ItemId, int count) {
        // cartItem.setProduct("" + productID);
        if (count > 0) {
            itemList.put(ItemId, cartItem);
        } else {
            itemList.remove(cartItem);
        }
        Log.d(TAG, "onBindViewHolder: " + itemList.size());

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        CartActivity.this.finish(); // if the activity running has it's own context

    }
}