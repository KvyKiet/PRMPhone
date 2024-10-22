package com.example.fellphonef;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private Button btnAddProduct, btnEditProduct, btnDeleteProduct, btnBackToHome;
    private DBHelper dbHelper;
    private Product selectedProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page);

        // Initialize database helper
        dbHelper = new DBHelper(this);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView_products);
        btnAddProduct = findViewById(R.id.btn_add_product);
        btnEditProduct = findViewById(R.id.btn_edit_product);
        btnDeleteProduct = findViewById(R.id.btn_delete_product);
        btnBackToHome = findViewById(R.id.btn_back_to_home);

        // Set up RecyclerView with LayoutManager and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = dbHelper.getAllProducts(); // Fetch all products from SQLite
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);

        // Set the product selection listener
        productAdapter.setOnItemClickListener(product -> {
            selectedProduct = product;
            Toast.makeText(AdminActivity.this, "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
        });

        // Handle Add Product button click
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        // Handle Edit Product button click
        btnEditProduct.setOnClickListener(v -> {
            if (selectedProduct != null) {
                Intent intent = new Intent(AdminActivity.this, EditProductActivity.class);
                intent.putExtra("product_name", selectedProduct.getName());
                intent.putExtra("product_price", selectedProduct.getPrice());
                intent.putExtra("product_detail", selectedProduct.getDetails()); // Pass any necessary product info
                startActivity(intent);
            } else {
                Toast.makeText(AdminActivity.this, "Please select a product to edit", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Delete Product button click
        btnDeleteProduct.setOnClickListener(v -> {
            if (selectedProduct != null) {
                dbHelper.deleteProduct(selectedProduct.getName()); // Delete product by name in SQLite
                productList.remove(selectedProduct); // Remove the product from the list
                productAdapter.notifyDataSetChanged(); // Notify adapter about the change
                Toast.makeText(AdminActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                selectedProduct = null; // Clear the selected product
            } else {
                Toast.makeText(AdminActivity.this, "Please select a product to delete", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Back to Homepage button click
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Optionally, call finish() to close the AdminActivity
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the product list when returning to the activity (e.g., after adding or editing)
        productList.clear();
        productList.addAll(dbHelper.getAllProducts());
        productAdapter.notifyDataSetChanged();
    }
}
