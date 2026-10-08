// ================================================================================
// PROGRAM 4: HikariCPConnectionPooling.java
// ================================================================================
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Demonstrates connection pooling with HikariCP.
 *
 * Required dependencies:
 * - MySQL Connector/J
 * - HikariCP
 *
 * Closing a pooled Connection returns it to the pool.
 * 
 * To run:
 * javac -cp ".;HikariCP-6.0.0.jar;slf4j-api-2.0.16.jar;slf4j-simple-2.0.16.jar;mysql-connector-j-26.7.0.jar" HikariCPConnectionPooling.java
 * java -cp ".;HikariCP-6.0.0.jar;slf4j-api-2.0.16.jar;slf4j-simple-2.0.16.jar;mysql-connector-j-26.7.0.jar" HikariCPConnectionPooling
 */
public class HikariCPConnectionPooling {

    public static void main(String[] args) {

        HikariConfig config = new HikariConfig();

        // config.setJdbcUrl("jdbc:mysql://localhost:3306/company_db"+ "?useSSL=false&serverTimezone=UTC");
         config.setJdbcUrl("jdbc:mysql://localhost:3306/company_db"
        + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");

        config.setUsername("root");
        config.setPassword("password");

        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10_000);

        try (HikariDataSource dataSource = new HikariDataSource(config)) {

            String sql = "SELECT COUNT(*) AS total FROM employees";

            /*
             * Borrow a connection from HikariCP.
             * Closing it returns it to the pool.
             */
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    System.out.println("Employee count: "+ resultSet.getInt("total"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Database/pool error: " + e.getMessage());
        }
    }
}
