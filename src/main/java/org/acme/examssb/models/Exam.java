package org.acme.examssb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("available_at")
    private Date availableAt;

    private Date deadline;

    @NotNull(message = "Questions are required")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "exam")
    @Valid
    private List<Question> questions;

}
