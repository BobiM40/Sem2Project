import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class adminWindow extends JFrame{
    private JPanel adminPanel;
    private JLabel welcomeLabel;
    private JButton deleteUserButton;
    private JButton addUserButton;
    private JLabel imgLabel;
    private JTextField searchTeachersField;
    private JTextField searchStudentsField;
    private JTable studentsTable;
    private JTable teachersTable;
    private JButton editStudentButton;
    private JButton editStudentCoursesButton;
    private JButton editTeacherButton;
    private JButton editTeacherCoursesButton;
    private JButton courseListButton;

    public static DefaultTableModel studentModel, teacherModel;
    private ArrayList<String[]> students, teachers;
    public adminWindow(User user) {
        setSize(1000, 800);
        setContentPane(adminPanel);
        setVisible(true);

        studentModel = new DefaultTableModel();
        studentsTable.setModel(studentModel);
        students = connect.executeQueryStudentsTableAdminWindow("SELECT StudentID, firstName, lastName, email FROM engage.Students");
        updateStudentsTable();

        teacherModel = new DefaultTableModel();
        teachersTable.setModel(teacherModel);
        teachers = connect.executeQueryTeachersTableAdminWindow("SELECT TeacherID firstName, lastName, email FROM engage.Teachers");
        updateTeachersTable();

        welcomeLabel.setText("Welcome " + user.getFirstName() + "! ");

        ImageIcon icon = user.getImageIcon(); // Ensure user.getImg() returns a valid path
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imgLabel.setIcon(icon);
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new newUser();
            }
        });
        courseListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new courseWindow();
            }
        });
    }
    private void updateStudentsTable () {
        studentModel.setRowCount(0);
        for (String[] student : students) {
            studentModel.addRow(student);
        }
    }
    private void updateTeachersTable () {
        teacherModel.setRowCount(0);
        for (String[] teacher : teachers) {
            teacherModel.addRow(teacher);
        }
    }
}
