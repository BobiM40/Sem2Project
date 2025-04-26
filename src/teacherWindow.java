import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

    public static DefaultTableModel studentModel, gradeModel;
    private ArrayList<String[]> students, grades;

    public teacherWindow(User user){
        setSize(1000, 800);
        setContentPane(teacherPanel);
        setVisible(true);

        studentModel = new DefaultTableModel();
        studentsTable.setModel(studentModel);
        students = connect.executeQueryStudentsTableTeacherWindow("SELECT s.StudentID, s.firstName, s.lastName, s.email, c.courseName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + user.getUserID() + ";");
        updateStudentsTable();

        gradeModel = new DefaultTableModel();
        gradesTable.setModel(gradeModel);
        grades = connect.executeQueryGradesTableTeacherWindow("SELECT g.GradeID, g.grade, g.weight, s.StudentID, s.firstName, s.lastName, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE t.TeacherID = " + user.getUserID() + ";");
        updateGradesTable();

        welcomeLabel.setText("Welcome " + user.getFirstName() + "! ");

        ImageIcon icon = user.getImageIcon(); // Ensure user.getImg() returns a valid path
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imgLabel.setIcon(icon);
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
}
