package seatingarrangement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

class Student {
    String regNo, name, course, subject, timing, semester, date, classroom;
    int seatNo;

    Student(String regNo, String name, String course, String subject, String timing, String semester, String date) {
        this.regNo = regNo;
        this.name = name;
        this.course = course;
        this.subject = subject;
        this.timing = timing;
        this.semester = semester;
        this.date = date;
    }
}

// Panel with dynamic background
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
            if (backgroundImage == null) throw new IOException("Image not found");
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("Background Image Not Found", getWidth() / 2 - 80, getHeight() / 2);
        }
    }
}

public class arrangement extends JFrame {
    private List<Student> students = new ArrayList<>();
    private JTable table;
    private DefaultTableModel tableModel;

    public arrangement() {
        setTitle("Seating Arrangement System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\NetBeansProjects\\seatingarrangement\\src\\icon\\fifth.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        JButton loadButton = new JButton("Load CSV");
        JButton shuffleButton = new JButton("Shuffle & Assign Seats");
        JButton serverButton = new JButton("Server");
        buttonPanel.add(loadButton);
        buttonPanel.add(shuffleButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(serverButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Reg No", "Name", "Course", "Subject", "Timing", "Semester", "Date", "Classroom", "Seat No"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Actions
        loadButton.addActionListener(e -> loadCSV());
        shuffleButton.addActionListener(e -> {
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please load a CSV first!");
            } else {
                assignSeats();
                displayTable();
                saveShuffledCSV();
            }
        });
        
        serverButton.addActionListener(e -> openChatServer());
    }

    private void loadCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            students.clear();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                br.readLine(); // Skip header
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 7) {
                        students.add(new Student(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(),
                                data[4].trim(), data[5].trim(), data[6].trim()));
                    }
                }
                JOptionPane.showMessageDialog(this, "CSV Loaded Successfully! " + students.size() + " students found.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading CSV file.");
            }
        }
    }

    private void assignSeats() {
        Random random = new Random();
        Map<String, Set<Integer>> classroomSeats = new HashMap<>();
        Map<String, List<Student>> groupMap = new HashMap<>();

        for (Student s : students) {
            String key = s.course + "_" + s.semester + "_" + s.subject;
            groupMap.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        int totalClassrooms = 50;
        List<String> classrooms = new ArrayList<>();
        for (int i = 1; i <= totalClassrooms; i++) {
            classrooms.add(String.format("%03d", i));
            classroomSeats.put(classrooms.get(i - 1), new HashSet<>());
        }

        int classroomIndex = 0;
        for (List<Student> group : groupMap.values()) {
            Collections.shuffle(group);
            for (Student s : group) {
                boolean assigned = false;
                while (!assigned) {
                    String classroom = classrooms.get(classroomIndex % totalClassrooms);
                    Set<Integer> assignedSeats = classroomSeats.get(classroom);
                    int seatNo;
                    do {
                        seatNo = random.nextInt(36) + 1;
                    } while (assignedSeats.contains(seatNo));
                    s.classroom = classroom;
                    s.seatNo = seatNo;
                    assignedSeats.add(seatNo);
                    classroomIndex++;
                    assigned = true;
                }
            }
        }
    }

    private void displayTable() {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.regNo, s.name, s.course, s.subject, s.timing, s.semester, s.date, s.classroom, s.seatNo});
        }
    }

    private void saveShuffledCSV() {
        try (PrintWriter pw = new PrintWriter("shuffled.csv")) {
            pw.println("Reg No,Name,Course,Subject,Timing,Semester,Date,Classroom,Seat No");
            for (Student s : students) {
                pw.printf("%s,%s,%s,%s,%s,%s,%s,%s,%d%n", s.regNo, s.name, s.course, s.subject, s.timing, s.semester, s.date, s.classroom, s.seatNo);
            }
            JOptionPane.showMessageDialog(this, "Shuffled file saved as shuffled.csv!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving shuffled file!");
        }
    }
    
    private void openChatServer() {
    try {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp",
    "C:\\NetBeansProjects\\seatingarrangement\\build\\classes", "seatingarrangement.ChatServer");

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line); // Debugging output
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error opening ChatServer: " + e.getMessage());
    }
}




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            arrangement gui = new arrangement();
            gui.setVisible(true);
        });
    }
}
