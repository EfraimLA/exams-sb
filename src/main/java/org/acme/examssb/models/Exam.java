package org.acme.examssb.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String instructions;

    private Date availableAt;

    private Date deadline;

    @NotNull(message = "Questions are required")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Question> questions;

}
