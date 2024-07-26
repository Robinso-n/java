import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ChatClientGUI {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private String clientName;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClientGUI::new);
    }

    public ChatClientGUI() {
        frame = new JFrame("Chat Client");
        textArea = new JTextArea();
        textArea.setEditable(false);
        textField = new JTextField();
        textField.addActionListener(new SendMessageAction());

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.add(textField, BorderLayout.SOUTH);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Get client name
            clientName = JOptionPane.showInputDialog(frame, "Enter your name:");
            out.println(clientName); // Send name to the server

            new Thread(new IncomingReader()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SendMessageAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = textField.getText();
            if (!message.trim().isEmpty()) {
                out.println(clientName + ": " + message);
                textField.setText("");
            }
        }
    }

    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    textArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
