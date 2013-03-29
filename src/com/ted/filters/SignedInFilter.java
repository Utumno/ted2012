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

/**
 * This filter checks if the user is NOT signed in and redirects to the
 * index.jsp in case the user is not
 * 
 */
@WebFilter(urlPatterns = { "/createjob", "/createproject", "/deletejob",
		"/deleteproject", "/deleteuser", "/logout", "/job", "/profile",
		"/project", "/projectlist", "/userhome", "/userlist" })
public class SignedInFilter implements Filter, Addresses {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;

		HttpSession session = httpReq.getSession(false);
		if (session != null) {
			if (session.getAttribute("signedUser") != null) {
				chain.doFilter(request, response);
				return;
			} else
				session.invalidate();
		}
		httpRes.sendRedirect(HOME_SERVLET);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
