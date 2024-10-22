package com.example.fellphonef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Retrieve products from SQLite
        dbHelper.clearProducts();
        dbHelper.seedProducts();
        productList = dbHelper.getAllProducts();

        // Initialize RecyclerView
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2)); // Use GridLayout with 2 columns

        // Initialize Adapter and set it to the RecyclerView
        productAdapter = new ProductAdapter(productList, this);
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("name", product.getName());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("details", product.getDetails());
                intent.putExtra("imageResId", product.getImageResId());
                startActivity(intent);
            }
        });
        recyclerViewProducts.setAdapter(productAdapter);

        // Login button functionality
        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Cart button functionality
        Button cartButton = findViewById(R.id.button_cart);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ActivityCart.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the product list when returning to the activity
//        dbHelper.clearProducts();
//        dbHelper.seedProducts();
        productList.clear();
        productList.addAll(dbHelper.getAllProducts());
        productAdapter.notifyDataSetChanged();
    }
}
