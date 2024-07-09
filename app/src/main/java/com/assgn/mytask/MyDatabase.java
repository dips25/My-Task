package com.assgn.mytask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.assgn.mytask.Notes.Notes;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {

    private static MyDatabase myDatabase;

    public static String DB_NAME = "NotesDb";

    public static final int DB_VERSION = 1;

    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table notes(id integer primary key not null , title text not null , description text not null);";
    public static final String DROP_TABLE = "drop table if exists notes";

    public MyDatabase(Context context) {

        this(context , DB_NAME , null , DB_VERSION);
    }
    public MyDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null , version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void insert(Notes notes) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("title" , notes.getTitle());
        cv.put("description" , notes.getDescription());

        try {

            db.insert("notes" , null , cv);
            db.close();
            Log.d("Insert", "inserted: ");

        } catch (Exception ex) {

            Log.d(MyDatabase.class.getName(), "insert: " + ex.getMessage());


        }



    }

    public ArrayList<Notes> getAllNotes() {

        ArrayList<Notes> notesArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from notes" , null);

        //cursor.moveToFirst();

        while (cursor.moveToNext()) {

            Notes n = new Notes(cursor.getInt(0) , cursor.getString(1) , cursor.getString(2));
            notesArrayList.add(n);
        }

        return notesArrayList;

    }

    public void update(Notes notes) {

        ContentValues cv = new ContentValues();
        cv.put("title" , notes.getTitle());
        cv.put("description" , notes.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            db.update("notes" , cv , "id=?" , new String[]{String.valueOf(notes.getId())});
            db.close();
            Log.d("Update", "updated: ");

        } catch (Exception e) {

            e.printStackTrace();
        }



    }

    public void deleteNote(int id) {

        String delQuery = "delete from notes where id="+Integer.toString(id);

        SQLiteDatabase db = this.getWritableDatabase();

        try{

            db.execSQL(delQuery);
            db.close();
            Log.d("delete", "deleteNote: ");

        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }
}
