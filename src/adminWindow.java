import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class adminWindow extends JFrame{
    private JPanel adminPanel;
    private JTable table1;
    private JScrollPane scrollPane;
    private JLabel welcomeLabel;
    private JTextField searchField;
    private JButton deleteUserButton;
    private JButton editUserButton;
    private JButton addUserButton;
    private JLabel imgLabel;

    public adminWindow(User user) {
        setSize(500, 500);
        setContentPane(adminPanel);
        setVisible(true);

        welcomeLabel.setText("Welcome " + user.getFirstName() + "! ");

        ImageIcon icon = user.getImageIcon(); // Ensure user.getImg() returns a valid path
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imgLabel.setIcon(icon);
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new newUser();
            }
        });
    }
}
