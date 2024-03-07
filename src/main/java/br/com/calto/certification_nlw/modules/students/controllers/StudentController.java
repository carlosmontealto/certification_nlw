package br.com.calto.certification_nlw.modules.students.controllers;

import br.com.calto.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import br.com.calto.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import br.com.calto.certification_nlw.modules.students.useCases.StudentCertificationAnswersUseCase;
import br.com.calto.certification_nlw.modules.students.useCases.VerifyIfHasCertificationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

  // Preciso usar o meu UseCase
  @Autowired
  private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

  @Autowired
  private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;

  @PostMapping("/verifyIfHasCertification")
  public String verifyIfHasCertification(
    @RequestBody VerifyHasCertificationDTO dto
  ) {
    var result = this.verifyIfHasCertificationUseCase.execute(dto);
    if (result) {
      return "usuario já fez a prova";
    }
    return "usuario pode fazer a prova";
  }

  @PostMapping("/certification/answer")
  public ResponseEntity<Object> certificationAnswer(
    @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO
  ) {
    try {
      var result =
        this.studentCertificationAnswersUseCase.execute(
            studentCertificationAnswerDTO
          );
      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
