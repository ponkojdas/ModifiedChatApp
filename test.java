import java.io.*;
import java.net.*;

public class test {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("10.100.94.192", 1234);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            
            new Thread(() -> {
                String msg;
                try {
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            
            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
