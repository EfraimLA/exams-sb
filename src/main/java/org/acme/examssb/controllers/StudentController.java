package org.acme.examssb.controllers;

import org.acme.examssb.models.Student;
import org.acme.examssb.repositories.IStudentRepository;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final IStudentRepository repository;

    StudentController(IStudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    List<Student> getAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Student getStudentById(@PathParam("id") final Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    Student createStudent(Student student) {
        return repository.save(student);
    }

    @PutMapping("/{id}")
    Student updateStudent(@PathParam("id") final Long id, Student student) {
        return repository.save(student);
    }

    @DeleteMapping("/{id}")
    void deleteStudent(@PathParam("id") final Long id) {
        repository.deleteById(id);
    }

}
