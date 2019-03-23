package com.example.dogsearch;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.dogsearch.model.Dog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DogsManager";

    // Contacts table name
    private static final String TABLE_DOGS = "Dogs";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_BREED = "breed";
    private static final String KEY_SUBBREED = "sub_breed";
    private static final String KEY_POTO = "poto";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_DOGS="CREATE TABLE " + TABLE_DOGS + "("
                + KEY_ID +" INTEGER PRIMARY KEY,"
                + KEY_BREED +" TEXT,"
                + KEY_SUBBREED +" TEXT,"
                + KEY_POTO  +" BLOB" + ")";
        db.execSQL(CREATE_TABLE_DOGS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOGS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //Insert values to the table
    public void addDogs(Dog dog){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(KEY_BREED, dog.getBreed());
        values.put(KEY_SUBBREED, dog.getSubBreed());
        values.put(KEY_POTO, dog.getImg() );

        db.insert(TABLE_DOGS, null, values);
        db.close();
    }

    //Retrieve data from the database
    public List<Dog> getAllDogs() {
        List<Dog> dogList = new ArrayList<Dog>();
        String selectQuery = "SELECT  * FROM " + TABLE_DOGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Dog dog = new Dog();
                dog.setBreed(cursor.getString(1));
                dog.setSubBreed(cursor.getString(2));
                dog.setImg(cursor.getBlob(3));
                dogList.add(dog);
            } while (cursor.moveToNext());
        }
        return dogList;
    }

    public Dog getOneDog(String breed, String subBreed){
        String selectQuery = "SELECT  * FROM " + TABLE_DOGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Dog dog = new Dog();

        return dog;
    }

    /**
     *Updating single dog
     **/

    public int updateDog(Dog dog, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BREED, dog.getBreed());
        values.put(KEY_POTO, dog.getImg());


        // updating row
        return db.update(TABLE_DOGS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /**
     *Deleting single dog
     **/

    public void deleteDog(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOGS, KEY_ID + " = ?",
                new String[] { String.valueOf(Id) });
        db.close();
    }

}
