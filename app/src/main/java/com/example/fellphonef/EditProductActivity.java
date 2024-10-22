package com.example.fellphonef;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProductActivity extends AppCompatActivity {

    private EditText editTextProductName, editTextProductPrice, editTextProductDetails, editTextProductImage;
    private Button buttonUpdateProduct, buttonDeleteProduct;
    private DBHelper dbHelper;
    private String originalProductName; // Lưu tên sản phẩm ban đầu để tìm và sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextProductDetails = findViewById(R.id.editTextProductDetails);
        editTextProductImage = findViewById(R.id.editTextProductImage);
        buttonUpdateProduct = findViewById(R.id.buttonUpdateProduct);
        buttonDeleteProduct = findViewById(R.id.buttonDeleteProduct);

        dbHelper = new DBHelper(this);

        // Nhận dữ liệu sản phẩm từ Intent
        Intent intent = getIntent();
        originalProductName = intent.getStringExtra("product_name");
        String productPrice = intent.getStringExtra("product_price");
        String productDetails = intent.getStringExtra("product_details");
        String productImage = intent.getStringExtra("product_image");

        // Điền dữ liệu vào các trường
        editTextProductName.setText(originalProductName);
        editTextProductPrice.setText(productPrice);
        editTextProductDetails.setText(productDetails);
        editTextProductImage.setText(productImage);

        // Xử lý sự kiện cập nhật sản phẩm
        buttonUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editTextProductName.getText().toString().trim();
                String priceString = editTextProductPrice.getText().toString().trim();
                String details = editTextProductDetails.getText().toString().trim();
                String imageResIdString = editTextProductImage.getText().toString().trim();

                if (newName.isEmpty() || priceString.isEmpty() || details.isEmpty() || imageResIdString.isEmpty()) {
                    Toast.makeText(EditProductActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(priceString);
                int imageResId = Integer.parseInt(imageResIdString);

                boolean result = dbHelper.updateProduct(originalProductName, price, details, imageResId);

                if (result) {
                    Toast.makeText(EditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditProductActivity.this, AdminActivity.class)); // Quay lại AdminActivity
                    finish(); // Đóng activity hiện tại
                } else {
                    Toast.makeText(EditProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện xóa sản phẩm
        buttonDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = dbHelper.deleteProduct(originalProductName);

                if (result) {
                    Toast.makeText(EditProductActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditProductActivity.this, AdminActivity.class)); // Quay lại AdminActivity
                    finish(); // Đóng activity hiện tại
                } else {
                    Toast.makeText(EditProductActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
