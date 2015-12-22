package es.udc.lbd.hermes.eventManager.web.rest;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.lbd.hermes.eventManager.web.rest.custom.JSONError;


public abstract class MainResource {
	static Logger log = Logger.getLogger(MainResource.class);
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody JSONError exceptionHandler(Exception e) {

		log.debug("Exception: " + e.getMessage());

		e.printStackTrace();
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));

		return new JSONError(JSONError.formatName(e), e.getMessage(),
				errors.toString());
	}
}
