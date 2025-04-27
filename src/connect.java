import org.mindrot.jbcrypt.BCrypt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.sql.*;

public class connect {
    private static final String URL = "jdbc:mysql://localhost:3306/engage";
    private static final String USER = "root";
    private static final String PASSWORD = "0000";

    public static void addUser(String username, String firstName, String lastName, String email, String hashPassword, byte role, InputStream img) {

        /*
        ROLE:
            0 - Student
            1 - Teacher
            2 - Admin
         */
        String table = "", id = "";
        switch(role){
            case 0:
                table = "engage.Students";
                break;
            case 1:
                table = "engage.Teachers";
                break;
            case 2:
                table = "engage.Admins";
                break;
        }

        String query = "INSERT INTO " + table + " (firstName, lastName, email, img) VALUES (?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Start transaction

            // Insert into the respective table (Admins, Teachers, or Students)
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setBinaryStream(4, img);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // If insertion is successful, retrieve the generated ID
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1); // Get the generated ID

                    // Now insert into the login table (StudentLogin, TeacherLogin, AdminLogin)
                    String loginTable = "";
                    String loginIdColumn = "";
                    switch(role) {
                        case 0:
                            loginTable = "engage.StudentLogin";
                            loginIdColumn = "StudentID";
                            break;
                        case 1:
                            loginTable = "engage.TeacherLogin";
                            loginIdColumn = "TeacherID";
                            break;
                        case 2:
                            loginTable = "engage.AdminLogin";
                            loginIdColumn = "AdminID";
                            break;
                    }

                    query = "INSERT INTO " + loginTable + " (username, " + loginIdColumn + ", password) VALUES (?, ?, ?)";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, username);
                    pstmt.setInt(2, generatedId); // Use the generated ID
                    pstmt.setString(3, hashPassword);

                    rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        connection.commit(); // Commit both queries as part of a single transaction
                        System.out.println("Insertion committed successfully for user: " + username);
                    } else {
                        connection.rollback(); // If login insertion fails, roll back
                    }
                }
            } else {
                connection.rollback(); // If user insertion fails, roll back
            }
        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback(); // Roll back in case of error
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void editUser(String id, String firstName, String lastName, String email, boolean role) {

        /*
        ROLE:
            false - Student
            true - Teacher
         */
        String table = "";
        if(role){
            table = "engage.Teachers";
        }else{
            table = "engage.Students";
        }

        String query = "UPDATE " + table + "SET firstName = ?, lastName = ?, email = ? WHERE StudentID = ?;";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Start transaction

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Insert into the respective table
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, id);

            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Edit committed successfully for " + table + " and user id: " + id);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void addGrade(String StudentID, String CourseID, String grade, String weight){
        // SQL query for inserting actor data into the database
        String query = "INSERT INTO engage.Grades (StudentID, CourseID, grade, weight) VALUES (?, ?, ?, ?);";

        // Declare variables for database connection and prepared statement
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Disable auto-commit to manually handle transactions
            connection.setAutoCommit(false);

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Set the values for the query placeholders using the input data
            pstmt.setString(1, StudentID);  // Set student id
            pstmt.setString(2, CourseID);   // Set course id
            pstmt.setString(3, grade);  // Set the grade value
            pstmt.setString(4, weight);  // Set the grade weight

            // Execute the query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Insertion committed successfully for grade with StudentID: " + StudentID + " and CourseID: " + CourseID);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void editGrade(String grade, String weight, String GradeID){
        // SQL query for inserting actor data into the database
        String query = "UPDATE engage.Grades SET grade = ?, weight = ? WHERE GradeID = ?;";

        // Declare variables for database connection and prepared statement
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Disable auto-commit to manually handle transactions
            connection.setAutoCommit(false);

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Set the values for the query placeholders using the input data
            pstmt.setString(1, grade);  // Set the grade value
            pstmt.setString(2, weight);   // Set the weight
            pstmt.setString(3, GradeID);  // Set the grade id

            // Execute the query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Edit committed successfully for grade with id: " + GradeID);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void deleteGrade(String GradeID){
        // SQL query for inserting actor data into the database
        String query = "DELETE FROM engage.Grades WHERE GradeID = ?;";

        // Declare variables for database connection and prepared statement
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Disable auto-commit to manually handle transactions
            connection.setAutoCommit(false);

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Set the values for the query placeholders using the input data
            pstmt.setString(1, GradeID);  // Set the grade id

            // Execute the query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Deletion committed successfully for grade with id: " + GradeID);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void addCourse(String TeacherID, String courseName){
        // SQL query for inserting actor data into the database
        String query = "INSERT INTO engage.Courses (courseName, TeacherID) VALUES (?, ?);";

        // Declare variables for database connection and prepared statement
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Disable auto-commit to manually handle transactions
            connection.setAutoCommit(false);

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Set the values for the query placeholders using the input data
            pstmt.setString(1, courseName);  // Set course name
            pstmt.setString(2, TeacherID);   // Set teacher id

            // Execute the query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Insertion committed successfully for course: " + courseName);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void editCourse(String CourseID, String TeacherID, String courseName){
        // SQL query for inserting actor data into the database
        String query = "UPDATE engage.Courses SET courseName = ?, TeacherID = ? WHERE CourseID = ?;";

        // Declare variables for database connection and prepared statement
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Disable auto-commit to manually handle transactions
            connection.setAutoCommit(false);

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Set the values for the query placeholders using the input data
            pstmt.setString(1, courseName);  // Set course name
            pstmt.setString(2, TeacherID);   // Set teacher id
            pstmt.setString(3, CourseID);   // Set course id

            // Execute the query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Edit committed successfully for course: " + courseName);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }

    }
    public static void applyCourse(String StudentID, String CourseID){
        // SQL query for inserting actor data into the database
        String query = "INSERT INTO engage.StudentsTakingCourses (StudentID, CourseID) VALUES (?, ?);";

        // Declare variables for database connection and prepared statement
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Disable auto-commit to manually handle transactions
            connection.setAutoCommit(false);

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Set the values for the query placeholders using the input data
            pstmt.setString(1, StudentID);  // Set student id
            pstmt.setString(2, CourseID);   // Set course id

            // Execute the query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Insertion committed successfully for student with ID: " + StudentID);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void assignCourse(String TeacherID, String CourseID){
        // SQL query for inserting actor data into the database
        String query = "UPDATE engage.Courses SET TeacherID = ? WHERE CourseID = ?;";

        // Declare variables for database connection and prepared statement
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Disable auto-commit to manually handle transactions
            connection.setAutoCommit(false);

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Set the values for the query placeholders using the input data
            pstmt.setString(1, TeacherID);  // Set teacher id
            pstmt.setString(2, CourseID);   // Set course id

            // Execute the query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Insertion committed successfully for teacher with ID: " + TeacherID);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void deleteCourse(String CourseID) {
        String deleteFromGrades = "DELETE FROM engage.Grades WHERE CourseID = ?;";
        String deleteFromStudentsTakingCourses = "DELETE FROM engage.StudentsTakingCourses WHERE CourseID = ?;";
        String deleteFromCourses = "DELETE FROM engage.Courses WHERE CourseID = ?;";

        Connection connection = null;
        PreparedStatement pstmtGrades = null;
        PreparedStatement pstmtStudents = null;
        PreparedStatement pstmtCourse = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Start transaction

            // Delete related records from Grades table
            pstmtGrades = connection.prepareStatement(deleteFromGrades);
            pstmtGrades.setString(1, CourseID);
            pstmtGrades.executeUpdate();

            // Delete related records from StudentsTakingCourses table
            pstmtStudents = connection.prepareStatement(deleteFromStudentsTakingCourses);
            pstmtStudents.setString(1, CourseID);
            pstmtStudents.executeUpdate();

            // Now delete the course itself
            pstmtCourse = connection.prepareStatement(deleteFromCourses);
            pstmtCourse.setString(1, CourseID);
            int rowsAffected = pstmtCourse.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Deletion committed successfully for course: " + CourseID);
            } else {
                connection.rollback();
                System.out.println("No course found with ID: " + CourseID + ". Transaction rolled back.");
            }

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                if (pstmtGrades != null) pstmtGrades.close();
                if (pstmtStudents != null) pstmtStudents.close();
                if (pstmtCourse != null) pstmtCourse.close();
                if (connection != null) connection.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    public static void deleteStudent(String StudentID) {
        // SQL statements to delete from related tables
        String deleteFromGrades = "DELETE FROM engage.Grades WHERE StudentID = ?;";
        String deleteFromStudentsTakingCourses = "DELETE FROM engage.StudentsTakingCourses WHERE StudentID = ?;";
        String deleteFromStudentLogin = "DELETE FROM engage.StudentLogin WHERE StudentID = ?;";
        String deleteFromStudents = "DELETE FROM engage.Students WHERE StudentID = ?;";

        Connection connection = null;
        PreparedStatement pstmtGrades = null;
        PreparedStatement pstmtStudentsTakingCourses = null;
        PreparedStatement pstmtStudentLogin = null;
        PreparedStatement pstmtStudent = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Start transaction

            // Delete related records from Grades table
            pstmtGrades = connection.prepareStatement(deleteFromGrades);
            pstmtGrades.setString(1, StudentID);
            pstmtGrades.executeUpdate();

            // Delete related records from StudentsTakingCourses table
            pstmtStudentsTakingCourses = connection.prepareStatement(deleteFromStudentsTakingCourses);
            pstmtStudentsTakingCourses.setString(1, StudentID);
            pstmtStudentsTakingCourses.executeUpdate();

            // Delete related records from StudentLogin table
            pstmtStudentLogin = connection.prepareStatement(deleteFromStudentLogin);
            pstmtStudentLogin.setString(1, StudentID);
            pstmtStudentLogin.executeUpdate();

            // Now delete the student from Students table
            pstmtStudent = connection.prepareStatement(deleteFromStudents);
            pstmtStudent.setString(1, StudentID);
            int rowsAffected = pstmtStudent.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Deletion committed successfully for student: " + StudentID);
            } else {
                connection.rollback();
                System.out.println("No student found with ID: " + StudentID + ". Transaction rolled back.");
            }

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                if (pstmtGrades != null) pstmtGrades.close();
                if (pstmtStudentsTakingCourses != null) pstmtStudentsTakingCourses.close();
                if (pstmtStudentLogin != null) pstmtStudentLogin.close();
                if (pstmtStudent != null) pstmtStudent.close();
                if (connection != null) connection.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    public static void deleteTeacher(String TeacherID) {
        String deleteFromGrades = "DELETE FROM engage.Grades WHERE CourseID IN (SELECT CourseID FROM engage.Courses WHERE TeacherID = ?);";
        String deleteFromStudentsTakingCourses = "DELETE FROM engage.StudentsTakingCourses WHERE CourseID IN (SELECT CourseID FROM engage.Courses WHERE TeacherID = ?);";
        String deleteFromCourses = "DELETE FROM engage.Courses WHERE TeacherID = ?;";
        String deleteFromTeacherLogin = "DELETE FROM engage.TeacherLogin WHERE TeacherID = ?;";
        String deleteFromTeachers = "DELETE FROM engage.Teachers WHERE TeacherID = ?;";

        Connection connection = null;
        PreparedStatement pstmtGrades = null;
        PreparedStatement pstmtStudentsTakingCourses = null;
        PreparedStatement pstmtCourses = null;
        PreparedStatement pstmtTeacherLogin = null;
        PreparedStatement pstmtTeacher = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Start transaction

            // Delete grades related to this teacher's courses
            pstmtGrades = connection.prepareStatement(deleteFromGrades);
            pstmtGrades.setString(1, TeacherID);
            pstmtGrades.executeUpdate();

            // Delete student enrollments in this teacher's courses
            pstmtStudentsTakingCourses = connection.prepareStatement(deleteFromStudentsTakingCourses);
            pstmtStudentsTakingCourses.setString(1, TeacherID);
            pstmtStudentsTakingCourses.executeUpdate();

            // Delete courses taught by this teacher
            pstmtCourses = connection.prepareStatement(deleteFromCourses);
            pstmtCourses.setString(1, TeacherID);
            pstmtCourses.executeUpdate();

            // Delete teacher's login info
            pstmtTeacherLogin = connection.prepareStatement(deleteFromTeacherLogin);
            pstmtTeacherLogin.setString(1, TeacherID);
            pstmtTeacherLogin.executeUpdate();

            // Finally, delete the teacher
            pstmtTeacher = connection.prepareStatement(deleteFromTeachers);
            pstmtTeacher.setString(1, TeacherID);
            int rowsAffected = pstmtTeacher.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Deletion committed successfully for teacher: " + TeacherID);
            } else {
                connection.rollback();
                System.out.println("No teacher found with ID: " + TeacherID + ". Transaction rolled back.");
            }

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                if (pstmtGrades != null) pstmtGrades.close();
                if (pstmtStudentsTakingCourses != null) pstmtStudentsTakingCourses.close();
                if (pstmtCourses != null) pstmtCourses.close();
                if (pstmtTeacherLogin != null) pstmtTeacherLogin.close();
                if (pstmtTeacher != null) pstmtTeacher.close();
                if (connection != null) connection.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    public static void removeStudentCourse(String CourseID, String StudentID){
        // SQL query for inserting actor data into the database
        String query = "DELETE FROM engage.StudentsTakingCourses WHERE StudentID = ? AND CourseID = ?;";

        // Declare variables for database connection and prepared statement
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Disable auto-commit to manually handle transactions
            connection.setAutoCommit(false);

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Set the values for the query placeholders using the input data
            pstmt.setString(1, StudentID);   // Set student id
            pstmt.setString(2, CourseID);   // Set course id

            // Execute the query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Removal committed successfully for student with ID: " + StudentID);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void removeTeacherCourse(String CourseID){
        // SQL query for inserting actor data into the database
        String query = "UPDATE engage.Courses SET TeacherID = NULL WHERE CourseID = ?;";

        // Declare variables for database connection and prepared statement
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Disable auto-commit to manually handle transactions
            connection.setAutoCommit(false);

            // Prepare the SQL query
            pstmt = connection.prepareStatement(query);

            // Set the values for the query placeholders using the input data
            pstmt.setString(1, CourseID);   // Set course id

            // Execute the query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // If the insertion is successful, commit the transaction
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Removal committed successfully for course with ID: " + CourseID);
            } else {
                // If insertion fails, roll back the transaction
                connection.rollback();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            try {
                if (connection == null) {
                    // If an error occurs, roll back the transaction
                    connection.rollback();
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                // Print stack trace if there is an error during rollback
                rollbackEx.printStackTrace();
            }
        }
    }
    public static boolean isValidUser(String user) {
        String[] tables = {
                "engage.StudentLogin",
                "engage.TeacherLogin",
                "engage.AdminLogin"
        };

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            for (String table : tables) {
                String query = "SELECT username FROM " + table + " WHERE username = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setString(1, user);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return false; // Username already exists in one of the tables
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error during username check: " + e.getMessage());
        }

        return true; // Username is available
    }
    public static User checkCredentials(String username, String password, String table) {
        String role = table.substring(7, table.length() - 5); // I remove "engage."  and "Login" from the table String to create the role String
        String query = "SELECT " + role + "ID, password FROM " + table + " WHERE username = ?";
        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = connection.prepareStatement(query)
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if(BCrypt.checkpw(password, hashedPassword)){  // compare input password with hashed one
                    return login(rs.getString(role + "ID"), role);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error during login: " + e.getMessage());
        }
        return null;  // user not found or error
    }
    public static ArrayList<String[]> executeQueryStudentsTableAdminWindow(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Reset the table columns before adding new ones
            adminWindow.studentModel.setColumnCount(0);  // Clear existing columns

            // ResultSetMetaData object provides detailed information about the columns in the result set.
            // This includes column names, types, and other attributes like whether a column is nullable, its size, etc.
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add colums automatically
            for (int i = 1; i <= columnCount; i++) {
                adminWindow.studentModel.addColumn(metaData.getColumnName(i)); // model from adminWindow.java
            }

            // to add rows.
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getString(i + 1);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return results;
    }
    public static ArrayList<String[]> executeQueryTeachersTableAdminWindow(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Reset the table columns before adding new ones
            adminWindow.teacherModel.setColumnCount(0);  // Clear existing columns

            // ResultSetMetaData object provides detailed information about the columns in the result set.
            // This includes column names, types, and other attributes like whether a column is nullable, its size, etc.
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add colums automatically
            for (int i = 1; i <= columnCount; i++) {
                adminWindow.teacherModel.addColumn(metaData.getColumnName(i)); // model from adminWindow.java
            }

            // to add rows.
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getString(i + 1);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return results;
    }
    public static ArrayList<String[]> executeQueryStudentsTableTeacherWindow(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Reset the table columns before adding new ones
            teacherWindow.studentModel.setColumnCount(0);  // Clear existing columns

            // ResultSetMetaData object provides detailed information about the columns in the result set.
            // This includes column names, types, and other attributes like whether a column is nullable, its size, etc.
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add colums automatically
            for (int i = 1; i <= columnCount; i++) {
                teacherWindow.studentModel.addColumn(metaData.getColumnName(i)); // model from teacherWindow.java
            }

            // to add rows.
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getString(i + 1);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return results;
    }
    public static ArrayList<String[]> executeQueryGradesTableTeacherWindow(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Reset the table columns before adding new ones
            teacherWindow.gradeModel.setColumnCount(0);  // Clear existing columns

            // ResultSetMetaData object provides detailed information about the columns in the result set.
            // This includes column names, types, and other attributes like whether a column is nullable, its size, etc.
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add colums automatically
            for (int i = 1; i <= columnCount; i++) {
                teacherWindow.gradeModel.addColumn(metaData.getColumnName(i)); // model from teacherWindow.java
            }

            // to add rows.
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getString(i + 1);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return results;
    }
    public static ArrayList<String[]> executeQueryGradesTableStudentWindow(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Reset the table columns before adding new ones
            studentWindow.gradeModel.setColumnCount(0);  // Clear existing columns

            // ResultSetMetaData object provides detailed information about the columns in the result set.
            // This includes column names, types, and other attributes like whether a column is nullable, its size, etc.
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add colums automatically
            for (int i = 1; i <= columnCount; i++) {
                studentWindow.gradeModel.addColumn(metaData.getColumnName(i)); // model from studentWindow.java
            }

            // to add rows.
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getString(i + 1);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return results;
    }
    public static ArrayList<String[]> executeQueryAverageTableStudentWindow(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Reset the table columns before adding new ones
            studentWindow.avgModel.setColumnCount(0);  // Clear existing columns

            // ResultSetMetaData object provides detailed information about the columns in the result set.
            // This includes column names, types, and other attributes like whether a column is nullable, its size, etc.
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add colums automatically
            for (int i = 1; i <= columnCount; i++) {
                studentWindow.avgModel.addColumn(metaData.getColumnName(i)); // model from studentWindow.java
            }

            // to add rows.
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getString(i + 1);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return results;
    }
    public static ArrayList<String[]> executeQueryCourses(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Reset the table columns before adding new ones
            courseWindow.courseModel.setColumnCount(0);  // Clear existing columns

            // ResultSetMetaData object provides detailed information about the columns in the result set.
            // This includes column names, types, and other attributes like whether a column is nullable, its size, etc.
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add colums automatically
            for (int i = 1; i <= columnCount; i++) {
                courseWindow.courseModel.addColumn(metaData.getColumnName(i)); // model from courseWindow.java
            }

            // to add rows.
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getString(i + 1);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return results;
    }
    public static ArrayList<String[]> executeQueryCourseList(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Reset the table columns before adding new ones
            editPersonalCourseList.courseListModel.setColumnCount(0);  // Clear existing columns

            // ResultSetMetaData object provides detailed information about the columns in the result set.
            // This includes column names, types, and other attributes like whether a column is nullable, its size, etc.
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add colums automatically
            for (int i = 1; i <= columnCount; i++) {
                editPersonalCourseList.courseListModel.addColumn(metaData.getColumnName(i)); // model from editPersonalCourseList.java
            }

            // to add rows.
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getString(i + 1);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return results;
    }
    public static boolean checkTeacherExists(String teacherId) {
        // SQL query to check if teacher with the given ID exists
        String query = "SELECT 1 FROM engage.Teachers WHERE TeacherID = ? LIMIT 1"; // LIMIT 1 for efficiency

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the teacher ID in the query
            pstmt.setString(1, teacherId);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // If a result is returned, teacher exists
            return rs.next(); // returns true if the teacher exists, false if no rows are returned
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean checkCourseExists(String courseId) {
        String query = "SELECT COUNT(*) FROM engage.Courses WHERE CourseID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, courseId);  // Set the course ID parameter

            // Execute the query and get the result
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // If the count is greater than 0, the course exists
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if there's an exception or no matching record
    }
    public static boolean isStudentEnrolled(String studentID, String courseID) {
        String query = "SELECT * FROM engage.StudentsTakingCourses WHERE StudentID = ? AND CourseID = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, studentID);
            pstmt.setString(2, courseID);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();  // If there is a result, student is enrolled
            }

        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
            return false;  // Assume not enrolled if error occurs
        }
    }
    public static User login(String id, String role) {
        User user = null;
        String query = "SELECT " + role + "ID, firstName, img FROM engage." + role + "s WHERE " + role + "ID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Get the image as a binary stream and store it
                    InputStream imgStream = rs.getBinaryStream("img");
                    user = new User(rs.getString(role + "ID"), rs.getString("firstName"), imgStream);
                }
            }
            return user;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return user;
    }
}
class User {
    private String userID;
    private String firstName;
    private InputStream img;  // Use InputStream for image data

    public User(String id, String first_name, InputStream img){
        this.userID = id;
        this.firstName = first_name;
        this.img = img;
    }

    public String getFirstName(){
        return firstName;
    }

    public ImageIcon getImageIcon() {
        try {
            // Convert InputStream to BufferedImage
            BufferedImage image = ImageIO.read(img);
            // Return ImageIcon from the BufferedImage
            return new ImageIcon(image);
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
            return null;
        }
    }

    public String getUserID() {
        return userID;
    }
}
