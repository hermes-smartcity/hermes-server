package es.enxenio.smart.citydriver.controller.estatica.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import es.enxenio.smart.citydriver.controller.estatica.form.EstaticaForm;



public class EstaticaFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return EstaticaForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "titulo", "erros.obrigatorio");
			
	}

}
