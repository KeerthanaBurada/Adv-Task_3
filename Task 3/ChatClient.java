import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("Connected to chat server.");
        new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.err.println("Error reading messages: " + e.getMessage());
            }
        }).start();

        while (true) {
            String message = input.readLine();
            writer.println(message);
        }
    }
}
