// PROGRAM 5: DaoPatternResultSetMapping.java
// ================================================================================

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Demonstrates a compact DAO design.
 *
 * Application
 *     ↓
 * EmployeeDao
 *     ↓
 * JDBC
 *     ↓
 * MySQL


javac -cp ".;HikariCP-6.0.0.jar;slf4j-api-2.0.16.jar;slf4j-simple-2.0.16.jar;mysql-connector-j-26.7.0.jar" DaoPatternResultSetMapping.java        

java -cp ".;HikariCP-6.0.0.jar;slf4j-api-2.0.16.jar;slf4j-simple-2.0.16.jar;mysql-connector-j-26.7.0.jar" DaoPatternResultSetMapping    
 */
public class DaoPatternResultSetMapping {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/company_db"+ "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
   
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    // Domain object.
    static class Employee {

        private final int id;
        private final String name;
        private final double salary;

        Employee(int id, String name, double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id
                    + ", name='" + name + '\''
                    + ", salary=" + salary
                    + '}';
        }
    }

    // DAO contract hides database-access details from callers.
    interface EmployeeDao {

        List<Employee> findAll() throws SQLException;

        Employee findById(int id) throws SQLException;
    }

    // JDBC implementation of the DAO.
    static class EmployeeDaoJdbc implements EmployeeDao {

        @Override
        public List<Employee> findAll() throws SQLException {

            String sql = "SELECT id, name, salary " + "FROM employees ORDER BY id";

            List<Employee> employees = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    // One ResultSet row becomes one Employee object.
                    employees.add(mapRow(resultSet));
                }
            }

            return employees;
        }

        @Override
        public Employee findById(int id) throws SQLException {

            String sql = "SELECT id, name, salary "+ "FROM employees WHERE id = ?";

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        return mapRow(resultSet);
                    }

                    return null;
                }
            }
        }

        // Centralized ResultSet → Employee mapping.
        private Employee mapRow(ResultSet resultSet)throws SQLException {

            return new Employee(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("salary"));
        }
    }

    public static void main(String[] args) {

        EmployeeDao employeeDao = new EmployeeDaoJdbc();

        try {
            System.out.println("All employees:");

            for (Employee employee : employeeDao.findAll()) {
                System.out.println(employee);
            }

            System.out.println("\nEmployee with ID 1:");

            Employee employee = employeeDao.findById(1);

            System.out.println(employee != null? employee: "Employee not found.");

        } catch (SQLException e) {
            System.err.println("DAO/database error: " + e.getMessage());
        }
    }
}