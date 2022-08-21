package bkr.android.opengl.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.renderscript.Float2;
import android.renderscript.Float3;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public class Utils {
    public static int length;

    public static String readAssets(Context context, String file) {
        StringBuilder res = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(file)));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                res.append(line);
                res.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    public static FloatBuffer createBuffer(float[] a) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(a.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(a).position(0);
        length = a.length;
        return buffer;
    }

    public static ShortBuffer createBuffer(short[] a) {
        ShortBuffer buffer = ByteBuffer.allocateDirect(a.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        buffer.put(a).position(0);
        length = a.length;
        return buffer;
    }

    public static IntBuffer createBuffer(int[] a) {
        IntBuffer buffer = ByteBuffer.allocateDirect(a.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(a).position(0);
        length = a.length;
        return buffer;
    }

    public static int loadCubeMap(Context context, int[] ids) {
        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {

            return 0;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap[] cubeBitmaps = new Bitmap[6];

        for (int i = 0; i < 6; i++) {
            cubeBitmaps[i] = BitmapFactory.decodeResource(context.getResources(),
                    ids[i], options);

            if (cubeBitmaps[i] == null) {

                GLES20.glDeleteTextures(1, textureObjectIds, 0);
                return 0;
            }
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureObjectIds[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        for (Bitmap bitmap : cubeBitmaps) {
            bitmap.recycle();
        }

        return textureObjectIds[0];
    }

    public static int loadTexture2D(Context context, int id) {
        Bitmap texture = BitmapFactory.decodeResource(context.getResources(), id);
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture, 0);
        texture.recycle();
        return textures[0];
    }

    public static String readRaw(Context context, int id) {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(id)));
        String nextLine;

        try {
            while ((nextLine = reader.readLine()) != null) {
                body.append(nextLine).append('\n');
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body.toString();
    }

    public static void showDialog(Context context, int xml_dialog) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(xml_dialog);

        //t EditText editText = dialog.findViewById(R.id.des);
        // ok.setOnClickListener(view -> {
        //
        // });

        dialog.show();
    }

    public static float barryCentric(float xp1, float yp1, float zp1, float xp2, float yp2, float zp2, float xp3, float yp3, float zp3, float x, float z) {
        float det = ((zp2 - zp3) * (xp1 - xp3)) + ((xp3 - xp2) * (zp1 - zp3));
        float l1 = (((zp2 - zp3) * (x - xp3)) + ((xp3 - xp2) * (z - zp3))) / det;
        float l2 = (((zp3 - zp1) * (x - xp3)) + ((xp1 - xp3) * (z - zp3))) / det;
        return (l1 * yp1) + (l2 * yp2) + (((1.0f - l1) - l2) * yp3);
    }

    public static void rotate(float[] vector, float a2, float[] axis) {
        float s = (float) Math.sin(Math.toRadians(a2));
        float c = (float) Math.cos(Math.toRadians(a2));
        vector[0] = (axis[0] * ((axis[0] * vector[0]) + (axis[1] * vector[1]) + (axis[2] * vector[2])) * (1.0f - c)) + (vector[0] * c) + ((((-axis[2]) * vector[1]) + (axis[1] * vector[2])) * s);
        vector[1] = (axis[1] * ((axis[0] * vector[0]) + (axis[1] * vector[1]) + (axis[2] * vector[2])) * (1.0f - c)) + (vector[1] * c) + (((axis[2] * vector[0]) - (axis[0] * vector[2])) * s);
        vector[2] = (axis[2] * ((axis[0] * vector[0]) + (axis[1] * vector[1]) + (axis[2] * vector[2])) * (1.0f - c)) + (vector[2] * c) + ((((-axis[1]) * vector[0]) + (axis[0] * vector[1])) * s);
    }

    public static void normalize(float[] v) {
        float length2 = (float) Math.sqrt((double) ((v[0] * v[0]) + (v[1] * v[1]) + (v[2] * v[2])));
        v[0] = v[0] / length2;
        v[1] = v[1] / length2;
        v[2] = v[2] / length2;
    }

    public static float[] normalized(float[] v) {
        float length2 = (float) Math.sqrt((double) ((v[0] * v[0]) + (v[1] * v[1]) + (v[2] * v[2])));
        return new float[]{v[0] / length2, v[1] / length2, v[2] / length2};
    }

    public static void cross(float[] src, float[] a2, float[] b) {
        src[0] = (a2[1] * b[2]) - (a2[2] * b[1]);
        src[1] = (a2[2] * b[0]) - (a2[0] * b[2]);
        src[2] = (a2[0] * b[1]) - (a2[1] * b[0]);
    }

    public static float[] cross(float[] a2, float[] b) {
        return new float[]{(a2[1] * b[2]) - (a2[2] * b[1]), (a2[2] * b[0]) - (a2[0] * b[2]), (a2[0] * b[1]) - (a2[1] * b[0])};
    }

    public static int loadTexture(Context context, int id) {
        Bitmap texture = BitmapFactory.decodeResource(context.getResources(), id);
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture, 0);
        texture.recycle();
        return textures[0];
    }

    public static int createShader(String source, int type) {
        int shaderHandle = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderHandle, source);
        GLES20.glCompileShader(shaderHandle);
        int[] isCompiled = new int[1];
        GLES20.glGetShaderiv(shaderHandle, 35713, isCompiled, 0);
        if (isCompiled[0] != 0) {
            return shaderHandle;
        }
        String typeShader = "Unknown Shader";
        if (type == 35633) {
            typeShader = "Vertex Shader";
        } else if (type == 35632) {
            typeShader = "Fragment Shader";
        }
        Log.e("OpenGl", "Cannot compile " + typeShader + ": " + GLES20.glGetShaderInfoLog(shaderHandle));
        GLES20.glDeleteShader(shaderHandle);
        return 0;
    }

    public static int[] createProgram(Context context, int id) {
        String shadersCode = readRaw(context, id);
        int c = shadersCode.indexOf("precision");
        if (c == -1) {
            return null;
        }
        String vsCode = shadersCode.substring(0, c);
        String fsCode = shadersCode.substring(c);
        int[] handles = new int[3];
        handles[1] = createShader(vsCode, 35633);
        if (handles[1] == 0) {
            Log.d("Create Program", "Vertex Shader Failed");
            return new int[3];
        }
        handles[2] = createShader(fsCode, 35632);
        if (handles[2] == 0) {
            Log.d("Create Program", "Fragment Shader Failed");
            return new int[3];
        }
        handles[0] = GLES20.glCreateProgram();
        GLES20.glAttachShader(handles[0], handles[1]);
        GLES20.glAttachShader(handles[0], handles[2]);
        GLES20.glLinkProgram(handles[0]);
        int[] link = new int[1];
        GLES20.glGetProgramiv(handles[0], 35714, link, 0);
        if (link[0] > 0) {
            return handles;
        }
        Log.d("Create Program", "Linking Failed!");
        GLES20.glDeleteProgram(handles[0]);
        return new int[3];
    }

    public static float[] sub(float[] a2, float[] b) {
        return new float[]{a2[0] - b[0], a2[1] - b[1], a2[2] - b[2]};
    }

    public static float[] calculateNormal(float[] a2, float[] b, float[] c) {
        float[] r = cross(sub(c, a2), sub(b, a2));
        normalize(r);
        return r;
    }

    public static float[] centerOf(float[] a2, float[] b, float[] c) {
        return new float[]{((a2[0] + b[0]) + c[0]) / 3.0f, ((a2[1] + b[1]) + c[1]) / 3.0f, ((a2[2] + b[2]) + c[2]) / 3.0f};
    }

    public static float[] toFloatArray3(List<Float3> array) {
        float[] vs = new float[array.size() * 3];
        int i = 0;
        for (Float3 f : array) {
            vs[i++] = f.x;
            vs[i++] = f.y;
            vs[i++] = f.z;
        }
        return vs;
    }

    public static float[] toFloatArray2(List<Float2> array) {
        float[] vs = new float[array.size() * 2];
        int i = 0;
        for (Float2 f : array) {
            vs[i++] = f.x;
            vs[i++] = f.y;
        }
        return vs;
    }

    public static short[] toShortArray(List<Short> array) {
        short[] vs = new short[array.size()];
        for (int j = 0, l = array.size(); j < l; j++) {
            vs[j] = array.get(j);
        }
        return vs;
    }

    public static float to0_1(float x) {
        return x + 1f / 2f;
    }

    public static void vibrate(Context context, int dur) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            v.vibrate(VibrationEffect.createOneShot(dur, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else
        {
            //deprecated in API 26
            v.vibrate(dur);
        }
    }

    public static void toast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void log(String s) {
        Log.e("Test", s);
    }

    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
