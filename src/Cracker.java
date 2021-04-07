import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

class Cracker implements Runnable {

    private int start;
    private int end;
    private final MessageDigest digest = MessageDigest.getInstance("SHA-256");
    private static boolean Done = false;
    private String found;

    public Cracker(int s, int e) throws NoSuchAlgorithmException {
        start = s;
        end = e;
    }

    public void generate(StringBuilder sb, int n) {
        if (Done)
            return;

        if (n == sb.length()) {
            String candidate = sb.toString();
            byte[] bytes = digest.digest(candidate.getBytes());
            if (Arrays.equals(bytes, Main.Byte_Password)) {
                found = candidate;
                Done = true;
            }
            return;
        }
        for (int i = 0; i < Main.Aphabet.length && !Done; i++) {
            char letter = Main.Aphabet[i];
            sb.setCharAt(n, letter);
            generate(sb, n + 1);
        }
    }

    @Override
    public void run() {

        for (int length = start; length <= end && !Done; length++) {
            StringBuilder sb = new StringBuilder();
            sb.setLength(length);
            generate(sb, 0);
        }

        if (Done) {
            long duration = System.currentTimeMillis() - Main.Start_time;
            System.out.println("Password cracked in " + TimeUnit.MILLISECONDS.toSeconds(duration) + "." + TimeUnit.MILLISECONDS.toMillis(duration) + " sec. Password was = " + found);
        } else {
            long PerLength = System.currentTimeMillis() - Main.Start_time;
            System.out.println("Time elapsed: " + TimeUnit.MILLISECONDS.toSeconds(PerLength) + "." + TimeUnit.MILLISECONDS.toMillis(PerLength) + " sec. Password not cracked for subset [" + start + ", " + end + "]");
        }
    }

}
