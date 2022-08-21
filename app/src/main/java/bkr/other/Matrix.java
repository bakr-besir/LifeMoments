package bkr.other;

public class Matrix {

    public static float[] mul(float[] r, float[] l) {
        float[] res = new float[16];
        res[0] = r[0] * l[0] + r[4] * l[1] + r[8] * l[2] + r[12] * l[3];
        res[4] = r[0] * l[4] + r[4] * l[5] + r[8] * l[6] + r[12] * l[7];
        res[8] = r[0] * l[8] + r[4] * l[9] + r[8] * l[10] + r[12] * l[11];
        res[12] = r[0] * l[12] + r[4] * l[13] + r[8] * l[14] + r[12] * l[15];
        res[1] = r[1] * l[0] + r[5] * l[1] + r[9] * l[2] + r[13] * l[3];
        res[5] = r[1] * l[4] + r[5] * l[5] + r[9] * l[6] + r[13] * l[7];
        res[9] = r[1] * l[8] + r[5] * l[9] + r[9] * l[10] + r[13] * l[11];
        res[13] = r[1] * l[12] + r[5] * l[13] + r[9] * l[14] + r[13] * l[15];
        res[2] = r[2] * l[0] + r[6] * l[1] + r[10] * l[2] + r[14] * l[3];
        res[6] = r[2] * l[4] + r[6] * l[5] + r[10] * l[6] + r[14] * l[7];
        res[10] = r[2] * l[8] + r[6] * l[9] + r[10] * l[10] + r[14] * l[11];
        res[14] = r[2] * l[12] + r[6] * l[13] + r[10] * l[14] + r[14] * l[15];
        res[3] = r[3] * l[0] + r[7] * l[1] + r[11] * l[2] + r[15] * l[3];
        res[7] = r[3] * l[4] + r[7] * l[5] + r[11] * l[6] + r[15] * l[7];
        res[11] = r[3] * l[8] + r[7] * l[9] + r[11] * l[10] + r[15] * l[11];
        res[15] = r[3] * l[12] + r[7] * l[13] + r[11] * l[14] + r[15] * l[15];
        return res;
    }

    public static float[] mul_vec(float[] m, float[] v) {
        float[] res = new float[4];
        res[0] = m[0] * v[0] + m[4] * v[1] + m[8] * v[2] + m[12] * v[3];
        res[1] = m[1] * v[0] + m[5] * v[1] + m[9] * v[2] + m[13] * v[3];
        res[2] = m[2] * v[0] + m[6] * v[1] + m[10] * v[2] + m[14] * v[3];
        res[3] = m[3] * v[0] + m[7] * v[1] + m[11] * v[2] + m[15] * v[3];
        return res;
    }

    public static float[] flipMatrix(float[] m) {
        return new float[]{m[0], m[4], m[8], m[12], m[1], m[5], m[9], m[13], m[2], m[6], m[10], m[14], m[3], m[7], m[11], m[15],};
    }

}
