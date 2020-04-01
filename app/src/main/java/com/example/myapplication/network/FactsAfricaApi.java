package com.example.myapplication.network;

import com.example.myapplication.models.Invoice;
import com.example.myapplication.models.Login;
import com.example.myapplication.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FactsAfricaApi {
    @POST("login")
    Call<User> login(@Body Login login);

    @GET("user")
    Call<User> getLoggedUser();

    @GET("supplier/invoices")
    Call<List<Invoice>> getAllInvoices();

    @GET("user/{id}")
    Call<User> getUserById(@Path("id") int id);

    @GET("supplier/invoices/saved")
    Call<List<Invoice>> getSavedInvoices();

    @GET("supplier/buyers")
    Call<User> getAllBuyers();
}
