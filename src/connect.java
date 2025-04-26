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
