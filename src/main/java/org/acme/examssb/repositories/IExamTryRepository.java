package org.acme.examssb.repositories;

import org.acme.examssb.models.ExamTry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExamTryRepository extends JpaRepository<ExamTry, Long> {
}
