import javax.swing.*;

public class studentWindow extends JFrame{
    private JPanel studentPanel;
    private JScrollPane scrollPane;
    private JTable table1;
    private JButton gradeCalculatorButton;
    private JLabel imgLabel;

    public studentWindow(){
        setSize(500, 500);
        setContentPane(studentPanel);
        setVisible(true);

    }
}
