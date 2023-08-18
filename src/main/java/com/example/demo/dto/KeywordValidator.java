package com.example.demo.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class KeywordValidator implements ConstraintValidator<ValidKeyword, KeywordForm> {
	@Override
	public void initialize(ValidKeyword constraintAnnotation) {
	}

	@Override
	public boolean isValid(KeywordForm value, ConstraintValidatorContext context) {
		String nameKeyword = value.getNameKeyword();
		String addressKeyword = value.getAddressKeyword();
		return !(nameKeyword == null || nameKeyword.isEmpty()) || !(addressKeyword == null || addressKeyword.isEmpty());
	}
}
