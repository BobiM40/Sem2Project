import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class teacherLogin extends JFrame{
    private JPanel tLoginPanel;
    private JLabel logo;
    private JTextField userField;
    private JPasswordField passField;
    private JButton button;

    public teacherLogin(){
        setSize(500, 500);
        setContentPane(tLoginPanel);
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

                boolean valid = connect.checkCredentials(username, password, "engage.TeacherLogin");

                if (valid) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    // proceed to next screen or admin panel
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }
            }
        });
    }
}
