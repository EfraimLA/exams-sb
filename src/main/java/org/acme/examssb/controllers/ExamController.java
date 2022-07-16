package org.acme.examssb.controllers;

import org.acme.examssb.models.Exam;
import org.acme.examssb.repositories.IExamRepository;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private final IExamRepository repository;

    ExamController(IExamRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    List<Exam> getExams() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Exam getExamById(@PathParam("id") final Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    Exam createExam(Exam exam) {
        return repository.save(exam);
    }

    @PutMapping
    Exam updateExam(Exam exam) {
        return repository.save(exam);
    }

    @DeleteMapping("/{id}")
    void deleteExam(@PathParam("id") final Long id) {
        repository.deleteById(id);
    }

}
