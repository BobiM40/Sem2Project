import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.sql.*;

public class connect {
    private static final String URL = "jdbc:mysql://localhost:3306/engage";
    private static final String USER = "root";
    private static final String PASSWORD = "0000";

    public static void addUser(String username, String firstName, String lastName, String email, String hashPassword, byte role) {

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

        String query = "INSERT INTO " + table + " (firstName, lastName, email) VALUES (?, ?, ?)";

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
    public static boolean checkCredentials(String username, String password, String table) {
        String query = "SELECT password FROM " + table + " WHERE username = ?";
        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = connection.prepareStatement(query)
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(password, hashedPassword);  // compare input password with hashed one
            }
        } catch (SQLException e) {
            System.out.println("SQL Error during login: " + e.getMessage());
        }
        return false;  // user not found or error
    }
}
