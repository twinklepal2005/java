package seatingarrangement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6789;
    private static JPanel chatPanel;
    private static JScrollPane scrollPane;
    private static JTextField inputField;
    private static JButton sendButton;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String studentName;
    private static Socket socket;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClient::createGUI);

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            studentName = JOptionPane.showInputDialog("Enter your name:");
            if (studentName == null || studentName.trim().isEmpty()) {
                closeConnection();
                System.exit(0);
            }

            out.println(studentName);
            String serverResponse = in.readLine();
            if ("ACCEPTED".equals(serverResponse)) {
                appendMessage("Connected to admin. Start chatting...", new Color(96, 75, 98), false);
                sendButton.setEnabled(true);
                new Thread(ChatClient::listenForMessages).start();
            } else {
                appendMessage("Chat request denied by admin.", new Color(96,75,98 ), false);
                closeConnection();
                System.exit(0);
            }
        } catch (IOException e) {
            appendMessage("Error connecting to server.", new Color(96,75,98 ), false);
        }
    }

    private static void createGUI() {
        JFrame frame = new JFrame("Student Chat Client");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(173, 216, 230));

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(250, 249, 246));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        sendButton.addActionListener(e -> sendMessage());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty() && out != null) {
            appendMessage(message, new Color(160, 170, 150), true);
            out.println(message);
            inputField.setText("");
        }
    }

    private static void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                final String receivedMessage = message.replaceFirst("^Admin:\\s*", "");
                SwingUtilities.invokeLater(() -> appendMessage(receivedMessage, new Color(2, 64, 130), false));
            }
        } catch (IOException e) {
            appendMessage("Chat ended.", new Color(96,75,98 ), false);
        } finally {
            closeConnection();
        }
    }

    private static void appendMessage(String message, Color color, boolean isUser) {
        SwingUtilities.invokeLater(() -> {
            JPanel messageWrapper = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT));
            messageWrapper.setOpaque(false);

            JLabel messageLabel = new JLabel("<html><div style='padding:8px; max-width:300px;'>" + message + "</div></html>");
            messageLabel.setOpaque(true);
            messageLabel.setBackground(color);
            messageLabel.setForeground(Color.WHITE);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            messageWrapper.add(messageLabel);
            chatPanel.add(messageWrapper);
            chatPanel.revalidate();
            chatPanel.repaint();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    private static void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException ignored) {}
    }
}
