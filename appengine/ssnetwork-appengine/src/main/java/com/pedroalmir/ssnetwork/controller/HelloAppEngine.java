package com.pedroalmir.ssnetwork.controller;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloAppEngine", urlPatterns = { "/hello" })
public class HelloAppEngine extends HttpServlet {

	/** Serial Version UID */
	private static final long serialVersionUID = -268736010161305297L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print("SSNetwork in App Engine!\r\n");
	}
}