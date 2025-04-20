import javax.swing.*;
import java.awt.*;

public class teacherWindow extends JFrame{
    private JPanel teacherPanel;
    private JScrollPane scrollPane;
    private JTable table1;
    private JTextField searchField;
    private JButton deleteGradeButton;
    private JButton editGradeButton;
    private JButton addGradeButton;
    private JLabel welcomeLabel;
    private JLabel imgLabel;

    public teacherWindow(User user){
        setSize(500, 500);
        setContentPane(teacherPanel);
        setVisible(true);

        welcomeLabel.setText("Welcome " + user.getFirstName() + "! ");

        ImageIcon icon = user.getImageIcon(); // Ensure user.getImg() returns a valid path
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imgLabel.setIcon(icon);
    }
}
