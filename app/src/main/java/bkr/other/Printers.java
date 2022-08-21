package bkr.other;

public class Printers {
    public static String printArray1D(byte[] array, int width) {
        StringBuilder s = new StringBuilder();
        int x = 1;
        for (byte b : array) {
            s.append(b).append(" ");
            if (x == width) {
                s.append("\n");
                x = 0;
            }
            x++;
        }
        return s.toString();
    }

    public static String printArray1D(int[] array, int width) {
        StringBuilder s = new StringBuilder();
        int x = 1;
        for (int j : array) {
            s.append(j).append(" ");
            if (x == width) {
                s.append("\n");
                x = 0;
            }
            x++;
        }
        return s.toString();
    }

    public static String printArray1D(float[] array, int width) {
        StringBuilder s = new StringBuilder();
        int x = 1;
        for (float j : array) {
            s.append(j).append(" ");
            if (x == width) {
                s.append("\n");
                x = 0;
            }
            x++;
        }
        return s.toString();
    }

    public static String printArray1D(double[] array, int width) {
        StringBuilder s = new StringBuilder();
        int x = 1;
        for (double j : array) {
            s.append(j).append(" ");
            if (x == width) {
                s.append("\n");
                x = 0;
            }
            x++;
        }
        return s.toString();
    }

    public static String printArray1D(short[] array, int width) {
        StringBuilder s = new StringBuilder();
        int x = 1;
        for (short value : array) {
            s.append(value).append(" ");
            if (x == width) {
                s.append("\n");
                x = 0;
            }
            x++;
        }
        return s.toString();
    }

    public static String printArray1D(long[] array, int width) {
        StringBuilder s = new StringBuilder();
        int x = 1;
        for (long l : array) {
            s.append(l).append(" ");
            if (x == width) {
                s.append("\n");
                x = 0;
            }
            x++;
        }
        return s.toString();
    }

    public static String printArray1D(String[] array, int width) {
        StringBuilder s = new StringBuilder();
        int x = 1;
        for (String value : array) {
            s.append(value).append(" ");
            if (x == width) {
                s.append("\n");
                x = 0;
            }
            x++;
        }
        return s.toString();
    }

    public static String printArray1D(Object[] array, int width) {
        StringBuilder s = new StringBuilder();
        int x = 1;
        for (Object o : array) {
            s.append(o).append(" ");
            if (x == width) {
                s.append("\n");
                x = 0;
            }
            x++;
        }
        return s.toString();
    }
}
