package eu.kraenz.moshstore.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/** Is the given string in lowercase? IMPORTANT: a `null` value will pass validation. */
public class LowercaseValidator implements ConstraintValidator<Lowercase, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) return true;
    return value.toLowerCase().equals(value);
  }
}
