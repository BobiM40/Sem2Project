import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class newGrade extends JFrame{
    private JLabel logo;
    private JTextField gradeField;
    private JTextField weightField;
    private JButton addGradeButton;
    private JButton cancelButton;
    private JPanel newGradePanel;


    public newGrade(String StudentID, String CourseID, teacherWindow teacherWindowInstance, User user) {
        setSize(800, 600);
        setContentPane(newGradePanel);
        setVisible(true);

        ImageIcon icon = (ImageIcon) logo.getIcon();
        if (icon != null) {
            Image img = icon.getImage();
            Image scaled = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(scaled));
        }

        addGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!gradeField.getText().isEmpty() && !weightField.getText().isEmpty() && isValid(gradeField.getText()) && isValid(weightField.getText())){
                    connect.addGrade(StudentID, CourseID, gradeField.getText(), weightField.getText());
                    JOptionPane.showMessageDialog(null, "Grade added successfully. ");
                    if (teacherWindowInstance != null) {
                        teacherWindowInstance.refreshGradesTable(user); // Call a method to refresh the grades table in teacherWindow
                    }
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null, "Please fill all the fields accordingly! ");
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    private boolean isValid(String value){
        try {
            Double.parseDouble(value);  // Try to parse the string as a double
            return true;  // If no exception is thrown, the string is a valid double
        } catch (NumberFormatException e) {
            return false;  // If exception is thrown, it's not a valid double
        }
    }
}
