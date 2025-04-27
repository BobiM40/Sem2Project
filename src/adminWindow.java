import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class adminWindow extends JFrame{
    private JPanel adminPanel;
    private JLabel welcomeLabel;
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
    private JButton deleteStudentButton;
    private JButton deleteTeacherButton;

    public static DefaultTableModel studentModel, teacherModel;
    private ArrayList<String[]> students, teachers;

    public adminWindow(User user) {
        setSize(1000, 800);
        setContentPane(adminPanel);
        setVisible(true);

        editStudentButton.setEnabled(false);
        editStudentCoursesButton.setEnabled(false);
        deleteStudentButton.setEnabled(false);

        editTeacherButton.setEnabled(false);
        editTeacherCoursesButton.setEnabled(false);
        deleteTeacherButton.setEnabled(false);

        studentModel = new DefaultTableModel();
        studentsTable.setModel(studentModel);
        students = connect.executeQueryStudentsTableAdminWindow("SELECT StudentID, firstName, lastName, email FROM engage.Students");
        updateStudentsTable();

        teacherModel = new DefaultTableModel();
        teachersTable.setModel(teacherModel);
        teachers = connect.executeQueryTeachersTableAdminWindow("SELECT TeacherID, firstName, lastName, email FROM engage.Teachers");
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
        studentsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if a row is selected
                if (studentsTable.getSelectedRow() != -1) {
                    // Enable the button if a row is selected
                    editStudentButton.setEnabled(true);
                    editStudentCoursesButton.setEnabled(true);
                    deleteStudentButton.setEnabled(true);
                } else {
                    // Disable the button if no row is selected
                    editStudentButton.setEnabled(false);
                    editStudentCoursesButton.setEnabled(false);
                    deleteStudentButton.setEnabled(false);
                }
            }
        });
        teachersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if a row is selected
                if (teachersTable.getSelectedRow() != -1) {
                    // Enable the button if a row is selected
                    editTeacherButton.setEnabled(true);
                    editTeacherCoursesButton.setEnabled(true);
                    deleteTeacherButton.setEnabled(true);
                } else {
                    // Disable the button if no row is selected
                    editTeacherButton.setEnabled(false);
                    editTeacherCoursesButton.setEnabled(false);
                    deleteTeacherButton.setEnabled(false);
                }
            }
        });
        editStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String StudentID = studentsTable.getValueAt(studentsTable.getSelectedRow(), 0).toString();
                new editUser(StudentID, false, adminWindow.this);
            }
        });
        editTeacherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String TeacherID = teachersTable.getValueAt(teachersTable.getSelectedRow(), 0).toString();
                new editUser(TeacherID, true, adminWindow.this);
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
    public void refreshTables(){
        students.clear();
        students = connect.executeQueryStudentsTableAdminWindow("SELECT StudentID, firstName, lastName, email FROM engage.Students");
        updateStudentsTable();

        teachers.clear();
        teachers = connect.executeQueryTeachersTableAdminWindow("SELECT TeacherID, firstName, lastName, email FROM engage.Teachers");
        updateTeachersTable();
    }
}
