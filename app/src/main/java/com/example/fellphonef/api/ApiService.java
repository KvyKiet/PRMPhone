package com.example.fellphonef.api;
import com.example.fellphonef.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/products")
    Call<List<Product>> getAllProducts();
}
