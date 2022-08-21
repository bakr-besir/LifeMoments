package bkr.other.opengl.utils;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import android.opengl.Matrix;

public class Camera {
    private final float[] pos;
    private final float[] projection = new float[16];
    private final float[] view = new float[16];
    private final float[] front = new float[3];
    private final float[] right = new float[3];
    public float yaw = 270, pitch;
    private byte dirty; // 0.No  1.Pos  2.Rot

    public Camera(float x, float y, float z) {
        pos = new float[]{x, y, z};
        updateVectors();
        updateViewMatrix();
    }

    public float[] getView() {
        if (dirty != 0) {
            if (dirty == 2) {
                updateVectors();
            }
            updateViewMatrix();
            dirty = 0;
        }
        return view;
    }

    public float[] getProjection() {
        return projection;
    }

    public void setProjection(float fov, int width, int height, float near, float far) {
        Matrix.perspectiveM(projection, 0, fov, (float) (width / height), near, far);
    }

    private void updateVectors() {
        front[0] = (float) (cos(toRadians(yaw)) * cos(toRadians(pitch)));
        front[1] = (float) (sin(toRadians(pitch)));
        front[2] = (float) (sin(toRadians(yaw)) * cos(toRadians(pitch)));
        normalize(front);
        cross(right, front, new float[]{0, 1, 0});
        normalize(right);
    }

    public static void normalize(float[] v) {
        float length = (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        v[0] /= length;
        v[1] /= length;
        v[2] /= length;
    }

    public static void cross(float[] src, float[] a, float[] b) {
        src[0] = a[1] * b[2] - a[2] * b[1];
        src[1] = a[2] * b[0] - a[0] * b[2];
        src[2] = a[0] * b[1] - a[1] * b[0];
    }

    private void updateViewMatrix() {
        Matrix.setLookAtM(view, 0, pos[0], pos[1], pos[2], pos[0] + front[0], pos[1] + front[1], pos[2] + front[2], 0, 1, 0);
    }

    public void rotateY(float a) {
        yaw = a;
        dirty = 2;
    }

    public void rotateX(float a) {
        pitch = a;
        dirty = 2;
    }

    public void move(float x, float y, float z) {
        pos[0] = x;
        pos[1] = y;
        pos[2] = z;
        dirty = 1;
    }

    public void forward(float a) {
        pos[0] += a * front[0];
        pos[1] += a * front[1];
        pos[2] += a * front[2];
        if (dirty == 0) dirty = 1;
    }

    public void back(float a) {
        pos[0] += a * -front[0];
        pos[1] += a * -front[1];
        pos[2] += a * -front[2];
        if (dirty == 0) dirty = 1;
    }

    public void right(float a) {
        pos[0] += a * right[0];
        pos[1] += a * right[1];
        pos[2] += a * right[2];
        if (dirty == 0) dirty = 1;
    }

    public void left(float a) {
        pos[0] += a * -right[0];
        pos[1] += a * -right[1];
        pos[2] += a * -right[2];
        if (dirty == 0) dirty = 1;
    }

    public float getX() {
        return pos[0];
    }

    public float getY() {
        return pos[1];
    }

    public float getZ() {
        return pos[2];
    }
}