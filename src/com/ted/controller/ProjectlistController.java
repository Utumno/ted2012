package com.ted.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ted.service.ProjectService;
import com.ted.service.ServiceExDBFailure;

@WebServlet("/projectlist")
public class ProjectListController extends Controller {

	private static final long serialVersionUID = 4030147452638613577L;
	private ProjectService projectService = new ProjectService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			ArrayList<String> projectlist = projectService
					.getAllProjectNamesForUser(null);
			request.setAttribute("projectlist", projectlist);
		} catch (ServiceExDBFailure e) {
			log.debug("ProjectListController::doGet", e);
			request.setAttribute("ErrorString", e.getMessage());
		}
		sc.getRequestDispatcher(PROJECTLIST_JSP).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
