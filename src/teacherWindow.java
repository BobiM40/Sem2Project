import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class teacherWindow extends JFrame{
    private JPanel teacherPanel;
    private JButton deleteGradeButton;
    private JButton editGradeButton;
    private JButton addGradeButton;
    private JLabel welcomeLabel;
    private JLabel imgLabel;
    private JTextField studentsSeachField;
    private JTextField gradesSearchField;
    private JTable studentsTable;
    private JTable gradesTable;
    private JComboBox studentSortBox;
    private JComboBox gradeSortBox;

    public static DefaultTableModel studentModel, gradeModel;
    private ArrayList<String[]> students, grades;

    public teacherWindow(User user){
        setSize(1400, 800);
        setContentPane(teacherPanel);
        setVisible(true);

        addGradeButton.setEnabled(false);
        editGradeButton.setEnabled(false);
        deleteGradeButton.setEnabled(false);

        studentModel = new DefaultTableModel();
        studentsTable.setModel(studentModel);
        students = connect.executeQueryStudentsTableTeacherWindow("SELECT s.StudentID, s.firstName, s.lastName, s.email, c.CourseID, c.courseName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + user.getUserID() + ";");
        updateStudentsTable();

        gradeModel = new DefaultTableModel();
        gradesTable.setModel(gradeModel);
        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + ";");
        updateGradesTable();

        welcomeLabel.setText("Welcome " + user.getFirstName() + "! ");

        ImageIcon icon = user.getImageIcon(); // Ensure user.getImg() returns a valid path
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imgLabel.setIcon(icon);
        studentSortBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(studentSortBox.getSelectedIndex()){
                    case 0:
                        students.clear();
                        students = connect.executeQueryStudentsTableTeacherWindow("SELECT s.StudentID, s.firstName, s.lastName, s.email, c.CourseID, c.courseName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY s.StudentID ASC;");
                        updateStudentsTable();
                        break;
                    case 1:
                        students.clear();
                        students = connect.executeQueryStudentsTableTeacherWindow("SELECT s.StudentID, s.firstName, s.lastName, s.email, c.CourseID, c.courseName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY s.StudentID DESC;");
                        updateStudentsTable();
                        break;
                    case 2:
                        students.clear();
                        students = connect.executeQueryStudentsTableTeacherWindow("SELECT s.StudentID, s.firstName, s.lastName, s.email, c.CourseID, c.courseName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY s.firstName ASC;");
                        updateStudentsTable();
                        break;
                    case 3:
                        students.clear();
                        students = connect.executeQueryStudentsTableTeacherWindow("SELECT s.StudentID, s.firstName, s.lastName, s.email, c.CourseID, c.courseName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY s.lastName ASC;");
                        updateStudentsTable();
                        break;
                    case 4:
                        students.clear();
                        students = connect.executeQueryStudentsTableTeacherWindow("SELECT s.StudentID, s.firstName, s.lastName, s.email, c.CourseID, c.courseName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY c.courseName ASC;");
                        updateStudentsTable();
                        break;
                    default:
                        System.out.println("Something went wrong with sorting. ");
                        break;
                }
            }
        });
        gradeSortBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(gradeSortBox.getSelectedIndex()){
                    case 0:
                        grades.clear();
                        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY g.GradeID ASC;");
                        updateGradesTable();
                        break;
                    case 1:
                        grades.clear();
                        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY g.GradeID DESC;");
                        updateGradesTable();
                        break;
                    case 2:
                        grades.clear();
                        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY g.grade ASC;");
                        updateGradesTable();
                        break;
                    case 3:
                        grades.clear();
                        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY g.grade DESC;");
                        updateGradesTable();
                        break;
                    case 4:
                        grades.clear();
                        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY g.weight ASC;");
                        updateGradesTable();
                        break;
                    case 5:
                        grades.clear();
                        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY s.StudentID ASC;");
                        updateGradesTable();
                        break;
                    case 6:
                        grades.clear();
                        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + " ORDER BY c.courseName ASC;");
                        updateGradesTable();
                        break;
                    default:
                        System.out.println("Something went wrong with sorting. ");
                        break;
                }
            }
        });
        studentsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if a row is selected
                if (studentsTable.getSelectedRow() != -1) {
                    // Enable the button if a row is selected
                    addGradeButton.setEnabled(true);
                } else {
                    // Disable the button if no row is selected
                    addGradeButton.setEnabled(false);
                }
            }
        });
        gradesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if a row is selected
                if (gradesTable.getSelectedRow() != -1) {
                    // Enable the button if a row is selected
                    editGradeButton.setEnabled(true);
                    deleteGradeButton.setEnabled(true);
                } else {
                    // Disable the button if no row is selected
                    editGradeButton.setEnabled(false);
                    deleteGradeButton.setEnabled(false);
                }
            }
        });
        addGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String StudentID = studentsTable.getValueAt(studentsTable.getSelectedRow(), 0).toString();
                String CourseID = studentsTable.getValueAt(studentsTable.getSelectedRow(), 4).toString();
                new newGrade(StudentID, CourseID, teacherWindow.this, user);
                grades.clear();
                grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + ";");
                updateGradesTable();
            }
        });
        editGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String GradeID = gradesTable.getValueAt(gradesTable.getSelectedRow(), 0).toString();
                new editGrade(GradeID, teacherWindow.this, user);
                grades.clear();
                grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + ";");
                updateGradesTable();
            }
        });
    }
    private void updateStudentsTable () {
        studentModel.setRowCount(0);
        for (String[] student : students) {
            studentModel.addRow(student);
        }
    }
    private void updateGradesTable () {
        gradeModel.setRowCount(0);
        for (String[] grade : grades) {
            gradeModel.addRow(grade);
        }
    }
    public void refreshGradesTable(User user){
        grades.clear();
        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.CourseID, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + ";");
        updateGradesTable();
    }
}
