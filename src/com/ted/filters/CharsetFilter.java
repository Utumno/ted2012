package com.ted.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

//import org.elkethe.util.RequestPrinter;
/**
 * Charset filter. Standard way of ensuring consistent encoding of request/
 * response streams. MUST BE THE VERY FIRST FILTER IN THE CHAIN. Also disables
 * page caching for supported browsers
 */
@WebFilter(urlPatterns = { "/*" })
public class CharsetFilter implements Filter {

	private String encoding;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// System.out.println("CharsetFilter.doFilter()");
		// Respect the client-specified character encoding
		// (see HTTP specification section 3.4.1)
		if (null == request.getCharacterEncoding())
			request.setCharacterEncoding(encoding);
		// Set the default response content type and encoding
		// response.setContentType("text/html; charset=UTF-8");
		// equivalent - see
		// http://stackoverflow.com/questions/4864899/encoding-and-servlet-api-setcontenttype-or-setcharacterencoding
		response.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
		HttpServletResponse httpResp = (HttpServletResponse) response;
		// just BEFORE THE RESPONSE IS COMMITTED
		// See :
		// http://stackoverflow.com/questions/49547/making-sure-a-web-page-is-not-cached-across-all-browsers/2068407#2068407
		httpResp.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		httpResp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		httpResp.setDateHeader("Expires", 0); // Proxies.

		// System.out.println("Headers : "
		// + RequestPrinter
		// .debugStringHeaders((HttpServletRequest) request));
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		encoding = filterConfig.getInitParameter("requestEncoding");
		if (encoding == null)
			encoding = "UTF-8";
	}
}
