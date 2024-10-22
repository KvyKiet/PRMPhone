package com.example.fellphonef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dbHelper = new DBHelper(this);

        // Get the passed product data from the intent
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 1);
        String productName = intent.getStringExtra("name");
        double productPrice = intent.getDoubleExtra("price", 0.0);
        String productDetails = intent.getStringExtra("details");
        int productImageResId = intent.getIntExtra("imageResId", R.drawable.default_product_image);

        // Reference the views in the layout
        TextView nameTextView = findViewById(R.id.detail_product_name);
        TextView priceTextView = findViewById(R.id.detail_product_price);
        TextView detailsTextView = findViewById(R.id.detail_product_details);
        ImageView productImageView = findViewById(R.id.detail_product_image);

        // Set the data to the views
        nameTextView.setText(productName);
        priceTextView.setText(String.format("$%.2f", productPrice));
        detailsTextView.setText(productDetails);
        productImageView.setImageResource(productImageResId);

        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(view -> {
            dbHelper.addToCart(id);
            Toast.makeText(ProductDetailActivity.this, "Add to cart successfully", Toast.LENGTH_LONG).show();
        });

        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(v -> {
            Intent intentLogin = new Intent(ProductDetailActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        });

        // Cart button functionality
        Button cartButton = findViewById(R.id.button_cart);
        cartButton.setOnClickListener(v -> {
            Intent intentCart = new Intent(ProductDetailActivity.this, ActivityCart.class);
            startActivity(intentCart);
        });
    }
}
