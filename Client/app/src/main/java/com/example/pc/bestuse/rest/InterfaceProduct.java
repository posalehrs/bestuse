package com.example.pc.bestuse.rest;

import com.example.pc.bestuse.model.Product;
import com.example.pc.bestuse.model.Request;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface InterfaceProduct {

    //@Headers("token:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1ODRhYmRmOTQ2NzFhZTBiNjgzZjEyNTEiLCJpbWFnZSI6InVybCIsIm51bWJlcl9waG9uZSI6IjA5ODciLCJhZGRyZXNzIjoicTEiLCJlbWFpbCI6ImVtYWlsIiwicGFzc3dvcmQiOiJwYXMiLCJ1c2VyX25hbWUiOiJjaGluaCIsIl9fdiI6MH0.lA3GJF-vLm_urT3I49JRQv23vqlMrrmtHQHCaO8GNog")
    @POST("product-create")
    Call<Product> productCreate(@Body Product product,@Header("token") String token);

    @POST("product-list")
    Call<List<Product>> getListProduct(@Body Request req,@Header("token") String token);

    @POST("product-detail")
    Call<Product> getProductDetail(@Body Request req);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


}
