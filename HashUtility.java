package blockchain;

import java.security.MessageDigest;
import java.util.Random;

public class HashUtility {

    public static long generateMagicNumber(String zeros) {
        String hash;
        long magicNumber = System.currentTimeMillis();

        do {
            magicNumber = Math.abs(magicNumber / 3 + (long)
                    (new Random().nextInt() * Math.random()));
            hash = HashUtility.applySha256(String.valueOf(magicNumber));
        } while (!hash.startsWith(zeros));

        return magicNumber;
    }

    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            StringBuilder hexString = new StringBuilder();
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
