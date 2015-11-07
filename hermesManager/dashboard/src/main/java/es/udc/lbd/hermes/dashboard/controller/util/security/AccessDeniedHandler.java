package es.udc.lbd.hermes.dashboard.controller.util.security;

//
///* Esta clase usase para cando unha persoa autenticada nunha parte da aplicacion accede a outra,
// * non se lle presente o aviso de que non ten permisos, senon que se cancele a sua sesion e vaia
// * para o formulario de login correspondente */
//public class AccessDeniedHandler extends AccessDeniedHandlerImpl {
//
//	@Override
//	public void handle(HttpServletRequest request,
//			HttpServletResponse response,
//			AccessDeniedException accessDeniedException) throws IOException,
//			ServletException {
//		SecurityContextHolder.getContext().setAuthentication(null);
//		request.getSession(true).invalidate();
//		response.sendRedirect(request.getRequestURL().toString());
//	}
//	
//}
