package org.acme.examssb.repositories;

import org.acme.examssb.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IQuestionRepository extends JpaRepository<Question, Long> {
}
