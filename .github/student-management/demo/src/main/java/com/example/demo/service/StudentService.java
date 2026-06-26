package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Student;

public interface StudentService {

    List<Student> getAllStudents();

    void saveStudent(Student student);

    Student getStudentById(Integer id);

    void deleteStudent(Integer id);

    List<Student> searchStudent(String keyword);
}