import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class studentWindow extends JFrame{
    private JPanel studentPanel;
    private JButton gradeCalculatorButton;
    private JLabel imgLabel;
    private JLabel welcomeLabel;
    private JTable gradeAverageTable;
    private JTable gradesTable;
    private JComboBox sortBox;

    public static DefaultTableModel gradeModel, avgModel;
    private ArrayList<String[]> grades, avgs;

    public studentWindow(User user){
        setSize(1000, 800);
        setContentPane(studentPanel);
        setVisible(true);

        gradeModel = new DefaultTableModel();
        gradesTable.setModel(gradeModel);
        grades = connect.executeQueryGradesTableStudentWindow("SELECT g.grade, g.weight, c.courseName, t.firstName AS teacherFirstName, t.lastName AS teacherLastName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE s.StudentID = " + user.getUserID() + ";");
        updateGradesTable();

        avgModel = new DefaultTableModel();
        gradeAverageTable.setModel(avgModel);
        avgs = connect.executeQueryTeachersTableAdminWindow("SELECT c.courseName, SUM(g.grade * g.weight) / SUM(g.weight) AS overallGrade FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE s.StudentID = " + user.getUserID() + " GROUP BY c.courseName ORDER BY c.courseName;");
        updateAvgTable();

        welcomeLabel.setText("Welcome " + user.getFirstName() + "! ");

        ImageIcon icon = user.getImageIcon(); // Ensure user.getImg() returns a valid path
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imgLabel.setIcon(icon);
    }
    private void updateGradesTable () {
        gradeModel.setRowCount(0);
        for (String[] grade : grades) {
            gradeModel.addRow(grade);
        }
    }
    private void updateAvgTable () {
        avgModel.setRowCount(0);
        for (String[] avg : avgs) {
            avgModel.addRow(avg);
        }
    }
}
