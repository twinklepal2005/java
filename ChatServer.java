package seatingarrangement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private static final int PORT = 6789;
    private static final Map<String, ClientHandler> clients = new HashMap<>();
    private static JPanel chatPanel;
    private static JScrollPane scrollPane;
    private static JButton sendButton;
    private static JTextField inputField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatServer::createGUI);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (chatPanel == null) {
                Thread.sleep(100);
            }
            appendMessage("Admin is online. Waiting for students...",  new Color(96,75,98 ), true);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException | InterruptedException e) {
            appendMessage("Server error: " + e.getMessage(),new Color(96,75,98 ) , false); // Muted Nude Green (#AABE9F)
 
           
        }
    }

    private static void createGUI() {
        JFrame frame = new JFrame("Admin Chat Server");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(173, 216, 230));

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(250, 249 ,246)  );

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        sendButton.addActionListener(e -> sendMessageToAll());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void sendMessageToAll() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            appendMessage(message, new Color(160,170,150)  // Soft Nude Green (#BDD0BA)
, true);
            for (ClientHandler client : clients.values()) {
                client.sendMessage("Admin: " + message);
            }
            inputField.setText("");
        }
    }

    private static void appendMessage(String message, Color color, boolean isAdmin) {
        SwingUtilities.invokeLater(() -> {
            JPanel messageWrapper = new JPanel(new FlowLayout(isAdmin ? FlowLayout.RIGHT : FlowLayout.LEFT));
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

    static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String studentName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                studentName = in.readLine();

                if (studentName == null || studentName.trim().isEmpty()) {
                    closeConnection();
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    int response = JOptionPane.showConfirmDialog(null, "Accept chat from " + studentName + "?", "Chat Request", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        out.println("ACCEPTED");
                        appendMessage(studentName + " has joined the chat", new Color(96,75,98 ), false);
                        sendButton.setEnabled(true);
                        clients.put(studentName, this);
                        new Thread(this::listenForMessages).start();
                    } else {
                        out.println("DENIED");
                        closeConnection();
                    }
                });
            } catch (IOException e) {
                appendMessage("Connection lost with " + studentName, Color.RED, false);
                closeConnection();
            }
        }

        private void listenForMessages() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    appendMessage( message, new Color(2,64,130), false);
                }
            } catch (IOException e) {
                appendMessage(studentName + " has left the chat", new Color(96,75,98 ), false);
            } finally {
                closeConnection();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        private void closeConnection() {
            clients.remove(studentName);
            try {
                if (socket != null) socket.close();
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException ignored) {}
        }
    }
}