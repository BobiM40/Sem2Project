import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class courseWindow extends JFrame{
    private JButton newCourseButton;
    private JButton deleteCourseButton;
    private JTable coursesTable;
    private JButton closeButton;
    private JPanel coursePanel;
    private JButton editCourseButton;

    public static DefaultTableModel courseModel;
    private ArrayList<String[]> courses;

    public courseWindow() {
        setSize(800, 600);
        setContentPane(coursePanel);
        setVisible(true);

        editCourseButton.setEnabled(false);
        deleteCourseButton.setEnabled(false);

        courseModel = new DefaultTableModel();
        coursesTable.setModel(courseModel);
        courses = connect.executeQueryCourses("SELECT CourseID, TeacherID, courseName FROM engage.Courses;");
        updateCoursesTable();

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        newCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new newCourse(courseWindow.this);
            }
        });
        coursesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if a row is selected
                if (coursesTable.getSelectedRow() != -1) {
                    // Enable the button if a row is selected
                    editCourseButton.setEnabled(true);
                    deleteCourseButton.setEnabled(true);
                } else {
                    // Disable the button if no row is selected
                    editCourseButton.setEnabled(false);
                    deleteCourseButton.setEnabled(false);
                }
            }
        });
        editCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CourseID = coursesTable.getValueAt(coursesTable.getSelectedRow(), 0).toString();
                new editCourse(courseWindow.this, CourseID);
            }
        });
        deleteCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CourseID = coursesTable.getValueAt(coursesTable.getSelectedRow(), 0).toString();
                connect.deleteCourse(CourseID);
                JOptionPane.showMessageDialog(null, "Course deleted successfully. ");
                courses.clear();
                courses = connect.executeQueryCourses("SELECT CourseID, TeacherID, courseName FROM engage.Courses;");
                updateCoursesTable();
            }
        });
    }
    private void updateCoursesTable () {
        courseModel.setRowCount(0);
        for (String[] course : courses) {
            courseModel.addRow(course);
        }
    }
    public void refreshCourseTable(){
        courses.clear();
        courses = connect.executeQueryCourses("SELECT CourseID, TeacherID, courseName FROM engage.Courses;");
        updateCoursesTable();
    }
}
