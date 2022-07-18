package org.acme.examssb.controllers;

import org.acme.examssb.models.Student;
import org.acme.examssb.repositories.IStudentRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.acme.examssb.utils.Utils.isValidTimezone;

@RestController
@RequestMapping("/students")
public class StudentController {

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

        return ResponseEntity.ok(student);
    }

    @PostMapping
    ResponseEntity<?> createStudent(@RequestBody @Valid Student student) {
        var timezone = student.getTimezone();

        // Validates timezone id is valid or default to UTC
        if (!isValidTimezone(timezone)) {
            return ResponseEntity.badRequest().body("Invalid timezone: " + timezone);
        }

        return ResponseEntity.ok(repository.save(student));
    }

    @PutMapping("/{id}")
    ResponseEntity<Student> updateStudent(@PathVariable("id") Long id, @RequestBody @Valid Student student) {
        var st = repository.findById(id).orElse(null);

        if (st == null) return ResponseEntity.notFound().build();

        st.setName(student.getName());
        st.setAge(student.getAge());
        st.setCity(student.getCity());
        st.setTimezone(student.getTimezone());

        return ResponseEntity.ok(st);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteStudent(@PathVariable("id") Long id) {
        try {
            repository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
