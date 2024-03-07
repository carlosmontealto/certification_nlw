package br.com.calto.certification_nlw.modules.students.repositories;

import br.com.calto.certification_nlw.modules.students.entities.StudentEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {
  public Optional<StudentEntity> findByEmail(String email);
}
