package com.ted.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ted.domain.Project;
import com.ted.helpers.Helpers;
import com.ted.service.ProjectService;
import com.ted.service.ServiceExDBFailure;

@WebServlet("/deleteproject")
public class DeleteProjectController extends Controller {

	private static final long serialVersionUID = 4956286744189905193L;
	private static final String SUCCESS = "Το project διεγράφη !";
	private String redirectAdress = PROJECTLIST_SERVLET;
	private ProjectService projectService = new ProjectService();

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] projectsToDelete = request.getParameterValues("deleteProject");
		if (projectsToDelete != null) {
			try {
				for (String proj : projectsToDelete) {
					System.out.println(proj);
					Project projToDelete = projectService
							.getProjectInfoByName(proj);
					projectService.deleteProject(projToDelete);
					redirectAdress += "?r=" + messageKey(request, SUCCESS);
				}
			} catch (ServiceExDBFailure e) {
				log.debug("DeleteProjectController::doPost", e);
				request.setAttribute("ErrorString", e.getMessage());
			}
		}
		response.sendRedirect(redirectAdress);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		final String projName = Helpers.decodeRequest(request
				.getParameter("name"));
		Project projToDelete;
		try {
			projToDelete = projectService.getProjectInfoByName(projName);
			projectService.deleteProject(projToDelete);
			redirectAdress += "?r=" + messageKey(request, SUCCESS);
		} catch (ServiceExDBFailure e) {
			log.debug("DeleteProjectController::doGet", e);
			request.setAttribute("ErrorString", e.getMessage());
		}
		response.sendRedirect(redirectAdress);
	}
}
