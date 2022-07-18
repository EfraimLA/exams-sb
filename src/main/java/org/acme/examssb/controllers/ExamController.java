package org.acme.examssb.controllers;

import org.acme.examssb.models.Exam;
import org.acme.examssb.models.ExamTry;
import org.acme.examssb.models.Question;
import org.acme.examssb.models.Score;
import org.acme.examssb.repositories.IExamRepository;
import org.acme.examssb.repositories.IExamTryRepository;
import org.acme.examssb.repositories.IQuestionRepository;
import org.acme.examssb.repositories.IStudentRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.acme.examssb.utils.Utils.formatDateTime;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final IExamRepository repository;

    private final IExamTryRepository examTryRepository;

    private final IStudentRepository studentRepository;

    private final IQuestionRepository questionRepository;

    ExamController(IExamRepository repository,
                   IExamTryRepository examTryRepository,
                   IStudentRepository studentRepository,
                   IQuestionRepository questionRepository) {
        this.repository = repository;
        this.examTryRepository = examTryRepository;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping
    List<Exam> getExams() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<Exam> getExamById(@PathVariable("id") Long id) {
        var exam = repository.findById(id).orElse(null);

        if (exam == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(exam);
    }

    @PostMapping
    ResponseEntity<?> createExam(@RequestBody @Valid Exam exam) {
        var now = Instant.now();

        if (exam.getAvailableAt() != null &&
                exam.getAvailableAt().isBefore(now)) {
            return ResponseEntity.badRequest().body("Exam cannot be available in the past");
        }

        if (exam.getDeadline() != null &&
                exam.getDeadline().isBefore(now)) {
            return ResponseEntity.badRequest().body("Exam cannot be closed in the past");
        }

        if (exam.getAvailableAt() != null && exam.getDeadline() != null &&
                exam.getAvailableAt().isAfter(exam.getDeadline())) {
            return ResponseEntity.badRequest().body("Exam cannot be available after it is closed");
        }

        // Validate question's total value equal to 100
        var sum = exam.getQuestions()
                .stream()
                .map(Question::getValue)
                .reduce(0, Integer::sum)
                .compareTo(100);

        if (sum != 0) return ResponseEntity.badRequest().build();

        exam.setQuestions(
                exam.getQuestions()
                        .stream()
                        .peek(question -> question.setExam(exam))
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(repository.save(exam));
    }

    @PostMapping("/try/{id}")
    ResponseEntity<?> examTry(@PathVariable("id") Long id,
                              @RequestParam("student") Long studentId,
                              @RequestBody @Valid ExamTry examTry) {

        // Gets exam and student by specified ids
        var exam = repository.findById(id).orElse(null);
        var student = studentRepository.findById(studentId).orElse(null);

        if (exam == null | student == null) return ResponseEntity.notFound().build();

        // Validates exam is open
        var now = Instant.now();

        if (exam.getAvailableAt() != null &&
                exam.getAvailableAt().isAfter(now)) {
            var studentTimezone = ZoneId.of(student.getTimezone());
            var map = new HashMap<String, Object>();

            map.put("message", "Exam is not available yet, it will be available at " + formatDateTime(exam.getAvailableAt().atZone(studentTimezone)));

            return ResponseEntity.badRequest().body(map);
        }

        if (exam.getDeadline() != null &&
                exam.getDeadline().isBefore(now)) {
            var studentTimezone = ZoneId.of(student.getTimezone());
            var map = new HashMap<String, Object>();

            map.put("message", "Exam was closed at " + formatDateTime(exam.getDeadline().atZone(studentTimezone)));

            return ResponseEntity.badRequest().body(map);
        }

        examTry.setExam(exam);
        examTry.setStudent(student);

        try {
            examTry.setAnswers(
                    examTry.getAnswers()
                            .stream()
                            .peek(answer -> {

                                var question = questionRepository.findById(answer.getQuestionId())
                                        .orElseThrow(() -> new ValidationException("Question not found"));

                                var qExam = question.getExam();

                                // Verifies questions are from the same exam
                                if (qExam == null) throw new ValidationException("Exam not found");
                                else if (!qExam.getId().equals(id))
                                    throw new ValidationException("Question's exam does not match");

                                answer.setQuestion(question);

                            }).collect(Collectors.toList())
            );
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().build();
        }

        examTryRepository.save(examTry);

        var score = new Score();
        score.setStudent(student);
        score.setAnswers(examTry.getAnswers());

        score.setScore(
                examTry.getAnswers()
                        .stream()
                        .map(answer -> {
                            var question = answer.getQuestion();

                            // Verifies answer is correct (by index)
                            if (question.getCorrectAnswer().equals(answer.getAnswer()))
                                return question.getValue();

                            return 0;
                        }).mapToInt(Integer::intValue).sum()
        );

        return ResponseEntity.ok(score);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteExam(@PathVariable("id") Long id) {
        try {
            repository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
