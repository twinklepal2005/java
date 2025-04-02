package seatingarrangement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StLogin extends JFrame implements ActionListener {

    JTextField studentName;
    JPasswordField password;
    JButton login, register;
    JLabel adminLink;

    StLogin() {
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        // Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/second.jpg"));
        Image i2 = i1.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(225, 10, 150, 150); // Centered at top
        add(l3);

        // Student Login Label
        JLabel title = new JLabel("STUDENT LOGIN");
        title.setBounds(220, 160, 200, 30);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("serif", Font.BOLD, 18));
        add(title);

        // Username Label
        JLabel user = new JLabel("Username");
        user.setBounds(120, 200, 100, 30);
        user.setForeground(Color.WHITE);
        add(user);

        // Password Label
        JLabel pass = new JLabel("Password");
        pass.setBounds(120, 250, 100, 30);
        pass.setForeground(Color.WHITE);
        add(pass);

        // Username Field
        studentName = new JTextField();
        studentName.setBounds(220, 200, 200, 30);
        add(studentName);

        // Password Field
        password = new JPasswordField();
        password.setBounds(220, 250, 200, 30);
        add(password);

        // Login Button
        login = new JButton("LOGIN");
        login.setBounds(220, 300, 90, 30);
        login.setBackground(Color.WHITE);
        login.setForeground(Color.BLACK);
        login.addActionListener(this);
        add(login);

        // Register Button
        register = new JButton("REGISTER");
        register.setBounds(330, 300, 90, 30);
        register.setBackground(Color.WHITE);
        register.setForeground(Color.BLACK);
        register.addActionListener(this);
        add(register);

        // Admin Login Link
        adminLink = new JLabel("Admin?");
        adminLink.setBounds(290, 350, 100, 30);
        adminLink.setForeground(new Color(245, 245, 220)); // Beige color

        adminLink.setFont(new Font("serif", Font.BOLD, 14));
        adminLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                new LOGIN(); // Open admin login
            }
        });
        add(adminLink);

        setBounds(500, 100, 600, 450);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String user = studentName.getText();
            String pass = new String(password.getPassword());
            
            try {
                conn c = new conn();
                String query = "SELECT * FROM streg WHERE username = ? AND password = ?";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, user);
                pstmt.setString(2, pass);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Login Successful");
                    new HallTicketViewer().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Credentials");
                    studentName.setText("");
                    password.setText("");
                }

                rs.close();
                pstmt.close();
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == register) {
            setVisible(false);
            new Register(); // Assuming Register class exists for student registration
        }
    }

    public static void main(String[] args) {
        new StLogin();
    }
}
