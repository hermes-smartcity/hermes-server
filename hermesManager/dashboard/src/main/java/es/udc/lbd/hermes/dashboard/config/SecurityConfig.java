//package es.udc.lbd.hermes.dashboard.config;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.csrf.CsrfFilter;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
//import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.WebUtils;
//
//@EnableWebSecurity
//@Configuration
//public class SecurityConfig extends
//   WebSecurityConfigurerAdapter {
//  @Autowired
//  public void configureGlobal(AuthenticationManagerBuilder auth) {
//    try {
//		auth.inMemoryAuthentication()
//		    .withUser("user")  // #1
//		      .password("password")
//		      .roles("USER")
//		      .and()
//		    .withUser("admin") // #2
//		      .password("password")
//		      .roles("ADMIN","USER");
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//  }
//
//  @Override
//  public void configure(WebSecurity web) throws Exception {
//    web
//      .ignoring()
//         .antMatchers("/resources/**"); // #3
//  }
//
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.authorizeRequests()
//        .antMatchers("/signup","/about").permitAll() // #4
//        .antMatchers("/admin/**").hasRole("ADMIN") // #6
//        .anyRequest().authenticated() // 7
//        .and()
//    .formLogin()  // #8
//        .loginPage("/usuario/signin") // #9
//        .permitAll().and().csrf()
//		.csrfTokenRepository(csrfTokenRepository()).and()
//		.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);// #5
//  }
//  
//  private Filter csrfHeaderFilter() {
//		return new OncePerRequestFilter() {
//			@Override
//			protected void doFilterInternal(HttpServletRequest request,
//					HttpServletResponse response, FilterChain filterChain)
//					throws ServletException, IOException {
//				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
//						.getName());
//				if (csrf != null) {
//					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
//					String token = csrf.getToken();
//					if (cookie == null || token != null
//							&& !token.equals(cookie.getValue())) {
//						cookie = new Cookie("XSRF-TOKEN", token);
//						cookie.setPath("/");
//						response.addCookie(cookie);
//					}
//				}
//				filterChain.doFilter(request, response);
//			}
//		};
//	}
//  
//  private CsrfTokenRepository csrfTokenRepository() {
//	  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//	  repository.setHeaderName("X-XSRF-TOKEN");
//	  return repository;
//	}
//}