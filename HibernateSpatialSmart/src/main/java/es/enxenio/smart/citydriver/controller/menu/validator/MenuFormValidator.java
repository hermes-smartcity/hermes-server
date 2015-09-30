package es.enxenio.smart.citydriver.controller.menu.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import es.enxenio.smart.citydriver.controller.menu.form.MenuForm;



public class MenuFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return MenuForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "erros.obrigatorio");
			
	}

}
