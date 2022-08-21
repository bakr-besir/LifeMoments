package com.bakr.diarymaker.tools;

import static com.bakr.diarymaker.tools.MainActivity.db;

import android.app.Dialog;
import android.graphics.Color;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.bakr.diarymaker.R;

import java.util.Random;

public class CodeActivity extends AppCompatActivity {
    public static float size = 13f;
    ArrayAdapter<String> adapter;
    String[] files;
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools_logs);
        tv = findViewById(R.id.story);
        tv.setTextSize(size);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.codes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.notes3) {
            updateNotesDialog();
            return true;
        }
        if (item.getItemId() == R.id.code_files) {
            openFilesDialog();
            return true;
        }
        if (item.getItemId() == R.id.big_txt) {
            size += 2;
            tv.setTextSize(size);
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
        final Dialog dialog = new Dialog(CodeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.note_dialog);

        EditText editText = dialog.findViewById(R.id.d_note);
        editText.setText(db.text.get(1));
        TextView done = dialog.findViewById(R.id.done);

        done.setOnClickListener(view -> {
            db.update(1, editText.getText().toString());
            Toast.makeText(this, "Note updated.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void openFilesDialog() {
        final Dialog dialog = new Dialog(CodeActivity.this);
        {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.code_dialog);
        } // set up

        ListView list = dialog.findViewById(R.id.files_list);
        TextView back = dialog.findViewById(R.id.go_back);

        files = new String[SourceCode.fileItemList.size()];
        for (int i = 0; i < files.length; i++) {
            files[i] = SourceCode.fileItemList.get(i).name;
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, files);
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            if (SourceCode.fileItemList.get(position).isFile) {
                Toast.makeText(this, SourceCode.fileItemList.get(position).name + " opened.", Toast.LENGTH_SHORT).show();
                tv.setText(SourceCode.fileItemList.get(position).content);
                dialog.dismiss();
            } else {
                Toast.makeText(this, SourceCode.fileItemList.get(position).name + " opened.", Toast.LENGTH_SHORT).show();
                SourceCode.fileItemList = SourceCode.fileItemList.get(position).files;
                files = new String[SourceCode.fileItemList.size()];
                for (int i = 0; i < files.length; i++) {
                    files[i] = SourceCode.fileItemList.get(i).name;
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, files);
                list.setAdapter(adapter);
            }
        });

        back.setOnClickListener(v -> {
            SourceCode.fileItemList = SourceCode.filesSourceCode.files;
            files = new String[SourceCode.fileItemList.size()];
            for (int i = 0; i < files.length; i++) {
                files[i] = SourceCode.fileItemList.get(i).name;
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, files);
            list.setAdapter(adapter);
        });

        dialog.show();
    }
}
