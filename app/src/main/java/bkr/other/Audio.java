package bkr.other;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static bkr.other.Binary.makeShort;
import static bkr.other.Binary.splitToBytes;

public class Audio {
    public static float note_freq(int i) {
        return (float) (440 * pow(1.059463096, i));
    }

    /**
     * @param a must be larger than b
     * @param b must be smaller or equal to a
     * @return mixed audio of a and b
     */
    public static byte[] addAudio(byte[] a, byte[] b) {
        byte[] res = new byte[a.length];

        for (int i = 0; i < a.length; i += 2) {
            if (i < b.length) {
                short s1 = makeShort(a[i], a[i + 1]);
                short s2 = makeShort(b[i], b[i + 1]);

                byte[] bytes = splitToBytes((short) (s1 + s2));

                if (s1 + s2 >= Short.MAX_VALUE) {
                    bytes = splitToBytes(Short.MAX_VALUE);
                } else if (s1 + s2 <= Short.MIN_VALUE) {
                    bytes = splitToBytes(Short.MIN_VALUE);
                }

                res[i] = bytes[0];
                res[i + 1] = bytes[1];
            }
        }
        return res;
    }

    static void addAudio(byte[] res, byte[] sound, int pos) {
        int p = (int) (pos / 1000f * 44100) * 2;
        int x = 0;
        while (p < res.length) {
            if (x + 1 < sound.length) {
                short s1 = makeShort(res[p], res[p + 1]);
                short s2 = makeShort(sound[x], sound[x + 1]);

                byte[] bytes = splitToBytes((short) (s1 + s2));

                if (s1 + s2 >= Short.MAX_VALUE) {
                    bytes = splitToBytes(Short.MAX_VALUE);
                } else if (s1 + s2 <= Short.MIN_VALUE) {
                    bytes = splitToBytes(Short.MIN_VALUE);
                }

                res[p] = bytes[0];
                res[p + 1] = bytes[1];
            }

            p += 2;
            x += 2;
        }
    }

    public static byte[] generateTone(int duration, float freq, float amp, int sampleRate) {
        final float p = sampleRate / freq;
        int count = (int) (duration / 1000f * sampleRate);

        byte[] data = new byte[count * 2];
        int x = 0;
        for (int i = 0; i < count; i++) {
            short s = (short) (sin(PI * 2 * i / p) * Short.MAX_VALUE * amp);
            data[x++] = (byte) (s & 0x00ff);
            data[x++] = (byte) ((s & 0xff00) >>> 8);
        }
        return data;
    }

    public static byte[] generateSilent(int duration, int sampleRate, int channels) {
        return new byte[duration / 1000 * sampleRate * 2 * channels];
    }
}
