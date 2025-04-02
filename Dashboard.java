package seatingarrangement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener {
    JButton adminBtn, studentBtn;

    Dashboard() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen mode
        setLayout(new BorderLayout());

        // Load Background Image
        ImageIcon bgIcon = new ImageIcon(ClassLoader.getSystemResource("icon/fourth.jpg"));
        Image bgImage = bgIcon.getImage();

        // Panel with Scalable Background
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(new GridBagLayout()); // Center everything
        setContentPane(bgPanel);

        // Layout Constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Heading Label - Centered & Bold
        JLabel heading = new JLabel("Choose Your Role:");
        heading.setFont(new Font("Serif", Font.BOLD, 50)); // Bigger & Bold
        heading.setForeground(Color.BLACK);
        bgPanel.add(heading, gbc);

        // Buttons Panel (For proper spacing)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 50, 0));
        buttonPanel.setOpaque(false); // Transparent panel

        // Admin Button
        adminBtn = new JButton("Admin");
        adminBtn.setFont(new Font("Arial", Font.BOLD, 20));
        adminBtn.setBackground(Color.WHITE);
        adminBtn.setForeground(Color.BLACK);
        adminBtn.setFocusPainted(false);
        adminBtn.setPreferredSize(new Dimension(180, 60));
        adminBtn.addActionListener(this);
        buttonPanel.add(adminBtn);

        // Student Button
        studentBtn = new JButton("Student");
        studentBtn.setFont(new Font("Arial", Font.BOLD, 20));
        studentBtn.setBackground(Color.WHITE);
        studentBtn.setForeground(Color.BLACK);
        studentBtn.setFocusPainted(false);
        studentBtn.setPreferredSize(new Dimension(180, 60));
        studentBtn.addActionListener(this);
        buttonPanel.add(studentBtn);

        // Place Buttons Below Heading
        gbc.gridy = 1;
        bgPanel.add(buttonPanel, gbc);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == adminBtn) {
            new LOGIN();  
            this.dispose();  // Close Dashboard
        } else if (ae.getSource() == studentBtn) {
            new StLogin();  // Open Student Login
            this.dispose();  // Close Dashboard
        }
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}
