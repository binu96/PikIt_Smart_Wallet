package com.acgb.greychainlabs.pikit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DELL on 4/20/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME= "Register.db";
    public static final String TABLE_NAME="register_table";
    public static final String COLUMN_1= "id";
    public static final String COLUMN_2= "fullname";
    public static final String COLUMN_3= "email";
    public static final String COLUMN_4= "password";
    private static final String TABLE_CREATE="create table " +TABLE_NAME +" ( "+ COLUMN_1+ " INTEGER PRIMARY KEY , "+COLUMN_2+" TEXT, "+COLUMN_3+" TEXT, "+COLUMN_4+" TEXT)";
    SQLiteDatabase sqLiteDatabase;

    //creating a table inside the db
    public DBHelper(Context context) {
        super(context,DB_NAME , null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
        this.sqLiteDatabase=sqLiteDatabase;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    //storing new registration details
    public boolean insertRegisterData(User u){
        sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        String query= "SELECT * FROM "+TABLE_NAME;
        Cursor cursor= sqLiteDatabase.rawQuery(query,null);
        int count= cursor.getCount();
        contentValues.put(COLUMN_1,count);

        contentValues.put(COLUMN_2, u.getFullname());
        contentValues.put(COLUMN_3, u.getEmail());
        contentValues.put(COLUMN_4, u.getPassword());
        //long result= sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
       // sqLiteDatabase.close();

        long result= sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result==-1) {
            sqLiteDatabase.close();
            return false;
        }else {
            sqLiteDatabase.close();
            return true; }

    }


    //check if password and username matches
    public boolean verifyLogin(String email, String password){
        sqLiteDatabase= this.getReadableDatabase();
        String query="Select email, password from "+ TABLE_NAME +" where email = '" +email + "' AND password = '" +password +"'";
        Cursor cursor= sqLiteDatabase.rawQuery(query, null);
        String a, b;
        b="not found";
        if(cursor.moveToFirst()){
            do{
                a= cursor.getString(0);b=cursor.getString(1);
                if(a.equals(email) && b.equals(password)){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        return false;
    }
}
