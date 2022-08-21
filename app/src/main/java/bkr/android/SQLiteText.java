package bkr.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteText extends SQLiteOpenHelper {
    private final List<String> ids = new ArrayList<>();
    public final List<String> text = new ArrayList<>();

    private final String DATABASE_NAME;
    private static final String COLUMN_ID = "_id";
    private static final String NOTE_TEXT = "text_data";
    private final String TABLE_NAME;

    public SQLiteText(Context context, String database_db, String table) {
        super(context, database_db, null, 1);
        this.DATABASE_NAME = database_db;
        this.TABLE_NAME = table;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TEXT + " TEXT);");
        Log.e("Test", "Hello");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public void add(String text) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TEXT, text);

        long result = database.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            Log.e("TAG", "failed.");
        } else {
            Log.e("TAG", "Added Successfully.");
        }
        load();
    }

    public void load() {
        ids.clear();
        text.clear();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getString(0));
                text.add(cursor.getString(1));
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    public void update(int index, String text) {
        this.text.set(index, text);
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

    public void delete(int index) {
        SQLiteDatabase database = getWritableDatabase();
        long result = database.delete(TABLE_NAME, "_id=?", new String[]{ids.get(index)});

        if (result == -1) {
            Log.e("TAG", "failed.");
        } else {
            Log.e("TAG", "Added Successfully.");
        }
        load();
    }

    public void delete() {
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME);
        load();
    }
}
