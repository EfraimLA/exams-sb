package org.acme.examssb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false,
            cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    private Question question;

    @JsonProperty("question_id")
    @Transient
    private Long questionId;

    @NotNull
    @Range(min = 0, max = 3) // Between 4 answers | Zero index
    private Integer answer;

}
