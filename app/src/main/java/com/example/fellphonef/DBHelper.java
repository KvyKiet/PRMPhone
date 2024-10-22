package com.example.fellphonef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_DETAILS = "details";
    public static final String COLUMN_PRODUCT_IMAGE = "image";

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
                COLUMN_PRODUCT_NAME + " TEXT PRIMARY KEY, " +
                COLUMN_PRODUCT_PRICE + " REAL, " +
                COLUMN_PRODUCT_DETAILS + " TEXT, " +
                COLUMN_PRODUCT_IMAGE + " INTEGER)";

        db.execSQL(createUserTable);
        db.execSQL(createProductTable);
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

    // Get all products from database
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PRODUCT_TABLE_NAME, null);
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

    //Seed products
    public void seedProducts() {
        List<Product> initProducts = Arrays.asList(
                new Product("iPhone 13 Pro Max", 2000.00, "The iPhone 13 Pro Max features a 6.7-inch Super Retina XDR display, A15 Bionic chip, and a powerful triple-camera system with LiDAR. It also offers 5G connectivity and all-day battery life, making it one of the most advanced smartphones on the market.", R.drawable.product_image1),
                new Product("Samsung Galaxy S21 Ultra", 1800.00, "With a 6.8-inch Dynamic AMOLED display, the Galaxy S21 Ultra offers stunning visuals. Powered by the Exynos 2100/Snapdragon 888, it boasts a quad-camera system with 100x Space Zoom and 5G support.", R.drawable.product_image1),
                new Product("Google Pixel 6 Pro", 1200.00, "The Google Pixel 6 Pro features a 6.71-inch LTPO OLED display and is powered by Google’s custom Tensor chip. It offers an enhanced camera system with computational photography, excellent software integration, and long-lasting battery life.", R.drawable.product_image1),
                new Product("OnePlus 9 Pro", 1050.00, "The OnePlus 9 Pro boasts a 6.7-inch Fluid AMOLED display and is powered by the Snapdragon 888 processor. With a Hasselblad-powered quad-camera system, 5G connectivity, and Warp Charge 65T, it's built for speed and performance.", R.drawable.product_image1),
                new Product("Xiaomi Mi 11 Ultra", 1400.00, "The Xiaomi Mi 11 Ultra features a 6.81-inch AMOLED display and Snapdragon 888 chipset. Its standout feature is a 50 MP triple-camera system with a unique rear display for enhanced selfies and notifications.", R.drawable.product_image1),
                new Product("Vivo X70 Pro+", 1350.00, "The Vivo X70 Pro+ features a 6.78-inch AMOLED display and is powered by the Snapdragon 888+. Its camera system is co-engineered with Zeiss optics, offering professional-level photography and videography features.", R.drawable.product_image1),
                new Product("Oppo Find X3 Pro", 1100.00, "The Oppo Find X3 Pro offers a 6.7-inch AMOLED display, Snapdragon 888 chipset, and a 50 MP quad-camera system. Its standout feature is a billion-color display, which makes for an incredible visual experience.", R.drawable.product_image1),
                new Product("Asus ROG Phone 5", 1500.00, "Designed for gamers, the Asus ROG Phone 5 boasts a 6.78-inch AMOLED display with a 144Hz refresh rate. Powered by the Snapdragon 888, it comes with a massive 6000mAh battery and enhanced cooling systems for extended gaming sessions.", R.drawable.product_image1),
                new Product("Huawei P50 Pro", 1250.00, "The Huawei P50 Pro offers a 6.6-inch OLED display and a powerful Kirin 9000 chipset. It shines in photography with its 50 MP quad-camera system, providing amazing clarity and details in low-light conditions.", R.drawable.product_image1),
                new Product("Sony Xperia 1 III", 1300.00, "Featuring a 6.5-inch 4K OLED display and a triple-camera setup with Zeiss optics, the Xperia 1 III excels in multimedia, offering enhanced gaming, professional-grade photography, and cinema-quality video.", R.drawable.product_image1)
        );
        for (Product product : initProducts) {
            insertProduct(product.getName(), product.getPrice(), product.getDetails(), product.getImageResId());
        }
    }
}
