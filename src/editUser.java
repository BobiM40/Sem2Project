import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class editUser extends JFrame{
    private JLabel logo;
    private JPanel editUserPanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField newEmailField;
    private JButton confirmButton;
    private JButton cancelButton;

    public editUser(String id, boolean role, adminWindow adminWindow) {
        setSize(800, 600);
        setContentPane(editUserPanel);
        setVisible(true);

        ImageIcon icon = (ImageIcon) logo.getIcon();
        if (icon != null) {
            Image img = icon.getImage();
            Image scaled = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(scaled));
        }

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || newEmailField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please, fill in all the fields. ");
                }else if(isValidEmail(newEmailField.getText())){
                    connect.editUser(id, firstNameField.getText(), lastNameField.getText(), newEmailField.getText(), role);
                    JOptionPane.showMessageDialog(null, "Edit successful! ");
                    adminWindow.refreshTables();
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null, "Invalid email! ");
                }
            }
        });
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
