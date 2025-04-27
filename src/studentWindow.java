import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        grades = connect.executeQueryGradesTableStudentWindow("SELECT g.grade, g.weight, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE s.StudentID = " + user.getUserID() + ";");
        updateGradesTable();

        avgModel = new DefaultTableModel();
        gradeAverageTable.setModel(avgModel);
        avgs = connect.executeQueryAverageTableStudentWindow("SELECT c.courseName, ROUND(SUM(g.grade * g.weight) / SUM(g.weight), 2) AS weightedAverage FROM engage.Grades g JOIN engage.Courses c ON g.CourseID = c.CourseID JOIN engage.StudentsTakingCourses stc ON g.CourseID = stc.CourseID WHERE stc.StudentID = " + user.getUserID() + " GROUP BY c.courseName;");
        updateAvgTable();

        welcomeLabel.setText("Welcome " + user.getFirstName() + "! ");

        ImageIcon icon = user.getImageIcon(); // Ensure user.getImg() returns a valid path
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imgLabel.setIcon(icon);
        sortBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sort;
                switch (sortBox.getSelectedIndex()){
                    case 0:
                        grades.clear();
                        grades = connect.executeQueryGradesTableStudentWindow("SELECT g.grade, g.weight, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE s.StudentID = " + user.getUserID() + " ORDER BY g.grade ASC;");
                        updateGradesTable();
                        break;
                    case 1:
                        grades.clear();
                        grades = connect.executeQueryGradesTableStudentWindow("SELECT g.grade, g.weight, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE s.StudentID = " + user.getUserID() + " ORDER BY g.grade DESC;");
                        updateGradesTable();
                        break;
                    case 2:
                        grades.clear();
                        grades = connect.executeQueryGradesTableStudentWindow("SELECT g.grade, g.weight, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE s.StudentID = " + user.getUserID() + " ORDER BY g.weight ASC;");
                        updateGradesTable();
                        break;
                    case 3:
                        grades.clear();
                        grades = connect.executeQueryGradesTableStudentWindow("SELECT g.grade, g.weight, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE s.StudentID = " + user.getUserID() + " ORDER BY g.weight DESC;");
                        updateGradesTable();
                        break;
                    case 4:
                        grades.clear();
                        grades = connect.executeQueryGradesTableStudentWindow("SELECT g.grade, g.weight, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE s.StudentID = " + user.getUserID() + " ORDER BY c.courseName ASC;");
                        updateGradesTable();
                        break;
                    case 5:
                        grades.clear();
                        grades = connect.executeQueryGradesTableStudentWindow("SELECT g.grade, g.weight, c.courseName FROM engage.Grades AS g JOIN engage.Courses AS c ON g.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID JOIN engage.Students AS s ON g.StudentID = s.StudentID WHERE s.StudentID = " + user.getUserID() + " ORDER BY c.courseName DESC;");
                        updateGradesTable();
                        break;
                    default:
                        System.out.println("Something went wrong with sorting. ");
                        break;
                }
            }
        });
        gradeCalculatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new gradeCalc();
            }
        });
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
