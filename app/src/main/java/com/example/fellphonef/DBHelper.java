package com.example.fellphonef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
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
}
