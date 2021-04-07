import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static char[] Aphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[];./!@".toCharArray();
    public static int Max_Length = 0;
    public static final String Password = null;
    public static byte[] Byte_Password;
    public static long Start_time;
    public static void setByte_Password(String pass){
        Byte_Password = stringToByteArray(pass);
    }public static void setMax_Length(int length){
        Max_Length = length;
    }
    public static byte[] stringToByteArray(String s) {
        int length = s.length();
        byte[] data = new byte[length/2];
        for(int i = 0; i < length; i+=2) {
            data[i/2] = (byte)((Character.digit(s.charAt(i),  16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        int cores = Runtime.getRuntime().availableProcessors();
        int lengthSet = Max_Length / cores;
        int end = 0;
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        System.out.println("Insert the SHA-256 code");
        String h_password = reader.readLine();
        setByte_Password(h_password);
        System.out.println("Insert lenght of password.");
        Scanner in = new Scanner(System.in);
        int length = in.nextInt();
        setMax_Length(length);
        System.out.println("Multi-thread?? (1 - yes,0 - no)");
        int answer = in.nextInt();

        Start_time = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        //System.out.println("Crack hash...");
        if(answer == 0) { // apelare algoritm cracker pentru un singur thread
            while (end < Max_Length) {
                int start = end + 1;
                end = start + lengthSet;
                if (end > Max_Length) end = Max_Length;
                new Cracker(start, end).run();
            }
            System.out.println("Test made without threads.");
        }else { // apelare algoritm cracker pentru mai multe threads
        System.out.println("Crack password with all procesor cores x.X");
        System.out.println("You have " + cores + " cores.");
        System.out.println("...");
        while(end<Max_Length) {
            int start = end + 1;
            end = start + lengthSet;
            if(end > Max_Length) end = Max_Length;
            executorService.submit(new Cracker(start, end));
        }
        System.out.println("Test made with all processor cores.");

            executorService.shutdown();
        }
    }
}
