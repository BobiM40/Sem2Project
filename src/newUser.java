import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

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
    private JButton selectImageButton;
    private JLabel imageLabel;

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
                // Convert the icon from JLabel to an InputStream (for image storage in the DB)
                ImageIcon icon = (ImageIcon) imageLabel.getIcon();
                InputStream imageInputStream = null;

                if (icon != null) {
                    // Create a BufferedImage to hold the icon
                    BufferedImage bufferedImage = new BufferedImage(
                            icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

                    // Create a Graphics object to draw the icon onto the BufferedImage
                    Graphics g = bufferedImage.createGraphics();
                    icon.paintIcon(null, g, 0, 0);
                    g.dispose();  // Dispose of the Graphics object once done

                    // Convert the BufferedImage to an InputStream
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                        imageInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    } catch (IOException ex) {
                        // Show an error message if there's an issue converting the image to a binary stream
                        JOptionPane.showMessageDialog(null, "Error converting image to binary stream.");
                        return;  // Exit the method if there's an error
                    }
                }

                connect.addUser(usernameField.getText(), firstNameField.getText(), lastNameField.getText(), emailField.getText(), hashedPassword, (byte)roles.getSelectedIndex(), imageInputStream);
                JOptionPane.showMessageDialog(null, "User added successfully. ");
                setVisible(false);
            }
        });
        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a file chooser window for selecting a file
                JFileChooser fileChooser = new JFileChooser();

                // Show the open dialog to the user and store the result (APPROVE_OPTION if a file is selected)
                int result = fileChooser.showOpenDialog(null);

                // If the user selects a file, proceed with loading and displaying the image
                if (result == JFileChooser.APPROVE_OPTION) {
                    // Get the selected file
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        // Read the image from the selected file
                        Image image = ImageIO.read(selectedFile);

                        // Scale the image to 100x100 pixels and create an ImageIcon
                        ImageIcon icon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH));

                        // Set the ImageIcon to the label and clear any existing text
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    } catch (IOException ex) {
                        // Show an error message if there is an issue loading the image
                        JOptionPane.showMessageDialog(null, "Error loading image.");
                    }
                }
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
                roles.getSelectedIndex() != -1 &&
                imageLabel.getIcon() != null;

        addUserButton.setEnabled(allValid);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}

