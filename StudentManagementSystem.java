import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StudentManagementSystem {

    static final String URL =
            "jdbc:mysql://localhost:3308/studentdb";

    static final String USER = "root";

    static final String PASSWORD = "Ratan@123";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            while (true) {

                System.out.println("\n========================================");
                System.out.println("   STUDENT MANAGEMENT SYSTEM");
                System.out.println("========================================");
                System.out.println("1.  Add Student");
                System.out.println("2.  View All Students");
                System.out.println("3.  Update Student Marks");
                System.out.println("4.  Delete Student");
                System.out.println("5.  Search Student by ID");
                System.out.println("6.  Search Student by Name");
                System.out.println("7.  View Students Sorted by Marks");
                System.out.println("8.  View Class Statistics");
                System.out.println("9.  View Top 5 Students");
                System.out.println("10. Department-wise Analysis");
                System.out.println("11. Exit");
                System.out.println("========================================");

                System.out.print("Enter choice: ");

                int choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        addStudent();
                        break;

                    case 2:
                        viewStudents();
                        break;

                    case 3:
                        updateStudent();
                        break;

                    case 4:
                        deleteStudent();
                        break;

                    case 5:
                        searchStudent();
                        break;

                    case 6:
                        searchByName();
                        break;

                    case 7:
                        viewStudentsSortedByMarks();
                        break;

                    case 8:
                        showClassStatistics();
                        break;

                    case 9:
                        showTopStudents();
                        break;

                    case 10:
                        departmentAnalysis();
                        break;

                    case 11:
                        System.out.println("\nThank you for using Student Management System!");
                        return;

                    default:
                        System.out.println("Invalid Choice! Please try again.");
                }
            }
        } finally {
            sc.close();
        }
    }

    static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    static void addStudent() {

        try (Connection con = getConnection()) {

            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            
            if (name.trim().isEmpty()) {
                System.out.println("Error: Name cannot be empty");
                return;
            }

            System.out.print("Enter Department: ");
            String dept = sc.nextLine();
            
            if (dept.trim().isEmpty()) {
                System.out.println("Error: Department cannot be empty");
                return;
            }

            System.out.print("Enter Marks: ");
            int marks = sc.nextInt();
            
            if (marks < 0 || marks > 100) {
                System.out.println("Error: Marks must be between 0 and 100");
                return;
            }

            String query =
                    "INSERT INTO students(name, department, marks) VALUES (?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setInt(3, marks);

            ps.executeUpdate();

            String grade = getGrade(marks);
            System.out.println("\n✓ Student Added Successfully!");
            System.out.println("  Name: " + name);
            System.out.println("  Department: " + dept);
            System.out.println("  Marks: " + marks);
            System.out.println("  Grade: " + grade);

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewStudents() {

        try (Connection con = getConnection()) {

            String query = "SELECT * FROM students";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(query);

            System.out.println("\nID\tNAME\t\tDEPARTMENT\tMARKS\tGRADE");
            System.out.println("=========================================================");

            boolean found = false;

            while (rs.next()) {
                found = true;
                int marks = rs.getInt("marks");
                String grade = getGrade(marks);
                System.out.printf("%d\t%-15s\t%s\t%d\t%s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        marks,
                        grade);
            }

            if (!found) {
                System.out.println("No students found!");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updateStudent() {

        try (Connection con = getConnection()) {

            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();

            System.out.print("Enter New Marks: ");
            int marks = sc.nextInt();
            
            if (marks < 0 || marks > 100) {
                System.out.println("Error: Marks must be between 0 and 100");
                return;
            }

            String query =
                    "UPDATE students SET marks=? WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1, marks);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                String grade = getGrade(marks);
                System.out.println("\n✓ Student Marks Updated Successfully!");
                System.out.println("  Student ID: " + id);
                System.out.println("  New Marks: " + marks);
                System.out.println("  New Grade: " + grade);
            } else {
                System.out.println("Student Not Found");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void deleteStudent() {

        try (Connection con = getConnection()) {

            System.out.print("Enter Student ID to Delete: ");

            int id = sc.nextInt();

            // First, get student details for confirmation
            String selectQuery = "SELECT * FROM students WHERE id=?";
            PreparedStatement selectPs = con.prepareStatement(selectQuery);
            selectPs.setInt(1, id);
            ResultSet rs = selectPs.executeQuery();

            if (!rs.next()) {
                System.out.println("Student Not Found");
                return;
            }

            System.out.println("\nStudent to Delete:");
            System.out.println("  Name: " + rs.getString("name"));
            System.out.println("  Department: " + rs.getString("department"));
            System.out.println("  Marks: " + rs.getInt("marks"));

            System.out.print("\nAre you sure? (yes/no): ");
            sc.nextLine();
            String confirm = sc.nextLine();

            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            String deleteQuery = "DELETE FROM students WHERE id=?";
            PreparedStatement deletePs = con.prepareStatement(deleteQuery);
            deletePs.setInt(1, id);

            int rows = deletePs.executeUpdate();

            if (rows > 0) {
                System.out.println("\n✓ Student Deleted Successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void searchStudent() {

        try (Connection con = getConnection()) {

            System.out.print("Enter ID: ");

            int id = sc.nextInt();

            String query =
                    "SELECT * FROM students WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\n--- Student Found ---");
                displayStudentWithGrade(rs);

            } else {
                System.out.println("Student Not Found");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void searchByName() {

        try (Connection con = getConnection()) {

            sc.nextLine();
            System.out.print("Enter Student Name (partial match): ");
            String name = sc.nextLine();

            String query = "SELECT * FROM students WHERE name LIKE ? ORDER BY marks DESC";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + name + "%");

            ResultSet rs = ps.executeQuery();

            boolean found = false;
            System.out.println("\nID\tNAME\t\tDEPARTMENT\tMARKS\tGRADE");
            System.out.println("=========================================================");

            while (rs.next()) {
                found = true;
                int marks = rs.getInt("marks");
                String grade = getGrade(marks);
                System.out.printf("%d\t%-15s\t%s\t%d\t%s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        marks,
                        grade);
            }

            if (!found) {
                System.out.println("No students found with that name.");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewStudentsSortedByMarks() {

        try (Connection con = getConnection()) {

            String query = "SELECT * FROM students ORDER BY marks DESC";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\n--- Students Sorted by Marks (Highest to Lowest) ---");
            System.out.println("ID\tNAME\t\tDEPARTMENT\tMARKS\tGRADE");
            System.out.println("=========================================================");

            while (rs.next()) {
                int marks = rs.getInt("marks");
                String grade = getGrade(marks);
                System.out.printf("%d\t%-15s\t%s\t%d\t%s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        marks,
                        grade);
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void showClassStatistics() {

        try (Connection con = getConnection()) {

            String query = "SELECT COUNT(*) as total, AVG(marks) as avg_marks, " +
                    "MAX(marks) as max_marks, MIN(marks) as min_marks FROM students";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                System.out.println("\n=== CLASS STATISTICS ===");
                int total = rs.getInt("total");
                double avgMarks = rs.getDouble("avg_marks");
                int maxMarks = rs.getInt("max_marks");
                int minMarks = rs.getInt("min_marks");

                if (total == 0) {
                    System.out.println("No students found!");
                } else {
                    System.out.printf("Total Students: %d\n", total);
                    System.out.printf("Average Marks: %.2f\n", avgMarks);
                    System.out.printf("Highest Marks: %d\n", maxMarks);
                    System.out.printf("Lowest Marks: %d\n", minMarks);
                    System.out.printf("Class Grade Average: %s\n", getGrade((int) avgMarks));
                }
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void showTopStudents() {

        try (Connection con = getConnection()) {

            String query = "SELECT * FROM students ORDER BY marks DESC LIMIT 5";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\n=== TOP 5 STUDENTS ===");
            System.out.println("RANK\tNAME\t\tDEPARTMENT\tMARKS\tGRADE");
            System.out.println("=========================================================");

            int rank = 1;
            boolean found = false;

            while (rs.next()) {
                found = true;
                int marks = rs.getInt("marks");
                String grade = getGrade(marks);
                System.out.printf("%d\t%-15s\t%s\t%d\t%s\n",
                        rank++,
                        rs.getString("name"),
                        rs.getString("department"),
                        marks,
                        grade);
            }

            if (!found) {
                System.out.println("No students found!");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void departmentAnalysis() {

        try (Connection con = getConnection()) {

            String query = "SELECT department, COUNT(*) as count, AVG(marks) as avg_marks, " +
                    "MAX(marks) as max_marks FROM students GROUP BY department ORDER BY avg_marks DESC";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\n=== DEPARTMENT-WISE ANALYSIS ===");
            System.out.println("DEPARTMENT\t\tSTUDENTS\tAVG MARKS\tHIGHEST MARKS");
            System.out.println("=============================================================");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.printf("%-20s\t%d\t\t%.2f\t%d\n",
                        rs.getString("department"),
                        rs.getInt("count"),
                        rs.getDouble("avg_marks"),
                        rs.getInt("max_marks"));
            }

            if (!found) {
                System.out.println("No data found!");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static String getGrade(int marks) {
        if (marks >= 90) return "A";
        if (marks >= 80) return "B";
        if (marks >= 70) return "C";
        if (marks >= 60) return "D";
        return "F";
    }

    static void displayStudentWithGrade(ResultSet rs) throws SQLException {
        int marks = rs.getInt("marks");
        String grade = getGrade(marks);

        System.out.printf("ID: %d\n", rs.getInt("id"));
        System.out.printf("Name: %s\n", rs.getString("name"));
        System.out.printf("Department: %s\n", rs.getString("department"));
        System.out.printf("Marks: %d\n", marks);
        System.out.printf("Grade: %s\n", grade);
    }
}
