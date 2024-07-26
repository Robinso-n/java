import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client1 {  // Similarly for Client2.java
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);

            // Get client name
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            out.println(name); // Send name to the server

            // Start a new thread for receiving messages
            new Thread(new IncomingReader(in)).start();

            // Read and send messages
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                out.println(name + ": " + message); // Send message with name
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class IncomingReader implements Runnable {
        private BufferedReader in;

        public IncomingReader(BufferedReader in) {
            this.in = in;
        }

        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}