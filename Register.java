package seatingarrangement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Register extends JFrame implements ActionListener {

    JTextField username;
    JPasswordField password, confirmPassword;
    JButton register, cancel;
    JLabel backToLogin;

    Register() {
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        JLabel title = new JLabel("REGISTER");
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(200, 20, 200, 30);
        add(title);

        JLabel user = new JLabel("Username");
        user.setBounds(80, 80, 120, 30);
        user.setFont(new Font("Tahoma", Font.PLAIN, 16));
        user.setForeground(Color.WHITE);
        add(user);

        JLabel pass = new JLabel("Password");
        pass.setBounds(80, 140, 120, 30);
        pass.setFont(new Font("Tahoma", Font.PLAIN, 16));
        pass.setForeground(Color.WHITE);
        add(pass);

        JLabel confirmPass = new JLabel("Confirm Password");
        confirmPass.setBounds(80, 200, 150, 30);
        confirmPass.setFont(new Font("Tahoma", Font.PLAIN, 16));
        confirmPass.setForeground(Color.WHITE);
        add(confirmPass);

        username = new JTextField();
        username.setBounds(250, 80, 180, 30);
        add(username);

        password = new JPasswordField();
        password.setBounds(250, 140, 180, 30);
        add(password);

        confirmPassword = new JPasswordField();
        confirmPassword.setBounds(250, 200, 180, 30);
        add(confirmPassword);

        register = new JButton("REGISTER");
        register.setBounds(100, 280, 140, 40);
        register.setBackground(Color.WHITE);
        register.setForeground(Color.BLACK);
        register.setFont(new Font("Tahoma", Font.BOLD, 14));
        register.addActionListener(this);
        add(register);

        cancel = new JButton("CANCEL");
        cancel.setBounds(270, 280, 140, 40);
        cancel.setBackground(Color.WHITE);
        cancel.setForeground(Color.BLACK);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 14));
        cancel.addActionListener(this);
        add(cancel);

        backToLogin = new JLabel("Back to Login?");
        backToLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
        backToLogin.setForeground(Color.CYAN);
        backToLogin.setBounds(200, 340, 150, 30);
        backToLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backToLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                new StLogin();
            }
        });
        add(backToLogin);

        setBounds(500, 150, 500, 450); // Increased width & height
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == register) {
            String user = username.getText();
            String pass = new String(password.getPassword());
            String confirmPass = new String(confirmPassword.getPassword());

            if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required");
                return;
            }

            if (!pass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match");
                return;
            }

            try {
                conn c = new conn();
                String query = "INSERT INTO streg (username, password) VALUES (?, ?)";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, user);
                pstmt.setString(2, pass);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Registration Successful");
                setVisible(false);
                new StLogin();

                pstmt.close();
                c.close();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Username already exists");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == cancel) {
            setVisible(false);
            new StLogin();
        }
    }

    public static void main(String[] args) {
        new Register();
    }
}
