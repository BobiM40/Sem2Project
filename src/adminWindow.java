import javax.swing.*;
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

    public adminWindow() {
        setSize(500, 500);
        setContentPane(adminPanel);
        setVisible(true);

        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new newUser();
            }
        });
    }
}
