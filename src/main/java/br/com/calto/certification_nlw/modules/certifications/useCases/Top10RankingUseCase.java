package br.com.calto.certification_nlw.modules.certifications.useCases;

import br.com.calto.certification_nlw.modules.students.entities.CertificationStudentEntity;
import br.com.calto.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Top10RankingUseCase {

  @Autowired
  private CertificationStudentRepository certificationStudentRepository;

  public List<CertificationStudentEntity> execute() {
    return this.certificationStudentRepository.findTop10ByOrderByGrateDesc();
  }
}
