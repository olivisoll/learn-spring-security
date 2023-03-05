package com.baeldung.lss.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SecurityQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", unique = true)
    private User user;
    @OneToOne(targetEntity = SecurityQuestionDefinition.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "securityQuestionDefinition_id")
    private SecurityQuestionDefinition questionDefinition;
    private String answer;

    public SecurityQuestion(final User user, final SecurityQuestionDefinition questionDefinition, final String answer) {
        this.user = user;
        this.questionDefinition = questionDefinition;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public SecurityQuestionDefinition getQuestionDefinition() {
        return questionDefinition;
    }

    public void setQuestionDefinition(final SecurityQuestionDefinition questionDefinition) {
        this.questionDefinition = questionDefinition;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SecurityQuestion that = (SecurityQuestion) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(questionDefinition, that.questionDefinition) && Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, questionDefinition, answer);
    }
}