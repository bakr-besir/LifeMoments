package bkr.android.opengl.utils;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class ShaderProgram {
    private final int program, vertexShader, fragmentShader;
    public final String logs;

    public ShaderProgram(String shaderSource) {
        vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, shaderSource.substring(0, shaderSource.indexOf("precision")));
        GLES20.glCompileShader(vertexShader);
        fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, shaderSource.substring(shaderSource.indexOf("precision")));
        GLES20.glCompileShader(fragmentShader);
        logs = GLES20.glGetShaderInfoLog(vertexShader) + GLES20.glGetShaderInfoLog(fragmentShader);
        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    public void start() {
        GLES20.glUseProgram(program);
    }

    public int getProgram() {
        return program;
    }

    public void stop() {
        GLES20.glUseProgram(0);
    }

    public void delete() {
        stop();
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
        GLES20.glDeleteProgram(program);
    }

    public void updateMatrix(int pos, float[] matrix) {
        GLES20.glUniformMatrix4fv(pos, 1, false, matrix, 0);
    }

    public void updateFloat(int pos, float f) {
        GLES20.glUniform1f(pos, f);
    }

    public void updateVec2(int pos, float x, float y) {
        GLES20.glUniform2f(pos, x, y);
    }

    public void updateVec3(int pos, float x, float y, float z) {
        GLES20.glUniform3f(pos, x, y, z);
    }

    public void updateVec4(int pos, float x, float y, float z, float w) {
        GLES20.glUniform4f(pos, x, y, z, w);
    }

    public void updateInt(int pos, int i) {
        GLES20.glUniform1i(pos, i);
    }

    public void updateVec2i(int pos, int x, int y) {
        GLES20.glUniform2i(pos, x, y);
    }

    public void updateVec3i(int pos, int x, int y, int z) {
        GLES20.glUniform3i(pos, x, y, z);
    }

    public void updateVec4i(int pos, int x, int y, int z, int w) {
        GLES20.glUniform4i(pos, x, y, z, w);
    }

    public void updateVertexPointer4(int pos, FloatBuffer buffer) {
        GLES20.glVertexAttribPointer(pos, 4, GLES20.GL_FLOAT, false, 16, buffer);
    }

    public void updateVertexPointer3(int pos, FloatBuffer buffer) {
        GLES20.glVertexAttribPointer(pos, 3, GLES20.GL_FLOAT, false, 12, buffer);
    }

    public void updateVertexPointer2(int pos, FloatBuffer buffer) {
        GLES20.glVertexAttribPointer(pos, 2, GLES20.GL_FLOAT, false, 8, buffer);
    }

    public void disableVertexPointer(int pos) {
        GLES20.glDisableVertexAttribArray(pos);
    }

    public void enableVertexPointer(int pos) {
        GLES20.glEnableVertexAttribArray(pos);
    }

    public void bindTexture(int texture, int unit) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + unit);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
    }

    public void updateSampler2D(int pos, int i) {
        GLES20.glUniform1i(pos, i);
    }
}
