package es.enxenio.smart.dashboard.controller.util.exceptionresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionResolver implements HandlerExceptionResolver {

	private static final Logger log = LoggerFactory.getLogger(ExceptionResolver.class);
	
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
