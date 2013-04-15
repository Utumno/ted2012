package com.ted.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ted.domain.User;
import com.ted.domain.User.RolesENUM;

/**
 * This filter checks for a SIGNED IN user and redirects to the home page in
 * case the user is signed it - away from the register/login pages
 */
@WebFilter(urlPatterns = { "/home", "/register", "/login" })
public class NotSignedInFilter extends BaseFilter {

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;
		// Check if session exists
		HttpSession session = httpReq.getSession(false);
		log.debug("Session " + session);
		if (session == null) {
			chain.doFilter(request, response);
			// System.out
			// .println("SessionCheckFilter.doFilter() after chain.doFilter session : "
			// + httpReq.getSession(false));
			return;
		} else {
			if (session.isNew()) {
				// so the browser gets a chance to acknowledge the session
				httpRes.sendRedirect(HOME_SERVLET);
				return;
			}
			User user = (User) session.getAttribute("signedUser");
			if (user == null) {
				session.invalidate();
				httpRes.sendRedirect(HOME_SERVLET);
				return;
			}
			log.debug("user : " + user);
			RequestDispatcher rd;
			if (user.getRole() != RolesENUM.ADMIN) {
				rd = session.getServletContext().getRequestDispatcher(
						USER_HOME_SERVLET);
			} else {
				rd = session.getServletContext().getRequestDispatcher(
						ADMIN_HOME_SERVLET);
			}
			rd.forward(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}
}
