import javax.swing.*;
import java.awt.*;

public class studentWindow extends JFrame{
    private JPanel studentPanel;
    private JScrollPane scrollPane;
    private JTable table1;
    private JButton gradeCalculatorButton;
    private JLabel imgLabel;
    private JLabel welcomeLabel;

    public studentWindow(User user){
        setSize(500, 500);
        setContentPane(studentPanel);
        setVisible(true);

        welcomeLabel.setText("Welcome " + user.getFirstName() + "! ");

        ImageIcon icon = user.getImageIcon(); // Ensure user.getImg() returns a valid path
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imgLabel.setIcon(icon);
    }
}
