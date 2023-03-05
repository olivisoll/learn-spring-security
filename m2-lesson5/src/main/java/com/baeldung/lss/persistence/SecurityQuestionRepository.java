package com.baeldung.lss.persistence;

import com.baeldung.lss.model.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Long> {

    SecurityQuestion findByQuestionDefinitionIdAndUserIdAndAnswer(Long questionDefinitionId, Long userId, String answer);

    SecurityQuestion findByUserIdAndAnswer(Long userId, String answer);
}
