import java.sql.*;
import java.util.Scanner;

public class StudentManagement {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentDB?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";


    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n--- Student Management System ---");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addStudent(connection, scanner);
                        break;
                    case 2:
                        viewStudents(connection);
                        break;
                    case 3:
                        updateStudent(connection, scanner);
                        break;
                    case 4:
                        deleteStudent(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting the system. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter student name: ");
        scanner.nextLine(); // Consume newline
        String name = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age = scanner.nextInt();
        System.out.print("Enter student grade: ");
        scanner.nextLine(); // Consume newline
        String grade = scanner.nextLine();
        System.out.print("Enter student email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO students (name, age, grade, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, grade);
            statement.setString(4, email);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Student added successfully!");
            }
        }
    }

    private static void viewStudents(Connection connection) throws SQLException {
        String sql = "SELECT * FROM students";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("\n--- Student List ---");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String grade = resultSet.getString("grade");
                String email = resultSet.getString("email");
                System.out.printf("ID: %d, Name: %s, Age: %d, Grade: %s, Email: %s%n", id, name, age, grade, email);
            }
        }
    }

    private static void updateStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter student ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Enter new age: ");
        int age = scanner.nextInt();
        System.out.print("Enter new grade: ");
        scanner.nextLine(); // Consume newline
        String grade = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();

        String sql = "UPDATE students SET name = ?, age = ?, grade = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, grade);
            statement.setString(4, email);
            statement.setInt(5, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student not found.");
            }
        }
    }

    private static void deleteStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter student ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found.");
            }
        }
    }
}
