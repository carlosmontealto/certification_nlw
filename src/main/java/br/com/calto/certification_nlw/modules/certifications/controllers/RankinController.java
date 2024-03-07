package br.com.calto.certification_nlw.modules.certifications.controllers;

import br.com.calto.certification_nlw.modules.certifications.useCases.Top10RankingUseCase;
import br.com.calto.certification_nlw.modules.students.entities.CertificationStudentEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ranking")
public class RankinController {

  @Autowired
  private Top10RankingUseCase top10RankingUseCase;

  @GetMapping("/top10")
  public List<CertificationStudentEntity> top10() {
    List<CertificationStudentEntity> result =
      this.top10RankingUseCase.execute();
    return result;
  }
}
