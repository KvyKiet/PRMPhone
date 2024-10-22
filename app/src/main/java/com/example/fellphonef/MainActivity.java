package com.example.fellphonef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Button cartButton = findViewById(R.id.button_cart);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ActivityCart.class);
            startActivity(intent);
        });
    }

}
