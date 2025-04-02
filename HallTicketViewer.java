package seatingarrangement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.Desktop;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Image;


public class HallTicketViewer extends JFrame {
    private List<Student> students = new ArrayList<>();
    private JTextField regNoField;
    private JTextField nameField;
    private JTextArea resultArea;
    private JButton downloadButton;
    private JButton talktoadminButton;
    private Student foundStudent;
    private Image backgroundImage;

    public HallTicketViewer() {
        setTitle("Hall Ticket Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource("/icon/sixth.jpg")).getImage();

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
       

        JLabel regNoLabel = new JLabel("Registration Number:");
regNoLabel.setForeground(Color.WHITE);
regNoLabel.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 20));

inputPanel.add(regNoLabel);

        regNoField = new JTextField();
        
        inputPanel.add(regNoField);

        JLabel regLabel = new JLabel("Name:");
regLabel.setForeground(Color.WHITE);
regLabel.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 20));

inputPanel.add(regLabel);
        nameField = new JTextField();
        inputPanel.add(nameField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setOpaque(false);
        
        JButton searchButton = new JButton("Search Hall Ticket");
         searchButton.setBackground(Color.WHITE);
        searchButton.setForeground(Color.BLACK);
        talktoadminButton = new JButton("Talk to Admin");
        talktoadminButton.setBackground(Color.WHITE);
        talktoadminButton.setForeground(Color.BLACK);

        buttonPanel.add(searchButton);
        buttonPanel.add(talktoadminButton);

        inputPanel.add(buttonPanel);
        backgroundPanel.add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        downloadButton = new JButton("Download as PDF");
        downloadButton.setEnabled(false);
        backgroundPanel.add(downloadButton, BorderLayout.SOUTH);

        loadCSV();

        searchButton.addActionListener(e -> searchStudent());
        talktoadminButton.addActionListener(e -> talkToAdmin());
        downloadButton.addActionListener(e -> generatePDF());
    }

    private void loadCSV() {
        File file = new File("shuffled.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "shuffled.csv file not found!");
            return;
        }
        students.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9) {
                    Student student = new Student(
                        data[0].trim(), data[1].trim(), data[2].trim(),
                        data[3].trim(), data[4].trim(), data[5].trim(), data[6].trim()
                    );
                    student.classroom = data[7].trim();
                    student.seatNo = Integer.parseInt(data[8].trim());
                    students.add(student);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading shuffled.csv file.");
        }
    }

    private void searchStudent() {
        String regNo = regNoField.getText().trim();
        String name = nameField.getText().trim();
        if (regNo.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Registration Number and Name.");
            return;
        }
        foundStudent = null;
        for (Student s : students) {
            if (s.regNo.equalsIgnoreCase(regNo) && s.name.equalsIgnoreCase(name)) {
                foundStudent = s;
                StringBuilder sb = new StringBuilder();
                sb.append("----- HALL TICKET -----\n");
                sb.append("Name: ").append(s.name).append("\n");
                sb.append("Reg No: ").append(s.regNo).append("\n");
                sb.append("Course: ").append(s.course).append("\n");
                sb.append("Subject: ").append(s.subject).append("\n");
                sb.append("Semester: ").append(s.semester).append("\n");
                sb.append("Timing: ").append(s.timing).append("\n");
                sb.append("Date: ").append(s.date).append("\n");
                sb.append("Classroom: ").append(s.classroom).append("\n");
                sb.append("Seat No: ").append(s.seatNo).append("\n");
                resultArea.setText(sb.toString());
                downloadButton.setEnabled(true);
                return;
            }
        }
        resultArea.setText("No student found with the given Reg No and Name.");
        downloadButton.setEnabled(false);
    }

    private void generatePDF() {
        if (foundStudent == null) {
            JOptionPane.showMessageDialog(this, "No hall ticket found to generate PDF.");
            return;
        }
        try {
            Document document = new Document();
            String fileName = "HallTicket_" + foundStudent.regNo + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            document.add(new Paragraph("HALL TICKET\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph("Name: " + foundStudent.name));
            document.add(new Paragraph("Reg No: " + foundStudent.regNo));
            document.add(new Paragraph("Course: " + foundStudent.course));
            document.add(new Paragraph("Subject: " + foundStudent.subject));
            document.add(new Paragraph("Semester: " + foundStudent.semester));
            document.add(new Paragraph("Timing: " + foundStudent.timing));
            document.add(new Paragraph("Date: " + foundStudent.date));
            document.add(new Paragraph("Classroom: " + foundStudent.classroom));
            document.add(new Paragraph("Seat No: " + foundStudent.seatNo));
            document.close();
            JOptionPane.showMessageDialog(this, "PDF saved as " + fileName);
            Desktop.getDesktop().open(new File(fileName));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating PDF.");
        }
    }

    private void talkToAdmin() {
        String regNo = regNoField.getText().trim();
        String name = nameField.getText().trim();
        if (regNo.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Registration Number and Name before contacting the admin.");
            return;
        }
        
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp",
                "C:\\NetBeansProjects\\seatingarrangement\\build\\classes", "seatingarrangement.ChatClient");
            processBuilder.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error opening chat client.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HallTicketViewer viewer = new HallTicketViewer();
            viewer.setVisible(true);
        });
    }
}
