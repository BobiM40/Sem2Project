import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class gradeCalc extends JFrame{
    private JTextField currentGradeField;
    private JTextField desiredGradeField;
    private JTextField examWeightField;
    private JLabel score;
    private JButton calculateButton;
    private JButton closeWindowButton;
    private JPanel calcPanel;
    private JLabel logo;

    public gradeCalc() {
        setSize(500, 500);
        setContentPane(calcPanel);
        setVisible(true);

        ImageIcon icon = (ImageIcon) logo.getIcon();
        if (icon != null) {
            Image img = icon.getImage();
            Image scaled = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(scaled));

        }

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentGradeField.getText().isEmpty() || desiredGradeField.getText().isEmpty() || examWeightField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please, fill in all the fields!");
                }else if(canBeDouble(currentGradeField.getText()) && canBeDouble(desiredGradeField.getText()) && canBeDouble(examWeightField.getText())){
                    double desiredGrade = Double.parseDouble(desiredGradeField.getText());
                    double currentGrade = Double.parseDouble(currentGradeField.getText());
                    double examWeight = Double.parseDouble(examWeightField.getText());
                    double requiredScore = (desiredGrade - (currentGrade * (100 - examWeight) / 100)) / (examWeight / 100);
                    score.setText(Double.toString(Math.round(requiredScore * 100.0) / 100.0));
                }else{
                    JOptionPane.showMessageDialog(null, "Please, fill in all the fields accordingly!");

                }
            }
        });
        closeWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }
    public static boolean canBeDouble(String str) {
        try {
            Double.parseDouble(str);  // Attempt to parse the string as a double
            return true;  // If no exception is thrown, it's a valid double
        } catch (NumberFormatException e) {
            return false;  // If an exception is thrown, it's not a valid double
        }
    }
}
