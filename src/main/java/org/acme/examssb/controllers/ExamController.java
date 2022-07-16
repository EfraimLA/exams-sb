package org.acme.examssb.controllers;

import org.acme.examssb.models.Exam;
import org.acme.examssb.models.ExamTry;
import org.acme.examssb.repositories.IExamRepository;
import org.acme.examssb.repositories.IExamTryRepository;
import org.acme.examssb.repositories.IStudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamController.class);

    private final IExamRepository repository;

    private final IExamTryRepository examTryRepository;

    private final IStudentRepository studentRepository;

    ExamController(IExamRepository repository, IExamTryRepository examTryRepository, IStudentRepository studentRepository) {
        this.repository = repository;
        this.examTryRepository = examTryRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    List<Exam> getExams() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Exam getExamById(@PathParam("id") Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    ResponseEntity<Exam> createExam(@RequestBody @Valid Exam exam) {
        var sum = exam.getQuestions()
                .stream()
                .map(question -> question.getValue())
                .reduce(0, Integer::sum)
                .compareTo(100);

        if (sum != 0) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(repository.save(exam));
    }

    @PostMapping("/try/{id}")
    ResponseEntity<ExamTry> examTry(@PathVariable("id") Long id,
                                    @RequestParam("student") Long studentId,
                                    @RequestBody @Valid ExamTry examTry) {
        var exam = repository.findById(id).orElse(null);
        var student = studentRepository.findById(studentId).orElse(null);

        if (exam == null | student == null) return ResponseEntity.notFound().build();

        examTry.setExam(exam);
        examTry.setStudent(student);

        return ResponseEntity.ok(examTryRepository.save(examTry));
    }

    @PutMapping
    Exam updateExam(@RequestBody @Valid Exam exam) {
        return repository.save(exam);
    }

    @DeleteMapping("/{id}")
    void deleteExam(@PathParam("id") final Long id) {
        repository.deleteById(id);
    }

}
