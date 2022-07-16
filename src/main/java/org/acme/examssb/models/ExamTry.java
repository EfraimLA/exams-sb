package org.acme.examssb.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "exam_tries")
@Entity
@Data
public class ExamTry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    private Student student;

    @OneToOne
    @NotNull
    private Exam exam;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<Answer> questionAnswers;

}
