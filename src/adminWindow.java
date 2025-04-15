import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class adminWindow {
    private JPanel adminPanel;
    private JTable table1;
    private JScrollPane scrollPane;
    private JLabel welcomeLabel;
    private JTextField searchField;
    private JButton deleteUserButton;
    private JButton editUserButton;
    private JButton addUserButton;

    public adminWindow() {
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new newUser();
            }
        });
    }
}
