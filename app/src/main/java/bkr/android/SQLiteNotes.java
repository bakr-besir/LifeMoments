package bkr.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteNotes extends SQLiteOpenHelper {
    public static final List<String> ids = new ArrayList<>();
    public static final List<String> notes = new ArrayList<>();

    private static final String DATABASE_NAME = "notes.db";
    private static final String TABLE_NAME = "my_notes";
    private static final String COLUMN_ID = "_id";
    private static final String NOTE_TEXT = "note_data";

    public SQLiteNotes(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TEXT + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public void addNote(String text) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TEXT, text);

        long result = database.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            Log.e("TAG", "failed.");
        } else {
            Log.e("TAG", "Added Successfully.");
        }
        readAllNotes();
    }

    public void readAllNotes() {
        ids.clear();
        notes.clear();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getString(0));
                notes.add(cursor.getString(1));
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    public void updateNote(int index, String text) {
        notes.set(index, text);
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TEXT, text);

        long result = database.update(TABLE_NAME, cv, "_id=?", new String[]{ids.get(index)});

        if (result == -1) {
            Log.e("TAG", "failed.");
        } else {
            Log.e("TAG", "Added Successfully.");
        }
    }

    public void deleteNote(int index) {
        SQLiteDatabase database = getWritableDatabase();
        long result = database.delete(TABLE_NAME, "_id=?", new String[]{ids.get(index)});

        if (result == -1) {
            Log.e("TAG", "failed.");
        } else {
            Log.e("TAG", "Added Successfully.");
        }
        readAllNotes();
    }

    public void deleteAllNotes() {
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME);
        readAllNotes();
    }
}
