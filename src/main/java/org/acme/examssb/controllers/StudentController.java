package org.acme.examssb.controllers;

import org.acme.examssb.models.Student;
import org.acme.examssb.repositories.IStudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

    private final IStudentRepository repository;

    StudentController(IStudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    List<Student> getStudents() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<Student> getStudentById(@PathVariable("id") Long id) {
        var student = repository.findById(id).orElse(null);

        if (student == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(student);
    }

    @PostMapping
    Student createStudent(@RequestBody @Valid Student student) {
        return repository.save(student);
    }

    @PutMapping("/{id}")
    Student updateStudent(@PathVariable("id") Long id, @RequestBody @Valid Student student) {
        var st = repository.findById(id).orElse(null);

        assert st != null;

        st.setName(student.getName());
        st.setAge(student.getAge());
        st.setCity(student.getCity());
        st.setTimezone(student.getTimezone());

        return st;
    }

    @DeleteMapping("/{id}")
    void deleteStudent(@PathVariable("id") Long id) {
        repository.deleteById(id);
    }

}
