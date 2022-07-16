package org.acme.examssb.models;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String question;

    @NotNull
    @Min(1)
    private Integer value;

    @Range(min = 0, max = 3) // Between 4 answers | Zero index
    private Integer correctAnswer;

    @ElementCollection
    @Size(min = 4, max = 4, message = "Must be 4 answers") // Must be 4 answers
    private List<String> answers;

}
