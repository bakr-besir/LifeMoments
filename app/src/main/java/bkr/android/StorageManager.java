package bkr.android;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class StorageManager {
    /*
          <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
          <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
          <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>

          android:requestLegacyExternalStorage="true"

          android:largeHeap="true"
    */

    public static boolean isPermissionsGranted(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readExternalStorage = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            );
            return readExternalStorage == PackageManager.PERMISSION_GRANTED;
        }
    }

    public static void askPermission(Activity context) {
        if (!isPermissionsGranted(context)) {
            new AlertDialog.Builder(context)
                    .setTitle("All Files permission")
                    .setMessage("Due to android 11 restrictions, this app requires all files permission.")
                    .setPositiveButton("Allow", (dialogInterface, i) -> {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                context.startActivityForResult(intent, 101);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                context.startActivityForResult(intent, 101);
                            }
                        } else {
                            ActivityCompat.requestPermissions(context, new String[] {
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, 101);
                        }

                    })
                    .setNegativeButton("Deny", (dialogInterface, i) -> {

                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public static boolean createFile(String pathname, String content) {
        try {
            FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + pathname);
            out.write(content.getBytes());
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createImageFile(String pathname, Bitmap img) {
        try {
            FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + pathname);
            img.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createFolders(String pathname) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + pathname);
        if (!folder.exists()) {
            return folder.mkdirs();
        }
        return true;
    }

    public static String readFile(String fileName) {

        // Get the dir of SD Card
        File sdCardDir = Environment.getExternalStorageDirectory();

        // Get The Text file
        File txtFile = new File(sdCardDir, fileName);

        // Read the file Contents in a StringBuilder Object
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(txtFile));

            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line).append('\n');
            }
            reader.close();
        } catch (IOException e) {
            Log.e("C2c", "Error occured while reading text file!!" + e.getMessage());
        }
        return text.toString();
    }
}
