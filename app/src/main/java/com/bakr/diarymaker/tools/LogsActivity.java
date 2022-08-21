package com.bakr.diarymaker.tools;


import static com.bakr.diarymaker.tools.MainActivity.db;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bakr.diarymaker.R;

import java.util.Random;

public class LogsActivity extends AppCompatActivity {
    TextView tv;
    public static float size = 13f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools_logs);
        tv = findViewById(R.id.story);
        tv.setText(Logs.logs);
        tv.setTextSize(size);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logs_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.notes2) {
            updateNotesDialog();
            return true;
        }
        if (item.getItemId() == R.id.big_txt) {
            size += 2;
            tv.setTextSize(size);
            return true;
        }
        if (item.getItemId() == R.id.e2_logs) {
            if (Logs.Enabled) {
                Toast.makeText(this, "Logs already enabled.", Toast.LENGTH_SHORT).show();
            } else {
                Logs.Enabled = true;
                Toast.makeText(this, "Logs enabled.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (item.getItemId() == R.id.d2_logs) {
            if (!Logs.Enabled) {
                Toast.makeText(this, "Logs already disabled.", Toast.LENGTH_SHORT).show();
            } else {
                Logs.Enabled = false;
                Toast.makeText(this, "Logs disabled.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (item.getItemId() == R.id.clear_lgs) {
            if (Logs.logs.length() > 0) {
                Logs.clear();
                Toast.makeText(this, "Logs cleared.", Toast.LENGTH_SHORT).show();
                tv.setText(Logs.logs);
            } else {
                Toast.makeText(this, "Logs are empty.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (item.getItemId() == R.id.sml_txt) {
            size -= 2;
            tv.setTextSize(size);
            return true;
        }
        if (item.getItemId() == R.id.back) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.go_code) {
            startActivity(new Intent(LogsActivity.this, CodeActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.rtc) {
            int color = 0;
            switch (new Random().nextInt(10)) {
                case 0:
                    color = Color.BLACK;
                    break;
                case 1:
                    color = Color.WHITE;
                    break;
                case 2:
                    color = Color.RED;
                    break;
                case 3:
                    color = Color.YELLOW;
                    break;
                case 4:
                    color = Color.GRAY;
                    break;
                case 5:
                    color = Color.GREEN;
                    break;
                case 6:
                    color = Color.DKGRAY;
                    break;
                case 7:
                    color = Color.LTGRAY;
                    break;
                case 8:
                    color = Color.MAGENTA;
                    break;
                case 9:
                    color = Color.CYAN;
                    break;
            }
            tv.setTextColor(color);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateNotesDialog() {
        final Dialog dialog = new Dialog(LogsActivity.this);
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
