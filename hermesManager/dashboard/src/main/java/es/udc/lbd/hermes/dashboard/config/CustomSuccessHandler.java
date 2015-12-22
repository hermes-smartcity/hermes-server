//package es.udc.lbd.hermes.dashboard.config;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
// 
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//     
//    @Override
//    protected void handle(HttpServletRequest request, 
//      HttpServletResponse response, Authentication authentication) throws IOException {
//    	 System.out.println("handle ------------");
//        String targetUrl = determineTargetUrl(authentication);
//  
//        if (response.isCommitted()) {
//            System.out.println("Can't redirect");
//            return;
//        }
//  
//        redirectStrategy.sendRedirect(request, response, targetUrl);
//    }
//     
//    protected String determineTargetUrl(Authentication authentication) {
//    	System.out.println("determineTargetUrl ------------");
//        String url="";
//         
//        Collection<? extends GrantedAuthority> authorities =  authentication.getAuthorities();
//         
//        List<String> roles = new ArrayList<String>();
//    
//        for (GrantedAuthority a : authorities) {
//            roles.add(a.getAuthority());
//        }
// 
//        //TODO pasar urls al fichero de constantes
//        if (isAdmin(roles)) {
//            url = "/";
//        } else if (isConsulta(roles)) {
//            url = "/#/consulta/TusEventos";
//        } else {
//            url="/accessDenied";
//        }
// 
//        return url;
//    }
//  
//    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
//    	System.out.println("setRedirectStrategy ------------");
//        this.redirectStrategy = redirectStrategy;
//    }
//    protected RedirectStrategy getRedirectStrategy() {
//    	System.out.println("getRedirectStrategy ------------");
//        return redirectStrategy;
//    }
//     
//    private boolean isConsulta(List<String> roles) {
//        if (roles.contains("ROLE_CONSULTA")) {
//            return true;
//        }
//        return false;
//    }
//    
//    private boolean isAdmin(List<String> roles) {
//        if (roles.contains("ROLE_ADMINISTRADOR")) {
//            return true;
//        }
//        return false;
//    }
// 
//}