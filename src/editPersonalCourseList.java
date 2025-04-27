import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class editPersonalCourseList extends JFrame{
    private JLabel title;
    private JTable coursesTable;
    private JPanel personalCoursesPanel;
    private JButton addCourseButton;
    private JButton removeCourseButton;
    private JButton closeButton;

    public static DefaultTableModel courseListModel;
    private ArrayList<String[]> courses;

    public editPersonalCourseList(String firstName, String id, boolean role) {

        // ROLE:
        // false -- student
        // true -- teacher

        setSize(800, 600);
        setContentPane(personalCoursesPanel);
        setVisible(true);

        removeCourseButton.setEnabled(false);

        title.setText(firstName + "'s Courses");

        courseListModel = new DefaultTableModel();
        coursesTable.setModel(courseListModel);
        if(role) {
            courses = connect.executeQueryCourseList("SELECT c.CourseID, c.courseName FROM engage.Courses AS c JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + id + ";");
        }else{
            courses = connect.executeQueryCourseList("SELECT c.CourseID, c.courseName, t.TeacherID, t.firstName AS teacherFirstName, t.lastName AS teacherLastName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID LEFT JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE s.StudentID = " + id + ";");
        }
        updateCoursesTable();

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CourseID = JOptionPane.showInputDialog(null, "Enter the course's ID. ", "CourseID: ", JOptionPane.QUESTION_MESSAGE);
                if(!isInteger(CourseID)){
                    JOptionPane.showMessageDialog(null, "Invalid ID! ");
                }else if(connect.checkCourseExists(CourseID)){
                    if(role){
                        connect.assignCourse(id, CourseID);
                        JOptionPane.showMessageDialog(null, "Course successfully added! ");
                        courses.clear();
                        courses = connect.executeQueryCourseList("SELECT c.CourseID, c.courseName FROM engage.Courses AS c JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + id + ";");
                        updateCoursesTable();
                    }else if(!connect.isStudentEnrolled(id, CourseID)){
                        connect.applyCourse(id, CourseID);
                        JOptionPane.showMessageDialog(null, "Course successfully added! ");
                        courses.clear();
                        courses = connect.executeQueryCourseList("SELECT c.CourseID, c.courseName, t.TeacherID, t.firstName AS teacherFirstName, t.lastName AS teacherLastName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE s.StudentID = " + id + ";");
                        updateCoursesTable();
                    }else{
                        JOptionPane.showMessageDialog(null, "Student already attends that course! ");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Course with such ID does not exist! ");
                }
            }
        });
        coursesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if a row is selected
                if (coursesTable.getSelectedRow() != -1) {
                    // Enable the button if a row is selected
                    removeCourseButton.setEnabled(true);
                } else {
                    // Disable the button if no row is selected
                    removeCourseButton.setEnabled(false);
                }
            }
        });
        removeCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CourseID = coursesTable.getValueAt(coursesTable.getSelectedRow(), 0).toString();
                if(role){
                    connect.removeTeacherCourse(CourseID);
                    JOptionPane.showMessageDialog(null, "Course successfully removed! ");
                    courses.clear();
                    courses = connect.executeQueryCourseList("SELECT c.CourseID, c.courseName FROM engage.Courses AS c JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE t.TeacherID = " + id + ";");
                    updateCoursesTable();
                }else{
                    connect.removeStudentCourse(CourseID, id);
                    JOptionPane.showMessageDialog(null, "Course successfully removed! ");
                    courses.clear();
                    courses = connect.executeQueryCourseList("SELECT c.CourseID, c.courseName, t.TeacherID, t.firstName AS teacherFirstName, t.lastName AS teacherLastName FROM engage.Students AS s JOIN engage.StudentsTakingCourses AS stc ON s.StudentID = stc.StudentID JOIN engage.Courses AS c ON stc.CourseID = c.CourseID LEFT JOIN engage.Teachers AS t ON c.TeacherID = t.TeacherID WHERE s.StudentID = " + id + ";");
                    updateCoursesTable();
                }
            }
        });
    }
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);  // Try to parse the string as an integer
            return true;  // If no exception is thrown, it's an integer
        } catch (NumberFormatException e) {
            return false;  // If an exception is thrown, it's not an integer
        }
    }
    private void updateCoursesTable () {
        courseListModel.setRowCount(0);
        for (String[] course : courses) {
            courseListModel.addRow(course);
        }
    }
}
