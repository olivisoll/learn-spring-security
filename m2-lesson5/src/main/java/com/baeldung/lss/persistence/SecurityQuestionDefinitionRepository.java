package com.baeldung.lss.persistence;

import com.baeldung.lss.model.SecurityQuestionDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityQuestionDefinitionRepository extends JpaRepository<SecurityQuestionDefinition, Long> {
}
