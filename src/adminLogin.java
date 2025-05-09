import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class adminLogin extends JFrame{
    private JPanel aLoginPanel;
    private JTextField userField;
    private JPasswordField passField;
    private JButton button;
    private JLabel logo;

    public adminLogin(){
        setSize(500, 500);
        setContentPane(aLoginPanel);
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

                User user = connect.checkCredentials(username, password, "engage.AdminLogin");

                if (user != null) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose();
                    new adminWindow(user);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }
            }
        });
    }
}
