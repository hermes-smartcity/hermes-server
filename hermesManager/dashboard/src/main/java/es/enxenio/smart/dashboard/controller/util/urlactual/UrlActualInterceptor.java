package es.enxenio.smart.dashboard.controller.util.urlactual;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UrlActualInterceptor extends HandlerInterceptorAdapter {
	
	private String parametro;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		
		if (queryString != null) {
			StringBuffer resultado = new StringBuffer();
			StringTokenizer tokenizer = new StringTokenizer(queryString, "&");
			while (tokenizer.hasMoreTokens()) {
				String siguienteToken = tokenizer.nextToken();
				if (!siguienteToken.startsWith(parametro)) {
					if (resultado.length() > 0) {
						resultado.append('&');
					}
					resultado.append(siguienteToken);
				}
			}
			if (resultado.length() > 0) {
				url.append('?').append(resultado);
			}	
		}
		
		request.setAttribute("URL_ACTUAL", url);
		return true;
	}
	
	public void setEliminarParametro(String parametro) {
		this.parametro = parametro;
	}

}
