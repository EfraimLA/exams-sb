package org.acme.examssb.repositories;

import org.acme.examssb.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExamRepository extends JpaRepository<Exam, Long> {
}
