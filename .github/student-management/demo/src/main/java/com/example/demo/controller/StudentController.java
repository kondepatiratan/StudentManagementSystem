package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {

    private final StudentService service;

    // Constructor Injection
    public StudentController(StudentService service) {
        this.service = service;
    }

    // Display Home Page
    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("students", service.getAllStudents());

        return "index";
    }

    // Open Add Student Page
    @GetMapping("/add")
    public String addStudent(Model model) {

        model.addAttribute("student", new Student());

        return "addStudent";
    }

    // Save Student
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student) {

        service.saveStudent(student);

        return "redirect:/";
    }

    // Open Edit Page
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Integer id, Model model) {

        Student student = service.getStudentById(id);

        model.addAttribute("student", student);

        return "editStudent";
    }

    // Delete Student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id) {

        service.deleteStudent(id);

        return "redirect:/";
    }
    @GetMapping("/search")
public String searchStudent(@RequestParam("keyword") String keyword,
                            Model model) {

    model.addAttribute("students",
            service.searchStudent(keyword));

    return "index";
}
}