package es.udc.lbd.hermes.dashboard.controller.util.formatter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

/* Clase que rexistra de forma automatica todos as implementacions
 * de Formatter<T>, que estean definidas como @Component */
public class RexistroFormatters implements FormatterRegistrar {

	@Autowired(required = false)
	List<Formatter<?>> formatters = new ArrayList<Formatter<?>>();
	
	@Override
	public void registerFormatters(FormatterRegistry registry) {
		for (Formatter<?> formatter : formatters) {
			registry.addFormatter(formatter);
		}
	}

}
