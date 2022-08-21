package com.bakr.diarymaker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bkr.android.ImagePicker;
import bkr.android.SQLiteText;
import bkr.android.StorageManager;

public class MainActivity extends AppCompatActivity {
    static SQLiteText db;
    ListView listView;
    MyAdapter adapter;
    List<Part> parts;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);

        // storage permission
        StorageManager.askPermission(this);

        // camera permission
        EnableRuntimePermission();

        db = new SQLiteText(this, "Papers.db", "paper");
        db.load();


        // load parts
        parts = new ArrayList<>();
        for (String p : db.text) {
            Part part = new Part();
            part.flag = p.charAt(0);
            part.state = String.valueOf(p.charAt(1));
            part.time = p.substring(p.indexOf(" "), p.indexOf("M") + 1).trim();
            part.story = p.substring(p.indexOf("M") + 1).trim();
            parts.add(part);
        }
        adapter = new MyAdapter(this, parts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            index = position;
            show_part(true, db.text.get(position), false);
        });
    } // 2022-7-8 20:54 PM

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
            // imageView.setImageBitmap(bitmap);
            Toast.makeText(this, "w:"+bitmap.getWidth()+", h:"+bitmap.getHeight(), Toast.LENGTH_SHORT).show();
            if (StorageManager.createImageFile("Diaries/photos/image-test.jpg", bitmap)){
                Toast.makeText(this, "image saved.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "image not saved.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.export_paper) {
            startActivity(new Intent(this, ExportActivity.class));
            return true;
        }

        if (item.getItemId() == R.id.add_part) {
            show_part(false, "", false);
            return true;
        }

        if (item.getItemId() == R.id.deleteAll) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setMessage("Delete all parts?");
            builder.setPositiveButton("Yes",
                    (dialog, which) -> {
                        db.delete();
                        update_parts();
                        dialog.dismiss();
                    });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        if (item.getItemId() == R.id.mark_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setMessage("Mark all parts?");
            builder.setPositiveButton("Yes",
                    (dialog, which) -> {
                        for (int i = 0, textSize = db.text.size(); i < textSize; i++) {
                            db.update(i, "#" + db.text.get(i).substring(1));
                        }
                        update_parts();
                        dialog.dismiss();
                    });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        if (item.getItemId() == R.id.unmark_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setMessage("Unmark all parts?");
            builder.setPositiveButton("Yes",
                    (dialog, which) -> {
                        for (int i = 0, textSize = db.text.size(); i < textSize; i++) {
                            db.update(i, "@" + db.text.get(i).substring(1));
                        }
                        update_parts();
                        dialog.dismiss();
                    });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        if (item.getItemId() == R.id.about) {
            final Dialog dialog3 = new Dialog(this);

            dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog3.setCancelable(true);

            TextView textView = new TextView(this);
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(800, 1000);

            textView.setLayoutParams(p2);
            layout.setLayoutParams(p2);
            layout.addView(textView);
            textView.setText("Diary Maker 1.8\n- ability to delete all parts at once.\n- marge place with story.\n- mark parts when they saved.\n- ask to change time if seems wrong.\n- adding about window.");
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            dialog3.setContentView(layout);

            dialog3.show();
            return true;
        }

        if (item.getItemId() == R.id.take_photo) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 7);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SimpleDateFormat")
    private void show_part(boolean edit_node, String part, boolean add_here) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.part_dialog);

        EditText time = dialog.findViewById(R.id.paper_time);
        EditText story = dialog.findViewById(R.id.paper_des);
        TextView can = dialog.findViewById(R.id.cancel);
        TextView ok = dialog.findViewById(R.id.ok);
        TextView curr = dialog.findViewById(R.id.curr);
        Spinner spinner = dialog.findViewById(R.id.spnr);

        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.dde, new String[]{"Normal", "Happy", "Art", "Surprise", "Idea", "Sad", "Other"}));

        if (edit_node) {
            String s = part.substring(0, part.indexOf(" "));
            String t = part.substring(part.indexOf(" "), part.indexOf("M") + 1).trim();
            String d = part.substring(part.indexOf("M") + 1).trim();

            int pos = 0;
            switch (s) {
                case "@h":
                    pos = 1;
                    break;
                case "@a":
                    pos = 2;
                    break;
                case "@p":
                    pos = 3;
                    break;
                case "@i":
                    pos = 4;
                    break;
                case "@s":
                    pos = 5;
                    break;
                case "@o":
                    pos = 6;
                    break;
            }
            time.setText(t);
            spinner.setSelection(pos);
            story.setText(d);
        } else {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format;
            if (db.text.size() == 0) {
                format = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            } else {
                format = new SimpleDateFormat("hh:mm a");
            }

            time.setText(format.format(date));
        }

        ok.setOnClickListener(view -> {
            if (edit_node) {
                if (part.startsWith("#") && part.substring(part.indexOf(" ") + 1, part.indexOf("M") + 1).equals(time.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(true);
                    builder.setMessage("Update time!");
                    builder.setPositiveButton("OK", (dialog3, which) -> dialog3.dismiss());

                    AlertDialog dialog3 = builder.create();
                    dialog3.show();
                } else {
                    String s = "@";
                    switch (spinner.getSelectedItemPosition()) {
                        case 1:
                            s += "h";
                            break;
                        case 2:
                            s += "a";
                            break;
                        case 3:
                            s += "p";
                            break;
                        case 4:
                            s += "i";
                            break;
                        case 5:
                            s += "s";
                            break;
                        case 6:
                            s += "o";
                            break;
                        default:
                            s += "n";
                    }
                    String out = s + " " + time.getText().toString() + " " + story.getText().toString() + " ";
                    db.update(index, out);
                    update_parts();
                    dialog.dismiss();
                }
            } else {
                String s = "@";
                switch (spinner.getSelectedItemPosition()) {
                    case 1:
                        s += "h";
                        break;
                    case 2:
                        s += "a";
                        break;
                    case 3:
                        s += "p";
                        break;
                    case 4:
                        s += "i";
                        break;
                    case 5:
                        s += "s";
                        break;
                    case 6:
                        s += "o";
                        break;
                    default:
                        s += "n";
                }
                String out = s + " " + time.getText().toString() + " " + story.getText().toString() + " ";
                if (add_here){
                    addHere(index, out);
                }else {
                    db.add(out);
                    update_parts();
                }

                dialog.dismiss();

            }
        });

        can.setOnClickListener(view -> dialog.dismiss());

        curr.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setMessage("What do you want to apply?");

            builder.setPositiveButton("Date and Time", (dialog2, which) -> {
                time.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(new Date(System.currentTimeMillis())));
                dialog2.dismiss();
            });
            builder.setNegativeButton("Time", (dialog2, which) -> {
                time.setText(new SimpleDateFormat("hh:mm a").format(new Date(System.currentTimeMillis())));
                dialog2.dismiss();
            });
            AlertDialog dialog2 = builder.create();
            dialog2.show();
        });

        dialog.show();
    }

    static class Part {
        char flag;
        String state;
        String time;
        String story;
    }

    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA}, 666);
        }
    }

    class MyAdapter extends ArrayAdapter<Part> {

        Context context;
        List<Part> items;

        MyAdapter(Context c, List<Part> items) {
            super(c, R.layout.custom_item, items);
            this.context = c;
            this.items = items;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.custom_item, parent, false);

            RelativeLayout bg = row.findViewById(R.id.par);
            TextView story = row.findViewById(R.id.story);
            TextView time = row.findViewById(R.id.time);
            TextView delete = row.findViewById(R.id.delete);
            TextView add_here = row.findViewById(R.id.add_here);
            TextView up = row.findViewById(R.id.move_up);
            TextView dn = row.findViewById(R.id.move_dn);
            // now set our resources on views

            if (items.get(position).flag == '@') {
                bg.setBackgroundColor(Color.WHITE);
            } else {
                bg.setBackgroundColor(Color.YELLOW);
            }

            story.setText(items.get(position).story);

            time.setText("@" + items.get(position).state + " " + items.get(position).time);

            delete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Do you want to delete this part?");
                builder.setPositiveButton("Yes",
                        (dialog, which) -> {
                            db.delete(position);
                            update_parts();
                            dialog.dismiss();
                        });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            });

            add_here.setOnClickListener(v -> {
                index = position;
                show_part(false, "", true);
            });

            up.setOnClickListener(v -> {
                if (position == 0) {
                    Toast.makeText(context, "can not move up.", Toast.LENGTH_SHORT).show();
                } else {
                    String t = db.text.get(position - 1);
                    db.update(position - 1, db.text.get(position));
                    db.update(position, t);
                    update_parts();
                }
            });

            dn.setOnClickListener(v -> {
                if (position == items.size() - 1) {
                    Toast.makeText(context, "can not move down.", Toast.LENGTH_SHORT).show();
                } else {
                    String t = db.text.get(position + 1);
                    db.update(position + 1, db.text.get(position));
                    db.update(position, t);
                    update_parts();
                }
            });

            return row;
        }
    }

    public void update_parts() {
        int len = db.text.size();
        // make size same to copy
        while (parts.size() != len) {
            if (parts.size() < len) {
                parts.add(new Part());
            } else {
                parts.remove(0);
            }
        }

        // edit all parts
        for (int i = 0, l = db.text.size(); i < l; i++) {
            String p = db.text.get(i);
            parts.get(i).flag = p.charAt(0);
            parts.get(i).state = String.valueOf(p.charAt(1));
            parts.get(i).time = p.substring(p.indexOf(" "), p.indexOf("M") + 1).trim();
            parts.get(i).story = p.substring(p.indexOf("M") + 1).trim();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update_parts();
        adapter.notifyDataSetChanged();
    }

    private void addHere(int index, String part) {
        // check if list has that size to added to db
        List<String> stored_parts = new ArrayList<>();
        if (index < db.text.size()) {
            // store all parts from index to end
            for (int i = index; i < db.text.size(); i++) {
                stored_parts.add(db.text.get(i));
            }

            // delete all parts from index to end
            while (index != db.text.size()) {
                db.delete(db.text.size() - 1);
            }

            // add part here
            db.add(part);

            // add stored parts
            for (String s : stored_parts) {
                db.add(s);
            }
        }
        update_parts();
    }
}
