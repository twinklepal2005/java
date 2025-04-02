package seatingarrangement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Seatingarrangement extends JFrame implements ActionListener {
    private JButton next;
    private JLabel text;
    private JLabel text1;
    
    Seatingarrangement() {
        setTitle("Seating Arrangement System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Load and scale the background image dynamically
        ImageIcon i1 = new ImageIcon(getClass().getResource("/icon/Designer.jpg"));
        Image i2 = i1.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, screenWidth, screenHeight);
        add(image);

        // Title text (Centered, resized to fit)
        text = new JLabel("SEATING ARRANGEMENT", SwingConstants.CENTER);
        text1 = new JLabel("SYSTEM", SwingConstants.CENTER);
        int textWidth = (int) (screenWidth * 0.7); // Increased width to avoid truncation
        int textHeight = screenHeight / 20;
        text.setBounds((screenWidth - textWidth) / 2, (int) ((double) screenHeight / 3.3), textWidth, textHeight);
        text.setForeground(Color.BLACK);
        text.setFont(new Font("Manrope", Font.BOLD, screenWidth / 40)); // Slightly reduced font size
        image.add(text);
        
         text1.setBounds((screenWidth - textWidth) / 2,(int) ((double) screenHeight / 2.7), textWidth, textHeight);
        text1.setForeground(Color.BLACK);
        text1.setFont(new Font("Manrope", Font.BOLD, screenWidth / 40)); // Slightly reduced font size
        image.add(text1);

        // Next button (Placed relative to screen size)
        next = new JButton("Next");
        next.setBounds(screenWidth - 250, screenHeight - 200, 180, 60);
        next.setBackground(Color.WHITE);
        next.setFont(new Font("Arial", Font.BOLD, screenWidth / 50));
        next.addActionListener(this);
        image.add(next);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Dashboard(); // Assuming Dashboard class exists
    }

    public static void main(String[] args) {
        new Seatingarrangement();
    }
}
