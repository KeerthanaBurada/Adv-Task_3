import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Chat server is running...");
        ServerSocket serverSocket = new ServerSocket(12345);

        while (true) {
            Socket socket = serverSocket.accept();
            new ClientHandler(socket).start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                writer = new PrintWriter(socket.getOutputStream(), true);
                clientWriters.add(writer);

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Message: " + message);
                    for (PrintWriter w : clientWriters) {
                        w.println(message);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Couldn't close socket: " + e.getMessage());
                }
                clientWriters.remove(writer);
            }
        }
    }
}
