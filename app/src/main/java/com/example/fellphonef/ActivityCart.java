package com.example.fellphonef;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCart extends AppCompatActivity {

    private ListView listView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private TextView totalTextView;
    private CheckBox selectAllCheckbox;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DBHelper(this);

        listView = findViewById(R.id.cart_list_view);
        totalTextView = findViewById(R.id.total_text_view);
        selectAllCheckbox = findViewById(R.id.select_all_checkbox);
        Button checkoutButton = findViewById(R.id.checkout_button);

        // Initialize cart items (You might get this data from a previous activity)
        cartItems = dbHelper.getAllProductInCart();
//        cartItems.add(new CartItem(new Product("Product 1", 100, "Details", R.drawable.product_image1), 1));
//        cartItems.add(new CartItem(new Product("Product 2", 200, "Details", R.drawable.default_product_image), 2));

        cartAdapter = new CartAdapter(this, cartItems);
        listView.setAdapter(cartAdapter);

        updateTotal();

        // Setup other interactions like select all checkbox, checkout, etc.
    }

    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        totalTextView.setText("Total: " + total + " VND");
    }
}
