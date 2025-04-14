import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mindrot.jbcrypt.BCrypt;

public class newUser extends JFrame{
    private JPanel newUserPanel;
    private JButton addUserButton;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JTextField emailField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField usernameField;
    private JLabel logo;
    private JComboBox roles;
    private JLabel emailFeedback;
    private JLabel passwordFeedback;
    private JLabel usernameFeedback;

    private boolean validUser = true, validEmail = false, passwordsMatch = false;

    public newUser() {
        setSize(500, 500);
        setContentPane(newUserPanel);
        setVisible(true);

        addUserButton.setEnabled(false);

        ImageIcon icon = (ImageIcon) logo.getIcon();
        if (icon != null) {
            Image img = icon.getImage();
            Image scaled = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(scaled));
        }


        usernameField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                String username = usernameField.getText().trim();
                validUser = connect.isValidUser(username);
                if (!validUser) {
                    usernameFeedback.setText("Username already taken!");
                } else {
                    usernameFeedback.setText("");
                }            }
        });

        emailField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                validEmail = isValidEmail(emailField.getText());
                if(!validEmail){
                    emailFeedback.setText("Invalid Email!");
                }else{
                    emailFeedback.setText("");
                }
                updateButtonState();
            }
        });

        firstNameField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                updateButtonState();
            }
        });

        lastNameField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                updateButtonState();
            }
        });

        roles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateButtonState();
            }
        });

        passwordField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                if(passwordField.getText().equals(repeatPasswordField.getText())){
                    passwordsMatch = true;
                    passwordFeedback.setText("");
                }else{
                    passwordsMatch = false;
                    passwordFeedback.setText("Passwords don't match!");
                }
                updateButtonState();
            }
        });

        repeatPasswordField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                if(passwordField.getText().equals(repeatPasswordField.getText())){
                    passwordsMatch = true;
                    passwordFeedback.setText("");
                }else{
                    passwordsMatch = false;
                    passwordFeedback.setText("Passwords don't match!");
                }
                updateButtonState();
            }
        });

        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hashedPassword = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());
                connect.addUser(usernameField.getText(), firstNameField.getText(), lastNameField.getText(), emailField.getText(), hashedPassword, (byte)roles.getSelectedIndex());
                JOptionPane.showMessageDialog(null, "Registered successfully. ");
                setVisible(false);
                //username check

            }
        });
    }

    private void updateButtonState() {
        boolean passwordsFilled = passwordField.getPassword().length > 0 &&
                repeatPasswordField.getPassword().length > 0;

        boolean allValid = validUser && validEmail &&
                passwordsMatch && passwordsFilled &&
                !firstNameField.getText().isEmpty() &&
                !lastNameField.getText().isEmpty() &&
                !usernameField.getText().isEmpty() &&
                roles.getSelectedIndex() != -1;

        addUserButton.setEnabled(allValid);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}

