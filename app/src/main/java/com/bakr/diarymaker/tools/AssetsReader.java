package com.bakr.diarymaker.tools;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AssetsReader {

    public static FileItem readItem(Context context, String item_name, FileItem parent) {
        FileItem fileItem;
        if (item_name.contains("."))
        {
            // read file
            fileItem = new FileItem(item_name, true);
            BufferedReader reader = null;
            try {
                StringBuilder file_content = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(context.getAssets().open(item_name), "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    file_content.append(line).append("\n");
                }

                fileItem.content = file_content.toString();
            } catch (Exception ignored) {}
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        else
        {
            fileItem = new FileItem(item_name, false);

            try {
                String[] files = context.getAssets().list(item_name);

                for (String s : files) {
                    String new_name = item_name + "/" + s;

                    fileItem.add(readItem(context, new_name, fileItem));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        fileItem.parent = parent;
        return fileItem;
    }

    public static String print(FileItem item) {
        StringBuilder a = new StringBuilder(item.isFile ? "FILE " + item.name + " content '" + item.content + "'\n" : "FOLDER " + item.name + " items " + item.files.size() + "\n");
        String pad = "";
        if (!item.isFile) {
            for (FileItem i : item.files) {
                a.append(print(pad, i));}}

        return a.toString();
    }

    private static String print(String pad, FileItem item) {
        pad+="  ";
        StringBuilder a = new StringBuilder(pad + (item.isFile ? "FILE " + item.name + " content '" + item.content.replaceAll("\n", "").substring(0, 3) + "'" : "FOLDER " + item.name + " items " + item.files.size()) + "\n");
        if (!item.isFile) {
            for (FileItem i : item.files) {
                a.append(pad).append(print(pad,i));}}

        return a.toString();
    }

    public static class FileItem {
        public boolean isFile;
        public String name, content;
        public List<FileItem> files;
        public FileItem parent;

        public FileItem(String name, boolean isFile) {
            this.name = name;
            this.isFile = isFile;
            if (!isFile) {
                files = new ArrayList<>();
            }
        }

        public FileItem(String name, boolean isFile, String content) {
            this(name, isFile);
            this.content = content;
        }

        public void addFile(String name, String content) {
            files.add(new FileItem(name, true, content));
        }

        public void add(String name) {
            files.add(new FileItem(name, false));
        }

        public void add(FileItem item) {
            files.add(item);
        }
    }
}
