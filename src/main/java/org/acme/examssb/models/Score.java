package org.acme.examssb.models;

import lombok.Data;

import java.util.List;

@Data
public class Score {

    private Integer score = 0;

    private Student student;

    private List<Answer> answers;

}
