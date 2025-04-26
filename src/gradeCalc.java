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
                }else if (!currentGradeField.getText().matches("[0-9%,.]+") || !desiredGradeField.getText().matches("[0-9%,.]+") || !examWeightField.getText().matches("[0-9%,.]+")) {
                    JOptionPane.showMessageDialog(null, "Please, fill in all the fields accordingly!");
                } else {
                    try{
                        double desiredGrade;
                        if(desiredGradeField.getText().contains("%")){
                            desiredGrade = Double.parseDouble(desiredGradeField.getText().replace("%", "")) / 100;
                        }else{
                            desiredGrade = Double.parseDouble(desiredGradeField.getText());
                        }
                        double currentGrade;
                        if(currentGradeField.getText().contains("%")){
                            currentGrade = Double.parseDouble(currentGradeField.getText().replace("%", "")) / 100;
                        }else{
                            currentGrade = Double.parseDouble(currentGradeField.getText());
                        }
                        double examWeight;
                        if(examWeightField.getText().contains("%")){
                            examWeight = Double.parseDouble(examWeightField.getText().replace("%", "")) / 100;
                        }else{
                            examWeight = Double.parseDouble(examWeightField.getText());
                        }
                        double requiredGrade = (desiredGrade - currentGrade * (1 - examWeight)) / examWeight;
                        score.setText(String.valueOf(requiredGrade));
                    }catch (NumberFormatException er) {
                        System.out.println("Invalid number format! " + er);
                    }
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
}
