package com.example.fellphonef;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {

    private EditText editTextProductName, editTextProductPrice, editTextProductDetails, editTextProductImage;
    private Button buttonAddProduct;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextProductDetails = findViewById(R.id.editTextProductDetails);
        editTextProductImage = findViewById(R.id.editTextProductImage);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        dbHelper = new DBHelper(this);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextProductName.getText().toString().trim();
                String priceString = editTextProductPrice.getText().toString().trim();
                String details = editTextProductDetails.getText().toString().trim();
                String imageResIdString = editTextProductImage.getText().toString().trim();

                if (name.isEmpty() || priceString.isEmpty() || details.isEmpty() || imageResIdString.isEmpty()) {
                    Toast.makeText(AddProductActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(priceString);
                int imageResId = Integer.parseInt(imageResIdString);

                boolean result = dbHelper.insertProduct(name, price, details, imageResId);

                if (result) {
                    Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddProductActivity.this, AdminActivity.class)); // Quay lại AdminActivity
                    finish(); // Đóng activity hiện tại
                } else {
                    Toast.makeText(AddProductActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
