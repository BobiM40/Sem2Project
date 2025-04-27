import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class newCourse extends JFrame{
    private JPanel newCoursePanel;
    private JLabel logo;
    private JTextField courseNameField;
    private JTextField teacherIdField;
    private JButton addCourseButton;
    private JButton cancelButton;

    public newCourse(courseWindow courseWindow) {
        setSize(800, 600);
        setContentPane(newCoursePanel);
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
        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isPositiveInteger(teacherIdField.getText())){
                    if(connect.checkTeacherExists(teacherIdField.getText())){
                        connect.addCourse(teacherIdField.getText(), courseNameField.getText());
                        JOptionPane.showMessageDialog(null, "Course added successfully. ");
                        courseWindow.refreshCourseTable();
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(null, "Teacher with such ID does not exist! ");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Invalid ID! ");
                }
            }
        });
    }
    private boolean isPositiveInteger(String str) {
        try {
            // Try to parse the string to an integer
            int number = Integer.parseInt(str);

            // Check if the number is positive
            return number > 0;
        } catch (NumberFormatException e) {
            // If parsing fails (invalid number), return false
            return false;
        }
    }

}
