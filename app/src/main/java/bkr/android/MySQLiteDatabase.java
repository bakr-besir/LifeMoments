package bkr.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteDatabase extends SQLiteOpenHelper {
    private static final String TAG = "MySQLiteDataBase";
    private final String TABLE_NAME;
    public final String DATA = "data";

    public MySQLiteDatabase(Context context, String databaseName, String tableName) {
        super(context, databaseName, null, 1);
        this.TABLE_NAME = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + DATA + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public Cursor read() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        }
        return cursor;
    }

    public void delete() {
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME);
    }

    public void delete(String index) {
        SQLiteDatabase database = getWritableDatabase();
        long result = database.delete(TABLE_NAME, "_id=?", new String[]{index});

        if (result == -1) {
            Log.e(TAG, "failed delete data.");
        }
    }

    public void update(ContentValues cv, String index) {
        SQLiteDatabase database = getWritableDatabase();

        long result = database.update(TABLE_NAME, cv, "_id=?", new String[]{index});

        if (result == -1) {
            Log.e(TAG, "failed update data.");
        }
    }

    public void insert(ContentValues cv) {
        long result = getWritableDatabase().insert(TABLE_NAME, null, cv);

        if (result == -1) {
            Log.e(TAG, "failed to insert data.");
        }
    }
}
