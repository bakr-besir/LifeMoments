package bkr.other;

public class Binary {
    public static short makeShort(byte a, byte b) {
        return (short) ((short) ((b & 0xff) << 8) | (short) (a & 0xff));
    }

    public static byte[] splitToBytes(short s) {
        return new byte[]{(byte) (s & 0x00ff), (byte) ((s & 0xff00) >>> 8)};
    }
}
