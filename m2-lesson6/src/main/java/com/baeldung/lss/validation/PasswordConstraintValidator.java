package com.baeldung.lss.validation;

import com.google.common.base.Joiner;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

final class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        final PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(8, 30)));
        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.buildConstraintViolationWithTemplate(
                        Joiner.on("n").join(validator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }

    @Override
    public void initialize(final ValidPassword constraintAnnotation) {

    }


}
