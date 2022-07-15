package org.acme.examssb.repositories;

import org.acme.examssb.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository<Student, Long> {
}
