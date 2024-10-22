package com.example.fellphonef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Database and table names
    public static final String DATABASE_NAME = "UserProductDatabase.db";

    // User table
    public static final String USER_TABLE_NAME = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Product table
    public static final String PRODUCT_TABLE_NAME = "products";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_DETAILS = "details";
    public static final String COLUMN_PRODUCT_IMAGE = "image";

    // Cart table
    public static final String CART_TABLE_NAME = "carts";
    public static final String COLUMN_CART_ID = "id";
    public static final String COLUMN_CART_USER_FKID = "userId";
    public static final String COLUMN_CART_PRODUCT_FKID = "productId";
    public static final String COLUMN_CART_QUANTITY = "quantity";

    // Order table
    public static final String ORDER_TABLE_NAME = "orders";
    public static final String ID = "id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User table
        String createUserTable = "CREATE TABLE " + USER_TABLE_NAME + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";

        // Create Product table
        String createProductTable = "CREATE TABLE " + PRODUCT_TABLE_NAME + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT ," +
                COLUMN_PRODUCT_PRICE + " REAL, " +
                COLUMN_PRODUCT_DETAILS + " TEXT, " +
                COLUMN_PRODUCT_IMAGE + " INTEGER)";

        // Create Cart table
        String createCartTable = "CREATE TABLE " + CART_TABLE_NAME + " (" +
                COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CART_PRODUCT_FKID + " INTEGER, " +
                COLUMN_CART_QUANTITY + " INTEGER)";

        db.execSQL(createUserTable);
        db.execSQL(createProductTable);
        db.execSQL(createCartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE_NAME);
        onCreate(db);
    }

    // ********** USER METHODS ********** //

    // Insert user into database
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);

        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1; // Return true if insertion is successful
    }

    // Check if username exists
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE username=?", new String[]{username});
        return cursor.getCount() > 0;
    }

    // Check user credentials for login
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE username=? AND password=?", new String[]{username, password});
        return cursor.getCount() > 0;
    }
    // ********** CART METHODS ********** //

    // Get all products in cart
    public List<CartItem> getAllProductInCart() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CART_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_PRODUCT_FKID)));
                int quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY)));
                Product product = getProductById(productId);
                if(product != null) {
                    cartItems.add(new CartItem(product, quantity));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    // Add product to cart
    public boolean addToCart(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            // Check if product already exists in the cart
            String query = "SELECT * FROM " + CART_TABLE_NAME + " WHERE productId = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

            if (cursor != null && cursor.moveToFirst()) {
                // Product exists, update the quantity
                int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                int newQuantity = currentQuantity + 1;

                contentValues.put("quantity", newQuantity);
                int rowsAffected = db.update(CART_TABLE_NAME, contentValues, "productId = ?", new String[]{String.valueOf(productId)});

                cursor.close();
                return rowsAffected > 0;
            } else {
                contentValues.put("productId", productId);
                contentValues.put("quantity", 1);

                long result = db.insert(CART_TABLE_NAME, null, contentValues);
                if (cursor != null) {
                    cursor.close();
                }
                return result != -1;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }
        return false;
    }



    // ********** PRODUCT METHODS ********** //

    // Insert product into database
    public boolean insertProduct(String name, double price, String details, int imageResId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_NAME, name);
        contentValues.put(COLUMN_PRODUCT_PRICE, price);
        contentValues.put(COLUMN_PRODUCT_DETAILS, details);
        contentValues.put(COLUMN_PRODUCT_IMAGE, imageResId);

        long result = db.insert(PRODUCT_TABLE_NAME, null, contentValues);
        return result != -1; // Return true if insertion is successful
    }

    // Update product
    public boolean updateProduct(String name, double price, String details, int imageResId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_PRICE, price);
        contentValues.put(COLUMN_PRODUCT_DETAILS, details);
        contentValues.put(COLUMN_PRODUCT_IMAGE, imageResId);

        int result = db.update(PRODUCT_TABLE_NAME, contentValues, "name = ?", new String[]{name});
        return result > 0;
    }

    // Delete product
    public boolean deleteProduct(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(PRODUCT_TABLE_NAME, "name = ?", new String[]{name});
        return result > 0;
    }

    //Get all products by id
    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PRODUCT_TABLE_NAME + " WHERE id = ?";
        Product product = null;

        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                String details = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DETAILS));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE));
                product = new Product(name, price, details, imageResId);
            }
        } catch (Exception e) {
            // Handle potential exceptions (e.g., log the error)
            Log.e("DatabaseError", "Error fetching product by ID: " + id, e);
        }

        return product;
    }


    // Get all products by ids from database
    public List<Product> getProductsByIds(ArrayList<Integer> ids) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Check if ids list is empty
        if (ids.isEmpty()) {
            return products; // Return empty list if no ids provided
        }

        // Prepare the SQL query using a StringBuilder to create the comma-separated list of ids
        StringBuilder idList = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            idList.append(ids.get(i));
            if (i < ids.size() - 1) {
                idList.append(", "); // Add a comma for separation
            }
        }

        // Construct the SQL query
        String query = "SELECT * FROM " + PRODUCT_TABLE_NAME + " WHERE id IN (" + idList.toString() + ")";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                String details = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DETAILS));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE));
                products.add(new Product(name, price, details, imageResId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }


    // Get all products from database
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PRODUCT_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                String details = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DETAILS));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE));
                products.add(new Product(id, name, price, details, imageResId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    //Clear products
    public void clearProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + PRODUCT_TABLE_NAME);
    }

    //Seed products
    public void seedProducts() {
        List<Product> initProducts = Arrays.asList(
                new Product(1, "iPhone 13 Pro Max",
                        2000.0,
                        "The iPhone 13 Pro Max features a 6.7-inch Super Retina XDR display, A15 Bionic chip, and a powerful triple-camera system with LiDAR.\n\n" +
                                "It also offers 5G connectivity and all-day battery life, making it one of the most advanced smartphones on the market.\n\n" +
                                "With its sleek design and premium build quality, the iPhone 13 Pro Max is a top choice for anyone seeking the best in smartphone technology.",
                        R.drawable.product_image1),

                new Product(2, "Samsung Galaxy S21 Ultra",
                        1800.0,
                        "With a 6.8-inch Dynamic AMOLED display, the Galaxy S21 Ultra offers stunning visuals. Powered by the Exynos 2100/Snapdragon 888,\n\n" +
                                "it boasts a quad-camera system with 100x Space Zoom and 5G support.\n\n" +
                                "Its robust battery life and premium features make it ideal for photography enthusiasts and heavy users alike.",
                        R.drawable.product_image1),

                new Product(3, "Google Pixel 6 Pro",
                        1200.0,
                        "The Google Pixel 6 Pro features a 6.71-inch LTPO OLED display and is powered by Google’s custom Tensor chip.\n\n" +
                                "It offers an enhanced camera system with computational photography, excellent software integration, and long-lasting battery life.\n\n" +
                                "The device excels in delivering an intuitive user experience with its clean Android interface and timely updates.",
                        R.drawable.product_image1),

                new Product(4, "OnePlus 9 Pro",
                        1050.0,
                        "The OnePlus 9 Pro boasts a 6.7-inch Fluid AMOLED display and is powered by the Snapdragon 888 processor.\n\n" +
                                "With a Hasselblad-powered quad-camera system, 5G connectivity, and Warp Charge 65T, it's built for speed and performance.\n\n" +
                                "This device combines premium hardware with an attractive price point, making it a strong contender in the flagship market.",
                        R.drawable.product_image1),

                new Product(5, "Xiaomi Mi 11 Ultra",
                        1400.0,
                        "The Xiaomi Mi 11 Ultra features a 6.81-inch AMOLED display and Snapdragon 888 chipset.\n\n" +
                                "Its standout feature is a 50 MP triple-camera system with a unique rear display for enhanced selfies and notifications.\n\n" +
                                "This phone also offers impressive battery life, ensuring you can enjoy all its features throughout the day.",
                        R.drawable.product_image1),

                new Product(6, "Vivo X70 Pro+",
                        1350.0,
                        "The Vivo X70 Pro+ features a 6.78-inch AMOLED display and is powered by the Snapdragon 888+.\n\n" +
                                "Its camera system is co-engineered with Zeiss optics, offering professional-level photography and videography features.\n\n" +
                                "With fast charging capabilities and a sleek design, it's perfect for those who demand both style and performance.",
                        R.drawable.product_image1),

                new Product(7, "Oppo Find X3 Pro",
                        1100.0,
                        "The Oppo Find X3 Pro offers a 6.7-inch AMOLED display, Snapdragon 888 chipset, and a 50 MP quad-camera system.\n\n" +
                                "Its standout feature is a billion-color display, which makes for an incredible visual experience.\n\n" +
                                "This phone is designed to impress with its innovative features and stunning aesthetics, appealing to users who value both performance and design.",
                        R.drawable.product_image1),

                new Product(8, "Asus ROG Phone 5",
                        1500.0,
                        "Designed for gamers, the Asus ROG Phone 5 boasts a 6.78-inch AMOLED display with a 144Hz refresh rate.\n\n" +
                                "Powered by the Snapdragon 888, it comes with a massive 6000mAh battery and enhanced cooling systems for extended gaming sessions.\n\n" +
                                "With customizable RGB lighting and gaming-specific features, it's a top choice for serious gamers looking for an edge.",
                        R.drawable.product_image1),

                new Product(9, "Huawei P50 Pro",
                        1250.0,
                        "The Huawei P50 Pro offers a 6.6-inch OLED display and a powerful Kirin 9000 chipset.\n\n" +
                                "It shines in photography with its 50 MP quad-camera system, providing amazing clarity and details in low-light conditions.\n\n" +
                                "With its sleek design and premium materials, the P50 Pro is not just a smartphone but a fashion statement.",
                        R.drawable.product_image1),

                new Product(10, "Sony Xperia 1 III",
                        1300.000,
                        "Featuring a 6.5-inch 4K OLED display and a triple-camera setup with Zeiss optics, the Xperia 1 III excels in multimedia.\n\n" +
                                "It offers enhanced gaming, professional-grade photography, and cinema-quality video.\n\n" +
                                "This device is perfect for content creators and entertainment enthusiasts looking for high-quality performance and features.",
                        R.drawable.product_image1)
        );
        for (Product product : initProducts) {
            insertProduct(product.getName(), product.getPrice(), product.getDetails(), product.getImageResId());
        }
    }
}
