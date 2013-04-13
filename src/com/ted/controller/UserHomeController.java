package com.ted.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ted.domain.Project.Job;
import com.ted.domain.Project.StatesENUM;
import com.ted.domain.User;
import com.ted.domain.User.RolesENUM;
import com.ted.service.ProjectService;
import com.ted.service.ServiceExDBFailure;

@WebServlet("/userhome")
public class UserHomeController extends Controller {

	private static final long serialVersionUID = 3284585663405610864L;
	private ProjectService projectService = new ProjectService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		final User user = (User) request.getSession(false).getAttribute(
				"signedUser");
		final String username = user.getUsername();
		RolesENUM role = user.getRole();
		switch (role) {
		case STAFF:
			request.setAttribute("staff", true);
			break;
		case MANAGER:
			request.setAttribute("manager", true);
			break;
		default:
			break;
		}
		try {
			// pairnw ola ta projects tou user
			List<String> userProjects = projectService
					.getAllProjectNamesForUser(username);
			request.setAttribute("userProjects", userProjects);
			List<String> publicProjects = projectService
					.getAllProjectNames(true);
			publicProjects.removeAll(userProjects);
			request.setAttribute("publicProjects", publicProjects);
			Map<String, List<Job>> userJobs = new HashMap<>();
			// briskw mono oses jobs den einai done
			for (String projectName : userProjects) {
				List<Job> lolol = projectService.getAllJobsForUserInProject(
						username, projectName);
				for (int i = lolol.size() - 1; i >= 0; i--) {
					Job job = lolol.get(i);
					if (job.getState() == StatesENUM.DONE) {
						lolol.remove(i);
					}
				}
				if (!lolol.isEmpty()) {
					userJobs.put(projectName, lolol);
				}
			}
			System.out.println("UserHomeController.doGet() JOBS " + userJobs);
			request.setAttribute("userJobs", userJobs);
		} catch (ServiceExDBFailure e) {
			log.debug("UserHomeController::doGet", e);
			request.setAttribute("ErrorString", e.getMessage());
		}
		sc.getRequestDispatcher(USER_HOME_JSP).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
