package eu.kraenz.moshstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/** Is the given string in lowercase? IMPORTANT: a `null` value will pass validation. */
public class LowecaseValidator implements ConstraintValidator<Lowercase, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) return true;
    var x = value.toLowerCase().equals(value);
    return x;
  }
}
