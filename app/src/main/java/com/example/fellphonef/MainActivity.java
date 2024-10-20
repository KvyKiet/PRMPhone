package com.example.fellphonef;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fellphonef.api.ApiService;
import com.example.fellphonef.api.RetrofitInstance;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchProducts();

        // Khởi tạo danh sách sản phẩm
        productList = new ArrayList<>();
        productList.add(new Product("Iphone 13", 19.99, "dep vl", R.drawable.product_image1));
        productList.add(new Product("Iphone 14", 29.99, "qua dinh", R.drawable.product_image2));
        productList.add(new Product("Iphone 15", 39.99, "dinh noc", R.drawable.product_image1));
        productList.add(new Product("Iphone 16", 49.99, "kich tran", R.drawable.product_image2));


        // Khởi tạo RecyclerView
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2)); // Sử dụng GridLayout với 2 cột

        // Khởi tạo Adapter và set cho RecyclerView
        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);

        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Nút chuyển gg map
        ImageButton locationButton = findViewById(R.id.btnlocation);
        locationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FragmentContainerActivity.class);
            startActivity(intent);
        });
    }


    //test api
    private void fetchProducts() {
        ApiService apiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<List<Product>> call = apiService.getAllProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body();
                    productAdapter = new ProductAdapter(productList);
                    recyclerViewProducts.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
}
