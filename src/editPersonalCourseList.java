import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class editPersonalCourseList extends JFrame{
    private JLabel title;
    private JTable coursesTable;
    private JPanel personalCoursesPanel;
    private JButton addCourseButton;
    private JButton removeCourseButton;
    private JButton closeButton;

    public editPersonalCourseList() {
        setSize(800, 600);
        setContentPane(personalCoursesPanel);
        setVisible(true);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
