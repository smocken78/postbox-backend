package de.congstar.auth.servlet.filter;



import de.congstar.auth.JWTGenerator;
import de.congstar.auth.JWTValidator;
import de.congstar.auth.SSORedirect;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.configuration.ConfigurationHolder3;
import org.mocken.exception.ApplicationException;
import org.mocken.exception.AuthenticationException;


/**
 * @author smocken
 * Filter for all requests to verify corret authorization
 * Successor for SimpleDispatcher Servlet
 */
@WebFilter(filterName = "Dispatcher", urlPatterns = {"/*"})
public class DispatchingFilter implements Filter {

    private static Pattern excludedURLs = null;
    private final Logger logger = LogManager.getLogger(this.getClass());
    final int jwtCookieLifetimeInSeconds = ConfigurationHolder3.getConfiguration().getInt("appconfig.jwt.cookie_TTL_in_minutes", 240) * 60;


    public void init(FilterConfig fConfig) throws ServletException {
        final String excluded = ConfigurationHolder3.getConfiguration()
            .getString("appconfig.security.auth.excludedURLs", null);

        if (excluded != null) {
            try {
                excludedURLs = Pattern.compile(excluded);
                logger.info("Exclude list for dispatching filter initialized with {}", excluded);
            } catch (PatternSyntaxException e) {
                logger.error(
                    "Could not initialize exclude list in dispatching filter. List stays empty.",
                    e);
            }
        }

        logger.info("DispatchingFilter initialized");
    }

    private boolean excludeURI(String uri) {
        return excludedURLs != null && excludedURLs.matcher(uri).find();
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

    	HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	    String jwtToken = request.getHeader("jwtToken")!=null?request.getHeader("jwtToken"):request.getParameter("value");
	    Cookie [] cookies = request.getCookies();
	    if (cookies!=null) {
		    for (int i=0;i<cookies.length;i++) {
		    	Cookie cookie = cookies[i];
		    	if (cookie.getName().matches("jwtToken"))
		    		jwtToken = cookie.getValue();
		    }
	    }


	    if (!excludeURI(request.getRequestURI())) {
			if (jwtToken == null) 
			{
				SSORedirect redirect = new SSORedirect();
				redirect.getRedirect(request, response);
				return;
			}
			else {
				JWTValidator jwt = new JWTValidator();
				try {
					if (jwt.authenticate(request,jwtToken)) {
						Cookie cookie = new Cookie("jwtToken", JWTGenerator.getJWTToken((User)request.getAttribute("user")));
						cookie.setSecure(true);
						cookie.setPath(request.getContextPath());
						cookie.setMaxAge(jwtCookieLifetimeInSeconds);
						response.addCookie(cookie);
					}
					chain.doFilter(req, res);
					return;
				}
				catch (ApplicationException | AuthenticationException e) {
					// TODO Auto-generated catch block
					Cookie cookie = new Cookie("jwtToken", "empty");
					cookie.setSecure(true);
					cookie.setPath(request.getContextPath());
					cookie.setMaxAge(0);
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
			}
	    }
	    else {
	    	logger.info("Pass of URI {} without security checks",request.getRequestURI());
	    	chain.doFilter(req, res);
	    }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

   
}
