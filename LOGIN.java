package seatingarrangement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LOGIN extends JFrame implements ActionListener {

    JTextField adminname;
    JPasswordField password;
    JButton login, cancel;

    LOGIN() {
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        JLabel user = new JLabel("AdminName");
        user.setBounds(40, 20, 100, 30);
        user.setForeground(Color.WHITE);
        add(user);

        JLabel pass = new JLabel("Password");
        pass.setBounds(40, 100, 100, 30);
        pass.setForeground(Color.WHITE);
        add(pass);

        adminname = new JTextField();
        adminname.setBounds(150, 20, 150, 30);
        add(adminname);

        password = new JPasswordField();
        password.setBounds(150, 100, 150, 30);
        add(password);

        login = new JButton("LOGIN");
        login.setBounds(40, 200, 120, 30);
        login.setBackground(Color.WHITE);
        login.setForeground(Color.BLACK);
        login.addActionListener(this);
        add(login);

        cancel = new JButton("CANCEL");
        cancel.setBounds(180, 200, 120, 30);
        cancel.setBackground(Color.WHITE);
        cancel.setForeground(Color.BLACK);
        cancel.addActionListener(this);
        add(cancel);

        // Load and scale image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/second.jpg"));
        Image i2 = i1.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(380, 50, 150, 150);
        add(l3);

        setBounds(500, 200, 600, 300);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String user = adminname.getText();
            String pass = new String(password.getPassword());

            try {
                // Ensure `conn` class is defined elsewhere in your project
                conn c = new conn();
                String query = "SELECT * FROM login WHERE adminname = ? AND password = ?";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, user);
                pstmt.setString(2, pass);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Successful Login!");
                    this.dispose(); // Close current frame
                    new arrangement().setVisible(true); // Ensure `Arrangement` class exists
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Login!");
                    adminname.setText("");
                    password.setText("");
                }

                // Close resources
                rs.close();
                pstmt.close();
                c.c.close();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            }
        } else if (ae.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new LOGIN();
    }
}
