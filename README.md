# Student Management System

A comprehensive Java-based Student Management System with MySQL database integration. This application allows you to manage student records with advanced analytics and reporting features.

## Features

✨ **Core Operations:**
- ✅ Add new students
- ✅ View all students with grades
- ✅ Update student marks
- ✅ Delete student records
- ✅ Search students by ID
- ✅ Search students by name

📊 **Analytics & Reports:**
- 📈 View students sorted by performance (highest to lowest marks)
- 📊 Class-wide statistics (average marks, highest/lowest)
- 🏆 Top 5 performing students with rankings
- 🏢 Department-wise analysis and performance metrics
- 📋 Automatic grade calculation (A-F grading system)

🔒 **Features:**
- Input validation for all student data
- Confirmation before deletion
- Proper exception handling with meaningful error messages
- Formatted output tables for easy readability
- Grade system: A (90+), B (80+), C (70+), D (60+), F (<60)

## Requirements

- Java 8 or higher
- MySQL Server
- MySQL JDBC Driver (mysql-connector-j-9.6.0)

## Database Setup

Create a MySQL database with the following schema:

```sql
CREATE DATABASE studentdb;

USE studentdb;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    marks INT NOT NULL CHECK (marks >= 0 AND marks <= 100)
);
```

## Configuration

Update the following credentials in `StudentManagementSystem.java`:

```java
static final String URL = "jdbc:mysql://localhost:3306/studentdb";
static final String USER = "root";
static final String PASSWORD = "your_password";
```

## How to Run

1. Compile the Java file:
```bash
javac src/StudentManagementSystem.java
```

2. Run the application:
```bash
java -cp lib/mysql-connector-j-9.6.0/mysql-connector-j-9.6.0/mysql-connector-j-9.6.0.jar;src StudentManagementSystem
```

## Menu Options

1. **Add Student** - Insert a new student with name, department, and marks
2. **View All Students** - Display all students with their grades
3. **Update Student Marks** - Modify student marks by ID
4. **Delete Student** - Remove a student record with confirmation
5. **Search by ID** - Find a student and display detailed information
6. **Search by Name** - Find students by partial name match
7. **View Sorted by Marks** - List students ranked by performance
8. **View Class Statistics** - Get overall class analytics
9. **View Top 5 Students** - See the best performing students
10. **Department Analysis** - View department-wise performance metrics
11. **Exit** - Close the application

## Project Structure

```
StudentManagementSystem/
├── src/
│   └── StudentManagementSystem.java
├── lib/
│   └── mysql-connector-j-9.6.0/
└── README.md
```

## Error Handling

The application includes comprehensive error handling:
- Database connection errors
- Invalid input validation
- Student not found scenarios
- SQL exception handling with detailed messages

## Author

Student Management System - Java CRUD Application

## License

MIT License
