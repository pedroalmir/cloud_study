package com.pedroalmir.ssnetwork;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pedroalmir.ssnetwork.service.MyCloudStorageService;

@WebServlet(name = "HelloAppEngine", urlPatterns = { "/hello" })
public class HelloAppEngine extends HttpServlet {

	/** Serial Version UID */
	private static final long serialVersionUID = -268736010161305297L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		//UserDAO userDAO = new UserDAO();
		//userDAO.save("Pedro Almir Martins de Oliveira", "pedroalmir", "p.pedroalmir8477@gmail.com", "password", null);
		
		MyCloudStorageService.uploadImage(null);
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print("SSNetwork in App Engine!\r\n");

	}
}