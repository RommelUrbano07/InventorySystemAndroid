 package com.example.inventorysystem_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


 public class DBAdapter {

    myDbHelper myhelper;
    Message message;
    public DBAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "sampleDB";    // Database Name
        private static final int DATABASE_Version = 1;    // Database Version
        private final Context context;

        // Database functions Edit,Update,Delete Item/Person

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(
                        "Create Table account(" +
                                "accountID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                "FirstName VARCHAR(255)," +
                                "MiddleName VARCHAR(255)," +
                                "LastName VARCHAR(255)," +
                                "Suffix VARCHAR(255)," +
                                "username VARCHAR(255)," +
                                "password VARCHAR(255)" +
                        "); "
                );
                db.execSQL(
                        "Create Table Inventory(" +
                                "accountID INTEGER NOT NULL," +
                                "ItemName NOT NULL," +
                                "ItemQuantity NOT NULL" +
                        " ); "
                );
            } catch (Exception e) {
                Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context,"OnUpgrade");
                db.execSQL("DROP TABLE IF EXISTS account;");
                db.execSQL("DROP TABLE IF EXISTS Inventory;");
                onCreate(db);
            }catch (Exception e) {
                Message.message(context,""+e);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long insertItemData(String value, String itemname, String itemquantity)
    {
        long id=0;
        if(checkItemExists(value,itemname)==false){
            SQLiteDatabase dbb = myhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("accountID", Integer.parseInt(value));
            contentValues.put("ItemName", itemname);
            contentValues.put("ItemQuantity", Integer.parseInt(itemquantity));
            id = dbb.insert("Inventory", null , contentValues);
        }else{
            id = addItem(value,itemname,itemquantity);
        }
        return id;
    }

    public boolean checkItemExists(String value, String itemname){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {value,itemname};
        Cursor cursor = db.rawQuery("Select * from Inventory where accountID = ? and ItemName =?", columns);
        while (cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex("ItemName"));
            if(itemname.equals(name)){
               return true;
            }
        }
        return false;
    }

    public int returnitemQuantity(String value,String itemname){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {itemname,value};
        Cursor cursor =db.rawQuery("Select * from Inventory where ItemName = ? AND accountID= ? ",columns );
        while (cursor.moveToNext())
        {
            int quantity = cursor.getInt(cursor.getColumnIndex("ItemQuantity"));
            return quantity;
        }
        return -1;
    }


    public int addItem(String value, String itemname, String itemquantity){
        int oldval= returnitemQuantity(value,itemname);
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int sum = oldval + Integer.parseInt(itemquantity);
        contentValues.put("ItemQuantity",sum);
        String[] whereArgs= {itemname,value};
        int count =db.update("Inventory",contentValues, "ItemName = ? AND accountID =?",whereArgs );
        return count;
    }


    public int subtractItem(String value, String itemname, String itemquantity){
        int db_quantity = returnitemQuantity(value,itemname);
        if( (db_quantity-Integer.parseInt(itemquantity)) < 0 ){
            return -1;
        }else{
            return addItem(value,itemname,(-Integer.parseInt(itemquantity))+"" );
        }
    }

    public  int delete(String value,String itemname)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={value,itemname};
        int count =db.delete("Inventory" ,"accountID=? AND ItemName = ?",whereArgs);
        return  count;
    }

    public int updateItem(String value,String Itemname , String newquantity)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemQuantity",newquantity);
        String[] whereArgs= {Itemname,value};
        int count =db.update("Inventory",contentValues, "ItemName = ? AND accountID=?",whereArgs );
        return count;
    }

    public String getUserFirstName(String userID)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {"accountID","FirstName"};
        Cursor cursor =db.query("account",columns,null,null,null,null,null);
        String name="No user found";
        while (cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex("accountID"));
            if(userID.equals(id+"")){
                name=cursor.getString(cursor.getColumnIndex("FirstName"));
            }
        }
        return name;
    }

    public Cursor getItemOnID(String userID){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {"ItemName","ItemQuantity"};
        Cursor cursor =db.rawQuery("Select * from Inventory where accountID = ?",new String[] {userID} );
        return cursor;
    }

    public int getUserData(String username, String password)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {"accountID","username","password","FirstName","MiddleName","LastName","Suffix"};
        Cursor cursor =db.query("account",columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex("accountID"));
            String user =cursor.getString(cursor.getColumnIndex("username"));
            String pass =cursor.getString(cursor.getColumnIndex("password"));
            if(user.equals(username) && pass.equals(password)){
                return cid;
            }
        }
        return -1;
    }

    public long insertData(String username, String password, String firstname, String middlename, String lastname, String suffix)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("FirstName", firstname);
        contentValues.put("MiddleName", middlename);
        contentValues.put("LastName", lastname);
        contentValues.put("Suffix", suffix);
        long id = dbb.insert("account", null , contentValues);
        return id;
    }
}

