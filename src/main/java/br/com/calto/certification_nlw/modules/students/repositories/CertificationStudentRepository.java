package br.com.calto.certification_nlw.modules.students.repositories;

import br.com.calto.certification_nlw.modules.students.entities.CertificationStudentEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CertificationStudentRepository
  extends JpaRepository<CertificationStudentEntity, UUID> {
  @Query(
    "SELECT c FROM certifications c INNER JOIN c.studentEntity std WHERE std.email= :email AND c.technology = :technology"
  )
  List<CertificationStudentEntity> findByStudentEmailAndTechnology(
    String email,
    String technology
  );

  @Query("SELECT c FROM certifications c ORDER BY c.grate DESC LIMIT 10")
  List<CertificationStudentEntity> findTop10ByOrderByGrateDesc();
}
