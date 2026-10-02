// PROGRAM 2: PreparedStatementDemo.java
// ================================================================================

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Demonstrates PreparedStatement with MySQL.
 *
 * PreparedStatement is preferred when values come from users,
 * files, APIs or other runtime sources.
 */
public class PreparedStatementDemo {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/company_db"
            + "?useSSL=false&serverTimezone=UTC";

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public static void main(String[] args) {

        String sql =
                "SELECT id, name, salary "
                + "FROM employees "
                + "WHERE salary >= ?";

        double minimumSalary = 80000;

        try (Connection connection =
                     DriverManager.getConnection(
                             DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Bind the runtime value to the ? placeholder.
            statement.setDouble(1, minimumSalary);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    System.out.println(
                            resultSet.getInt("id")
                            + " | "
                            + resultSet.getString("name")
                            + " | "
                            + resultSet.getDouble("salary"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
