package com.bakr.diarymaker.tools;

public class Logs {
    public static StringBuilder logs = new StringBuilder();
    private static String last_text;
    private static boolean repeating;
    private static int repeated_count;
    public static boolean Enabled = true;

    public static void println(String txt) {
        if (Enabled){
            txt = txt + "\n";
            if (txt.equals(last_text)) {
                if (repeating) {
                    // update count
                    logs.setLength(logs.lastIndexOf("x")+1);
                    logs.append(repeated_count);
                }
                else {
                    // add '  x??'
                    repeated_count = 2;
                    if (logs.charAt(logs.length()-1) == '\n'){
                        logs.setLength(logs.length()-1);
                    }
                    logs.append("  x");
                    logs.append(repeated_count);
                    repeating = true;
                }
                repeated_count ++;
            }
            else {
                if (repeating) {
                    logs.append("\n");
                    repeating = false;
                }
                logs.append(txt);
                last_text = txt;
            }
        }
    }

    public static void clear() {
        logs = new StringBuilder();
        last_text = "";
    }
}
