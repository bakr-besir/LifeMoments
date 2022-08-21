package bkr.other;

import java.util.Random;

public class Names {
    public static String createRandomReadableName(int length, int randomRange) {
        String x = "the at there some my of be use her than and this an would first a have each to from which been in or she him call is one do into who you had how time that by their has its it word if look now he but will two find was not up more long for what other write down on all about go day are were out see did as we many number get with when then no come his your them way made they can these could may I said so people part";

        Random r = new Random();

        int l = r.nextInt(randomRange) - randomRange / 2 + length;
        StringBuilder name = new StringBuilder();
        int qt = 0;
        int ot = 0;

        for (int i = 0; i < l; i++) {

            boolean q = r.nextBoolean();

            String lit = q ? String.valueOf("bcdfghjklmnpqrstvxzyw".charAt(r.nextInt(21))) : String.valueOf("aeiou".charAt(r.nextInt(5)));
            if (i == 0) lit = lit.toUpperCase();

            if (lit.equalsIgnoreCase("Q")) lit += "u";

            if (q) {
                qt++;
                ot = 0;
            } else {
                qt = 0;
                ot++;
            }


            if (qt < 2 && ot < 2) name.append(lit);
            else i--;

            if (i + 1 == l) if (x.contains(name.toString().toLowerCase())) i = 0;
        }

        return name.toString();
    }
}
