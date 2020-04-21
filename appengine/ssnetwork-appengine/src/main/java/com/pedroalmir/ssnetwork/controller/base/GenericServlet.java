/**
 * 
 */
package com.pedroalmir.ssnetwork.controller.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;

/**
 * @author Pedro Almir
 *
 */
public abstract class GenericServlet extends HttpServlet {
	
	/** */
	private static final long serialVersionUID = -6419322864776099910L;
	
	@Override
	public void init() throws ServletException {
		super.init();
		MyEntityManager.getManager().clear();
	}

	/**
	 * @param response
	 * @param result
	 * @throws IOException
	 */
	protected void sendResponse(HttpServletResponse response, Object result) throws IOException {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		PrintWriter out = response.getWriter();
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(result));
        out.flush();
	}
	
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
		response.addHeader("Access-Control-Max-Age", "1728000");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, accept, origin");
		response.addHeader("Access-Control-Allow-Credentials", "true");
	}
}
