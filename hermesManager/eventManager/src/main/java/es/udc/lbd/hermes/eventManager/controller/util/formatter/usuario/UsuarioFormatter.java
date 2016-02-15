//package es.enxenio.smart.citydriver.controller.util.formatter.usuario;
//
//import java.text.ParseException;
//import java.util.Locale;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.Formatter;
//import org.springframework.web.context.support.SpringBeanAutowiringSupport;
//
//import es.enxenio.aspg.enciclopedia.model.usuario.Usuario;
//import es.enxenio.aspg.enciclopedia.model.usuario.service.UsuarioService;
//
//
//public class UsuarioFormatter implements Formatter<Usuario> {
//
//	@Autowired
//	private UsuarioService xestorContidosService;
//	
//	public UsuarioFormatter() {
//		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
//	}
//	
//	@Override
//	public String print(Usuario object, Locale locale) {
//		return String.valueOf(object.getId());
//	}
//
//	@Override
//	public Usuario parse(String text, Locale locale) throws ParseException {
//		if (text.isEmpty()) {
//			return null;
//		}
//		return xestorContidosService.get(Long.valueOf(text));
//	}
//
//}
