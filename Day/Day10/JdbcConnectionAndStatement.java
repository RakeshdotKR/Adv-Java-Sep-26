// package Day.Day10;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * JDBC basics with MySQL.
 *
 * Before running:
 * 1. Start MySQL.
 * 2. Create a database named company_db.
 * 3. Update DB_USER and DB_PASSWORD.
 * 4. Add MySQL Connector/J to the classpath.
 */
public class JdbcConnectionAndStatement {

    private static final String DB_URL ="jdbc:mysql://localhost:3306/company_db"
            + "?useSSL=false&serverTimezone=UTC";

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public static void main(String[] args) {

        String sql = "SELECT id, name, salary FROM employees";

        /*
         * try-with-resources closes ResultSet, Statement and Connection
         * automatically, even when an exception occurs.
         */
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Connected to MySQL.");

            while (resultSet.next()) {
                System.out.println(
                        resultSet.getInt("id")
                        + " | "
                        + resultSet.getString("name")
                        + " | "
                        + resultSet.getDouble("salary"));
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
