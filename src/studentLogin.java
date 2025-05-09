import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class studentLogin extends JFrame{
    private JLabel logo;
    private JTextField userField;
    private JPasswordField passField;
    private JPanel stLoginPanel;
    private JButton button;

    public studentLogin(){
        setSize(500, 500);
        setContentPane(stLoginPanel);
        setVisible(true);

        button.setEnabled(false);

        ImageIcon icon = (ImageIcon) logo.getIcon();
        if (icon != null) {
            Image img = icon.getImage();
            Image scaled = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(scaled));

        }
        userField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override public void update() {
                button.setEnabled(!userField.getText().isEmpty() && !passField.getText().isEmpty());
            }
        });

        passField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override public void update() {
                button.setEnabled(!userField.getText().isEmpty() && !passField.getText().isEmpty());
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                User user = connect.checkCredentials(username, password, "engage.StudentLogin");

                if (user != null) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose();
                    new studentWindow(user);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }
            }
        });
    }
}
