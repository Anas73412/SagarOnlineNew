package com.ecom.sagaronline.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseCartHandler extends SQLiteOpenHelper {

    private static String DB_NAME = "database13";
    private static int DB_VERSION = 3;
    private SQLiteDatabase db;

    public static final String CART_TABLE = "cart";
    public static final String SINGLE_CART = "singlecart";
    public static final String COLUMN_ID = "product_id";
    public static final String COLUMN_CID = "cart_id";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_IMAGE = "product_image";
    public static final String COLUMN_CAT_ID = "cat_id";
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_MRP = "mrp";
    public static final String COLUMN_UNIT_PRICE = "unit_price";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_TYPE = "type";







    public DatabaseCartHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        String exe = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
                + "(" + COLUMN_CID + " integer primary key, "
                + COLUMN_ID + " integer NOT NULL,"
                + COLUMN_QTY + " DOUBLE NOT NULL,"
                + COLUMN_IMAGE + " TEXT NOT NULL, "
                + COLUMN_CAT_ID + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PRICE + " DOUBLE NOT NULL, "
                + COLUMN_MRP + " DOUBLE NOT NULL, "
                + COLUMN_STOCK + " DOUBLE NOT NULL, "
                + COLUMN_UNIT_PRICE + " DOUBLE NOT NULL, "
                + COLUMN_UNIT + " TEXT NOT NULL, "
                + COLUMN_TYPE + " TEXT NOT NULL "


                + ")";
        String exec = "CREATE TABLE IF NOT EXISTS " + SINGLE_CART
                + "(" + COLUMN_CID + " integer primary key, "
                + COLUMN_ID + " integer NOT NULL,"
                + COLUMN_QTY + " DOUBLE NOT NULL,"
                + COLUMN_IMAGE + " TEXT NOT NULL, "
                + COLUMN_CAT_ID + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PRICE + " DOUBLE NOT NULL, "
                + COLUMN_MRP + " DOUBLE NOT NULL, "
                + COLUMN_STOCK + " DOUBLE NOT NULL, "
                + COLUMN_UNIT_PRICE + " DOUBLE NOT NULL, "
                + COLUMN_UNIT + " TEXT NOT NULL, "
                + COLUMN_TYPE + " TEXT NOT NULL "


                + ")";

        db.execSQL(exe);
        db.execSQL(exec);

    }

    public boolean setCart(HashMap<String, String> map, Float Qty) {
        db = getWritableDatabase();
        if (isInCart(map.get(COLUMN_CID))) {
            db.execSQL("update " + CART_TABLE + " set " + COLUMN_QTY + " = '" + Qty + "'," + COLUMN_PRICE + " = '" + map.get(COLUMN_PRICE) + "' where " + COLUMN_CID + "=" + map.get(COLUMN_CID));
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CID, map.get(COLUMN_CID));
            values.put(COLUMN_ID, map.get(COLUMN_ID));
            values.put(COLUMN_QTY, Qty);
            values.put(COLUMN_CAT_ID, map.get(COLUMN_CAT_ID));
            values.put(COLUMN_IMAGE, map.get(COLUMN_IMAGE));
            values.put(COLUMN_STOCK, map.get(COLUMN_STOCK));
            values.put(COLUMN_NAME, map.get(COLUMN_NAME));
            values.put(COLUMN_PRICE, map.get(COLUMN_PRICE));
            values.put(COLUMN_MRP, map.get(COLUMN_MRP));
            values.put(COLUMN_UNIT_PRICE, map.get(COLUMN_UNIT_PRICE));
            values.put(COLUMN_UNIT, map.get(COLUMN_UNIT));
            values.put(COLUMN_TYPE, map.get(COLUMN_TYPE));


            // values.put(COLUMN_UNIT_VALUE, map.get(COLUMN_UNIT_VALUE));
            //   values.put(COLUMN_DESC, map.get(COLUMN_DESC));


            db.insert(CART_TABLE, null, values);

            return true;
        }
    }

    public boolean setSingleCart(HashMap<String, String> map, Float Qty) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CID, map.get(COLUMN_CID));
        values.put(COLUMN_ID, map.get(COLUMN_ID));
        values.put(COLUMN_QTY, Qty);
        values.put(COLUMN_CAT_ID, map.get(COLUMN_CAT_ID));
        values.put(COLUMN_IMAGE, map.get(COLUMN_IMAGE));
        values.put(COLUMN_STOCK, map.get(COLUMN_STOCK));
        values.put(COLUMN_NAME, map.get(COLUMN_NAME));
        values.put(COLUMN_PRICE, map.get(COLUMN_PRICE));
        values.put(COLUMN_MRP, map.get(COLUMN_MRP));
        values.put(COLUMN_UNIT_PRICE, map.get(COLUMN_UNIT_PRICE));
        values.put(COLUMN_UNIT, map.get(COLUMN_UNIT));
        values.put(COLUMN_TYPE, map.get(COLUMN_TYPE));


        // values.put(COLUMN_UNIT_VALUE, map.get(COLUMN_UNIT_VALUE));
        //   values.put(COLUMN_DESC, map.get(COLUMN_DESC));


        db.insert(SINGLE_CART, null, values);

        return true;

    }

    public boolean updateCart(HashMap<String, String> map, Float Qty)
    {
        db = getWritableDatabase();
        if (isInCart(map.get(COLUMN_CID))) {
            db.execSQL("update " + CART_TABLE + " set " + COLUMN_QTY + " = '" + Qty + "'," + COLUMN_PRICE + " = '" + map.get(COLUMN_PRICE) + "' where " + COLUMN_CID + "=" + map.get(COLUMN_CID));
            return true;
        }
        return false;


    }

    public boolean isInCart(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_CID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) return true;
        return false;
    }


    public String getCartItemQty(String id) {

        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_CID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));

    }

    public String getInCartItemQty(String id) {
        if (isInCart(id)) {
            db = getReadableDatabase();
            String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_CID + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
        } else {
            return "0.0";
        }
    }

    public int getCartCount() {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        return cursor.getCount();
    }

    public String getTotalAmount() {
        db = getReadableDatabase();
//        String qry = "Select SUM(" + COLUMN_PRICE + " * "+ COLUMN_QTY + " ) as total_amount  from " + CART_TABLE;
        String qry = "Select SUM(" + COLUMN_PRICE + ") as total_amount  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String total = cursor.getString(cursor.getColumnIndex("total_amount"));
        if (total != null) {

            return total;
        } else {
            return "0";
        }
    }

    public String getTotalMRP() {
        db = getReadableDatabase();
        String qry = "Select SUM(" + COLUMN_MRP + ") as total_mrp  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String total = cursor.getString(cursor.getColumnIndex("total_mrp"));
        if (total != null) {

            return total;
        } else {
            return "0";
        }
    }


    public ArrayList<HashMap<String, String>> getCartAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_CID, cursor.getString(cursor.getColumnIndex(COLUMN_CID)));
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndex(COLUMN_QTY)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            map.put(COLUMN_CAT_ID, cursor.getString(cursor.getColumnIndex(COLUMN_CAT_ID)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
            map.put(COLUMN_MRP, cursor.getString(cursor.getColumnIndex(COLUMN_MRP)));
            map.put(COLUMN_UNIT_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_PRICE)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndex(COLUMN_STOCK)));
            map.put(COLUMN_UNIT, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT)));
            map.put(COLUMN_TYPE, cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
//            map.put(COLUMN_DESC, cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));

            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public ArrayList<HashMap<String, String>> getSingleCartAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + SINGLE_CART;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_CID, cursor.getString(cursor.getColumnIndex(COLUMN_CID)));
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndex(COLUMN_QTY)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            map.put(COLUMN_CAT_ID, cursor.getString(cursor.getColumnIndex(COLUMN_CAT_ID)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
            map.put(COLUMN_MRP, cursor.getString(cursor.getColumnIndex(COLUMN_MRP)));
            map.put(COLUMN_UNIT_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_PRICE)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndex(COLUMN_STOCK)));
            map.put(COLUMN_UNIT, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT)));
            map.put(COLUMN_TYPE, cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));

            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public ArrayList<HashMap<String, String>> getCartProduct(int product_id) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE+ " where " + COLUMN_CID + " = " + product_id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_CID, cursor.getString(cursor.getColumnIndex(COLUMN_CID)));
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndex(COLUMN_QTY)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            map.put(COLUMN_CAT_ID, cursor.getString(cursor.getColumnIndex(COLUMN_CAT_ID)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
            map.put(COLUMN_MRP, cursor.getString(cursor.getColumnIndex(COLUMN_MRP)));
            map.put(COLUMN_UNIT_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_PRICE)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndex(COLUMN_STOCK)));
            map.put(COLUMN_UNIT, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT)));
            map.put(COLUMN_TYPE, cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
//            map.put(COLUMN_DESC, cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));

            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }


    public String getColumnRewards() {
        db = getReadableDatabase();
        String qry = "SELECT rewards FROM "+ CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String reward = cursor.getString(cursor.getColumnIndex("rewards"));
        if (reward != null) {

            return reward;
        } else {
            return "0";
        }
    }

    public String getFavConcatString() {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String concate = "";
        for (int i = 0; i < cursor.getCount(); i++) {
            if (concate.equalsIgnoreCase("")) {
                concate = cursor.getString(cursor.getColumnIndex(COLUMN_CID));
            } else {
                concate = concate + "_" + cursor.getString(cursor.getColumnIndex(COLUMN_CID));
            }
            cursor.moveToNext();
        }
        return concate;
    }

    public void clearCart() {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE);
    }

    public void clearSingleCart() {
        db = getReadableDatabase();
        db.execSQL("delete from " + SINGLE_CART);
    }

    public void removeItemFromCart(String id) {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE + " where " + COLUMN_CID + " = " + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
