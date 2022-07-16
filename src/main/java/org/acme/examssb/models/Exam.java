package org.acme.examssb.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

}
