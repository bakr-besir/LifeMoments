package com.bakr.diarymaker;

import static com.bakr.diarymaker.MainActivity.db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import bkr.android.StorageManager;

public class ExportActivity extends AppCompatActivity {
    EditText et;
    public static final String PACKAGE = "Diaries";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        et = findViewById(R.id.et);
        StringBuilder story = new StringBuilder();
        for (String s : db.text) story.append(s);
        et.setText(story);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.export_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.export_txt) {
            String first_time = db.text.get(0).substring(db.text.get(0).indexOf(" ") + 1, db.text.get(0).indexOf("M") + 1);

            first_time = first_time.replaceAll(":", "-");
            String folder = PACKAGE + "/" + "diary" + first_time;
            StorageManager.createFolders(folder);
            StorageManager.createFile(folder  + "/" + "diary" + first_time + ".txt", et.getText().toString());
            Toast.makeText(this, folder + "/" + "diary" + first_time + ".txt", Toast.LENGTH_SHORT).show();

            for (int i = 0, textSize = db.text.size(); i < textSize; i++) {
                db.update(i, "#"+db.text.get(i).substring(1));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}