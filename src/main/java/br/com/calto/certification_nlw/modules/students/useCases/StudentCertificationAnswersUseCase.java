package br.com.calto.certification_nlw.modules.students.useCases;

import br.com.calto.certification_nlw.modules.questions.entities.QuestionEntity;
import br.com.calto.certification_nlw.modules.questions.repositories.QuestionRepository;
import br.com.calto.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import br.com.calto.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import br.com.calto.certification_nlw.modules.students.entities.AnswerCertificationsEntity;
import br.com.calto.certification_nlw.modules.students.entities.CertificationStudentEntity;
import br.com.calto.certification_nlw.modules.students.entities.StudentEntity;
import br.com.calto.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import br.com.calto.certification_nlw.modules.students.repositories.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentCertificationAnswersUseCase {

  @Autowired
  private CertificationStudentRepository certificatationStudentRepository;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

  public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto)
    throws Exception {
    var hasCertification = verifyIfHasCertificationUseCase.execute(
      new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology())
    );
    if (hasCertification) {
      throw new Exception("Você já tirou sua certificação");
    }

    //Buscar as alternativas das peguntas
    List<QuestionEntity> questionsEntity = questionRepository.findByTechnology(
      dto.getTechnology()
    );
    List<AnswerCertificationsEntity> answersCertifications = new ArrayList<>();

    AtomicInteger correctAnswer = new AtomicInteger(0);
    // Correta ou Incorreta

    dto
      .getQuestionAnswer()
      .stream()
      .forEach(questionAnswer -> {
        var question = questionsEntity
          .stream()
          .filter(q -> q.getId().equals(questionAnswer.getQuestionId()))
          .findFirst()
          .get();

        var findCorrectAlternative = question
          .getAlternatives()
          .stream()
          .filter(alternative -> alternative.isCorrect())
          .findFirst()
          .get();

        if (
          findCorrectAlternative
            .getId()
            .equals(questionAnswer.getAlternativeId())
        ) {
          questionAnswer.setCorrect(true);
          correctAnswer.incrementAndGet();
        } else {
          questionAnswer.setCorrect(false);
        }

        var answersCertificationEntity = AnswerCertificationsEntity
          .builder()
          .answerId(questionAnswer.getAlternativeId())
          .questionId(questionAnswer.getQuestionId())
          .isCorrect(questionAnswer.isCorrect())
          .build();

        answersCertifications.add(answersCertificationEntity);
      });

    //verificar se existe Student pelo email

    var student = studentRepository.findByEmail(dto.getEmail());
    UUID studentId;
    if (student.isEmpty()) {
      var studentCreated = StudentEntity
        .builder()
        .email(dto.getEmail())
        .build();
      studentCreated = studentRepository.save(studentCreated);
      studentId = studentCreated.getId();
    } else {
      studentId = student.get().getId();
    }

    CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity
      .builder()
      .technology(dto.getTechnology())
      .studentId(studentId)
      .grate(correctAnswer.get())
      .build();

    var certificationStudentCreated = certificatationStudentRepository.save(
      certificationStudentEntity
    );

    answersCertifications
      .stream()
      .forEach(answerCertification -> {
        answerCertification.setCertificationId(
          certificationStudentEntity.getId()
        );
        answerCertification.setCertificationStudentEntity(
          certificationStudentEntity
        );
      });

    certificationStudentEntity.setAnswerCertificationsEntity(
      answersCertifications
    );

    certificatationStudentRepository.save(certificationStudentEntity);

    return certificationStudentCreated;
    //Salvar as informações da certificação
  }
}
