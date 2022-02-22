package com.vegbags.database;


import com.vegbags.Models.ListCart;
import com.vegbags.Models.ListOrderCart;
import com.vegbags.ResponseModels.AddressModel;
import com.vegbags.ResponseModels.CartResponseModel;
import com.vegbags.ResponseModels.InvoiceModel;
import com.vegbags.ResponseModels.LoginModel;
import com.vegbags.ResponseModels.MessageModel;
import com.vegbags.ResponseModels.OrderResponseModel;
import com.vegbags.ResponseModels.PaymentResp;
import com.vegbags.ResponseModels.ProductModel;
import com.vegbags.ResponseModels.TransactionModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("otp-login")
    Call<LoginModel> getLogin(
            @Field("phone") String phone,
            @Field("otp") String otp
    );

    @GET("products/")
    Call<List<ProductModel>> getProducts();

    @GET("products/{id}")
    Call<ProductModel> getProduct(
            @Path("id") String id);

    @POST("cart/")
    Call<ListCart> AddToCart(
            @Header("Authorization") String token,
            @Body ListCart listCart
    );

    @GET("cart/")
    Call<List<CartResponseModel>> getCartItems(@Header("Authorization") String token);

    @DELETE("DeleteCartItem/{id}/")
    Call<Void> DeleteCart(@Header("Authorization") String token,
                          @Path("id") String id);


    @GET("order/")
    Call<List<OrderResponseModel>> getOrders(@Header("Authorization") String token);

    @POST("order/")
    Call<ListOrderCart> CreateOrder(
            @Header("Authorization") String token,
            @Body ListOrderCart listCart
    );


    @FormUrlEncoded
    @POST("adresses/")
    Call<AddressModel> CreateAddress(
            @Header("Authorization") String token,
            @Field("contactName") String contactName,
            @Field("phoneNo") String phoneNo,
            @Field("altPhoneNo") String altPhoneNo,
            @Field("address") String address,
            @Field("pincode") String pincode
    );

    @FormUrlEncoded
    @POST("changeDeleiveryType")
    Call<MessageModel> DeliveryType(
            @Header("Authorization") String token,
            @Field("type") String type,
            @Field("name") String name);

    @FormUrlEncoded
    @POST("newPayment/")
    Call<PaymentResp> NewPayment(@Header("Authorization") String token,
                                 @Field("description") String description,
                                 @Field("refId") String refId,
                                 @Field("transactionType") String transactionType,
                                 @Field("amount") String amount);

    @GET("adresses/")
    Call<List<AddressModel>> getAddresses(@Header("Authorization") String token);

    @PUT("adresses/{id}/")
    Call<AddressModel> UpdateAddresses(@Header("Authorization") String token,
                                       @Path("id") String id,
                                       @Body AddressModel addressModel);

    @DELETE("adresses/{id}/")
    Call<Void> DeleteAddresses(@Header("Authorization") String token,
                               @Path("id") String id);

    @DELETE("EmptyCart")
    Call<Void> DeleteCart(@Header("Authorization") String token);

    @DELETE("order/{id}/")
    Call<Void> DeleteOrder(@Header("Authorization") String token, @Path("id") String id);


    @GET("invoices/")
    Call<List<InvoiceModel>> getInvoices(@Header("Authorization") String token);

    @GET("CreditLedger")
    Call<TransactionModel> getTransaction(@Header("Authorization") String token);

    @GET("GetUser")
    Call<TransactionModel> getGetUser(@Header("Authorization") String token);

    @GET("GetServiceAvailablity")
    Call<Void> getServiceAvailable(@Query("code") String code);
}
