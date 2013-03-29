package com.ted.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ted.controller.Addresses;
import com.ted.domain.User;
import com.ted.domain.User.RolesENUM;

/**
 * This filter checks if the user is admin and redirects to home if user is not
 * 
 */
@WebFilter(urlPatterns = { "/adminhome", "/createproject", "/deleteproject",
		"/deleteuser", "/projectlist", "/userlist" })
public class AdminFilter implements Filter, Addresses {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;

		HttpSession session = httpReq.getSession(false);
		if (((User) session.getAttribute("signedUser")).getRole() == RolesENUM.ADMIN) {
			chain.doFilter(request, response);
			return;
		}
		httpRes.sendRedirect(HOME_SERVLET);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
