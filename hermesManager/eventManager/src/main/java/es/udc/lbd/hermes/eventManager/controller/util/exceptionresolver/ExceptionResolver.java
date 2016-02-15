package es.udc.lbd.hermes.eventManager.controller.util.exceptionresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionResolver implements HandlerExceptionResolver {

	static Logger log = Logger.getLogger(ExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		log.error(request.getRequestURI(), ex);
		String url = request.getRequestURI().substring(request.getContextPath().length());
		if (url.startsWith("/dashboard")) {
			ModelAndView mav = new ModelAndView("dashboard/excepcion");
			mav.addObject("excepcion", ex);
			mav.addObject("url", request.getRequestURL());
			return mav;
		} else {
			return new ModelAndView("excepcion");
		}
	}


}
