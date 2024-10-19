package com.example.fellphonef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends ArrayAdapter<CartItem> {
    private List<CartItem> cartItems;

    public CartAdapter(Context context, List<CartItem> items) {
        super(context, 0, items);
        this.cartItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CartItem cartItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item, parent, false);
        }

        // Lookup view for data population
        ImageView productImage = convertView.findViewById(R.id.product_image);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productPrice = convertView.findViewById(R.id.product_price);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);

        // Populate the data into the template view using the data object
        productImage.setImageResource(cartItem.getProduct().getImageResId());
        productName.setText(cartItem.getProduct().getName());
        productPrice.setText(String.format("%.2f vnd", cartItem.getProduct().getPrice()));
        checkBox.setChecked(cartItem.getProduct().isSelected());

        // Checkbox listener to update selection status
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartItem.getProduct().setSelected(isChecked);
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
