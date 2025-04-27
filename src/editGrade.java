import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class editGrade extends JFrame{
    private JLabel logo;
    private JPanel editGradePanel;
    private JTextField gradeField;
    private JTextField weightField;
    private JButton confirmButton;
    private JButton cancelButton;

    public editGrade(String GradeID, teacherWindow teacherWindowInstance, User user) {
        setSize(800, 600);
        setContentPane(editGradePanel);
        setVisible(true);

        ImageIcon icon = (ImageIcon) logo.getIcon();
        if (icon != null) {
            Image img = icon.getImage();
            Image scaled = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(scaled));
        }

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!gradeField.getText().isEmpty() && !weightField.getText().isEmpty() && isValid(gradeField.getText()) && isValid(weightField.getText())){
                    connect.editGrade(gradeField.getText(), weightField.getText(), GradeID);
                    JOptionPane.showMessageDialog(null, "Grade edited successfully. ");
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
