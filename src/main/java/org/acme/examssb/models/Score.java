package org.acme.examssb.models;

import lombok.Data;

@Data
public class Score {

    private Integer score;

    private Student student;

    private Exam exam;

    public void setExam(Exam exam) {
        exam.setQuestions(null);

        this.exam = exam;
    }
}
