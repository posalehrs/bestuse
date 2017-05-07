package com.example.pc.bestuse.rest;

import com.example.pc.bestuse.model.Product;
import com.example.pc.bestuse.model.Request;
import com.example.pc.bestuse.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface InterfaceUser {

    @POST("login")
    Call<User> login(@Body User user);

    @POST("user-register")
    Call<User> registerUser(@Body User user);

    @POST("get-user")
    Call<User> getUser(@Body Request _id,@Header("token") String token);

//    @POST("update-product")
//    Call<Product> updateProduct(@Body Product p,@Header("token") String token);

    @POST("user-update")
    Call<User> updateUser(@Body User user,@Header("token") String token);

    @POST("add-favorite")
    Call<User> addFavorite(@Body Product product, @Header("token") String token);


}
