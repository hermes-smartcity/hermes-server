package es.enxenio.smart.eventManager.web.rest.custom;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONError {

	private String message;
	private String stackTrace = null;
	private String exceptionClassName;
	private Map<String, String> exceptionParameters = new HashMap<String, String>();

	public JSONError() {

	}

	public JSONError(String exceptionClassName, String message) {

		this.exceptionClassName = exceptionClassName;
		this.message = message;
	}

	public JSONError(String exceptionClassName, String message,
			String stackTrace) {

		this.exceptionClassName = exceptionClassName;
		this.message = message;
		this.stackTrace = stackTrace;
	}

	public String asString() {

		try {
			return new ObjectMapper().writer().withDefaultPrettyPrinter()
					.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String formatName(Exception e) {

		return e.getClass().getSimpleName().replace("Exception", "");
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}

	public String getStackTrace() {

		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {

		this.stackTrace = stackTrace;
	}

	public String getExceptionClassName() {

		return exceptionClassName;
	}

	public void setExceptionClassName(String exceptionClassName) {

		this.exceptionClassName = exceptionClassName;
	}

	public Map<String, String> getExceptionParameters() {

		return exceptionParameters;
	}

	public void setExceptionParameters(Map<String, String> exceptionParameters) {

		this.exceptionParameters = exceptionParameters;
	}
}
