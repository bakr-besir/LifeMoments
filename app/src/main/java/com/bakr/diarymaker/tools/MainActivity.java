package com.bakr.diarymaker.tools;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.bakr.diarymaker.R;

import java.util.Random;

import bkr.android.SQLiteText;

public class MainActivity extends AppCompatActivity {

    public static SQLiteText db;
    ListView listGames;
    private final Game[] games = {
            new Game("Paper Creator", com.bakr.diarymaker.MainActivity.class),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools_main);
        listGames = findViewById(R.id.list_games);
        db = new SQLiteText(this, "tools.db", "tools");
        db.load();
        if (db.text.size() == 0) {
            db.add("0");
            db.add("");
        }

        String[] data = new String[games.length];
        for (int i = 0; i < games.length; i++) {
            data[i] = games[i].name;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listGames.setAdapter(adapter);

        listGames.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(this, data[position], Toast.LENGTH_SHORT).show();
            db.update(0, position + "");
            startActivity(new Intent(this, games[position].pos));
        });
        Random r = new Random();
       // if (r.nextBoolean() || r.nextBoolean()) {
            startActivity(new Intent(this, games[Integer.parseInt(db.text.get(0))].pos));
      //  }

        SourceCode.filesSourceCode = AssetsReader.readItem(this, "test", null);
        SourceCode.fileItemList = SourceCode.filesSourceCode.files;

       // Logs.println(AssetsReader.print(SourceCode.filesSourceCode));
    }

    private static class Game {
        String name;
        Class<?> pos;

        public Game(String name, Class<?> pos) {
            this.name = name;
            this.pos = pos;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.notes) {
            updateNotesDialog();
            return true;
        }
        if (item.getItemId() == R.id.show_logs) {
            startActivity(new Intent(MainActivity.this, LogsActivity.class));
            return true;
        }if (item.getItemId() == R.id.e_logs) {
            if (Logs.Enabled) {
                Toast.makeText(this, "Logs already enabled.", Toast.LENGTH_SHORT).show();
            } else {
                Logs.Enabled = true;
                Toast.makeText(this, "Logs enabled.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }if (item.getItemId() == R.id.d_logs) {
            if (!Logs.Enabled) {
                Toast.makeText(this, "Logs already disabled.", Toast.LENGTH_SHORT).show();
            } else {
                Logs.Enabled = false;
                Toast.makeText(this, "Logs disabled.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (item.getItemId() == R.id.show_code) {
            startActivity(new Intent(MainActivity.this, CodeActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.clear_logs) {

            if (Logs.logs.length() > 0) {
                Logs.clear();
                Toast.makeText(this, "Logs cleared.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Logs are empty.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateNotesDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.note_dialog);

        EditText editText = dialog.findViewById(R.id.d_note);
        editText.setText(db.text.get(1));
        TextView done = dialog.findViewById(R.id.done);

        done.setOnClickListener(view -> {
            db.update(1, editText.getText().toString());
            Toast.makeText(this, "Notes Updated.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}

