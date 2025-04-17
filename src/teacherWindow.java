import javax.swing.*;

public class teacherWindow extends JFrame{
    private JPanel teacherPanel;
    private JScrollPane scrollPane;
    private JTable table1;
    private JTextField searchField;
    private JButton deleteGradeButton;
    private JButton editGradeButton;
    private JButton addGradeButton;
    private JLabel welcomeLabel;
    private JLabel imgLabel;

    public teacherWindow(){
        setSize(500, 500);
        setContentPane(teacherPanel);
        setVisible(true);

    }
}
