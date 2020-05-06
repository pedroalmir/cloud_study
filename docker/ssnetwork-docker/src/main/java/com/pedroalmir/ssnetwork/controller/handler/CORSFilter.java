/**
 * 
 */
package com.pedroalmir.ssnetwork.controller.handler;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Pedro Almir
 *
 */
public class CORSFilter implements Filter{
	
	FilterConfig fc;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.fc = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		
		HttpServletResponse response = (HttpServletResponse) resp;
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
		response.addHeader("Access-Control-Max-Age", "1728000");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, accept, origin");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		
		// pass the request/response on
	    chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		 this.fc = null;
	}

}
