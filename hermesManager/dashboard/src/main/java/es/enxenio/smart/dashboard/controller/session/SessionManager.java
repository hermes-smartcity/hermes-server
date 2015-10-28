package es.enxenio.smart.dashboard.controller.session;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

public class SessionManager {

	private static final String ATRIBUTO_MENSAXES_PENDENTES = "MENSAXES_PENDENTES";
	
	/** Almacena unha nova mensaxe pendente */
	public synchronized static void addMensaxePendente(HttpSession session, MensaxePendente mensaxePendente) {
		@SuppressWarnings("unchecked")
		List<MensaxePendente> mensaxes = (List<MensaxePendente>) session.getAttribute(ATRIBUTO_MENSAXES_PENDENTES);
		if (mensaxes == null)
			mensaxes = new ArrayList<MensaxePendente>();
		mensaxes.add(mensaxePendente);
		session.setAttribute(ATRIBUTO_MENSAXES_PENDENTES, mensaxes);
	}
	

}
