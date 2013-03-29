package com.ted.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeController extends Controller {

	private static final long serialVersionUID = -2097068583224851634L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("HomeController.doGet() session : "
				+ request.getSession(false));
		sc.getRequestDispatcher(HOME_JSP).forward(request, response);
		// forward - can't redirect inside WEB-INF/*
		System.out.println("HomeController.doGet() session after forward: "
				+ request.getSession(false));
		return;
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
