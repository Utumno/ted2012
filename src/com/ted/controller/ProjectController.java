package com.ted.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ted.domain.Project;
import com.ted.domain.Project.Job;
import com.ted.domain.User;
import com.ted.domain.User.RolesENUM;
import com.ted.helpers.Helpers;
import com.ted.service.ProjectService;
import com.ted.service.ServiceExDBFailure;
import com.ted.service.ServiceExProjectExists;
import com.ted.service.UserService;
import com.ted.validators.Validators;

@WebServlet("/project")
public class ProjectController extends Controller {

	private static final long serialVersionUID = 3062157291535232368L;
	private ProjectService projectService = new ProjectService();
	private UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Project proj = null;
		final String name = Helpers.decodeRequest(request.getParameter("name"));
		System.out.println("ProjectController.doGet() " + name);
		try {
			proj = projectService.getProjectInfoByName(name);
			if (proj == null) {
				throw new ServiceExDBFailure();
			}
			// request.setAttribute("project", proj);
			User signedUser = (User) (request.getSession(false)
					.getAttribute("signedUser"));
			if (signedUser == null) { // should not be
				response.sendRedirect(HOME_SERVLET);
				return;
			}
			RolesENUM role = signedUser.getRole();

			switch (role) {
			case ADMIN:
				request.setAttribute("admin", true);
				helperBasicProjectInitialization(request, proj);
				helperAllStaffInitialization(request,
						(ArrayList<String>) request.getAttribute("addedStaff"));
				// only the admin can change the manager
				helperAllManagerNames(request);
				sc.getRequestDispatcher(PROJECT_JSP).forward(request, response);
				return;
			case MANAGER:
				request.setAttribute("manager", true);
				if (proj.getManager().getUsername()
						.equals(signedUser.getUsername())) {
					// Manager to this project
					request.setAttribute("projectManager", true);
					helperBasicProjectInitialization(request, proj);
					helperAllStaffInitialization(request,
							(ArrayList<String>) request
									.getAttribute("addedStaff"));
					sc.getRequestDispatcher(PROJECT_JSP).forward(request,
							response);
					return;
				}
			default:
				// the manager falls through if not manager of this
				// project - to see the Public projects
				// http://stackoverflow.com/a/8564008/281545
				if (role == RolesENUM.GUEST) {
					request.setAttribute("guest", true);
				} else if (role == RolesENUM.STAFF) {
					request.setAttribute("staff", true);
				}
				ArrayList<String> addedStaff = new ArrayList<String>();
				ArrayList<User> addedStaffUsers = proj.getStaff();
				for (User a : addedStaffUsers) {
					addedStaff.add(a.getUsername());
				}
				if (proj.isPublik()
						|| (addedStaff.contains(signedUser.getUsername()))) {
					helperBasicProjectInitialization(request, proj);
					sc.getRequestDispatcher(PROJECT_JSP).forward(request,
							response);
				} else {
					response.sendRedirect(HOME_SERVLET);
				}
				return;
			}
		} catch (ServiceExDBFailure e) {
			log.debug("ProjectController::doGet", e);
			request.setAttribute("ErrorString", e.getMessage());
			sc.getRequestDispatcher(PROJECT_JSP).forward(request, response);
			return;
		}
	}

	/**
	 * Only used when the user is admin : sets the allManagers attribute so the
	 * admin can change the manager
	 * 
	 * @param request
	 * @throws ServiceExDBFailure
	 */
	private void helperAllManagerNames(HttpServletRequest request)
			throws ServiceExDBFailure {
		ArrayList<String> allManagers = new ArrayList<>();
		ArrayList<User> allManagersUsers = null;
		allManagersUsers = userService.allManagers();
		for (User user : allManagersUsers) {
			allManagers.add(user.getUsername());
		}
		request.setAttribute("allManagers", allManagers);
	}

	/**
	 * Initializes basic project information common to all Roles that have
	 * access to the project
	 * 
	 * @param request
	 * @param proj
	 * @throws ServiceExDBFailure
	 */
	private void helperBasicProjectInitialization(HttpServletRequest request,
			Project proj) throws ServiceExDBFailure {

		request.setAttribute("name", proj.getName());
		request.setAttribute("description", proj.getDescription());
		if (proj.isPublik()) {
			request.setAttribute("publik", "publik");
		} else {
			request.setAttribute("publik", "private");
		}
		request.setAttribute("selectedManager", proj.getManager().getUsername());

		ArrayList<String> addedStaff = new ArrayList<String>();
		ArrayList<User> addedStaffUsers = proj.getStaff();
		for (User a : addedStaffUsers) {
			addedStaff.add(a.getUsername());
		}
		request.setAttribute("addedStaff", addedStaff);

		ArrayList<Job> jobs = null;
		jobs = projectService.getJobsForProject(proj.getName());
		request.setAttribute("jobs", jobs);
	}

	/**
	 * Only the manager and the admin can add or remove staff
	 * 
	 * @param request
	 * @param addedStaff
	 * @throws ServiceExDBFailure
	 */
	private void helperAllStaffInitialization(HttpServletRequest request,
			ArrayList<String> addedStaff) throws ServiceExDBFailure {
		ArrayList<String> allStaff = new ArrayList<>();
		ArrayList<User> allStaffUsers = userService.allStaff();
		for (User user : allStaffUsers) {
			allStaff.add(user.getUsername());
		}
		allStaff.removeAll(addedStaff);
		request.setAttribute("allStaff", allStaff);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String description = null, publik = null, manager = null, staffMember = null, deletedStaffMember = null;
		String[] addedStaff1 = request.getParameterValues("added");
		ArrayList<String> addedStaff = new ArrayList<>(); // never null ****
		if (addedStaff1 != null) {
			for (int i = 0; i < addedStaff1.length; ++i) {
				addedStaff.add(addedStaff1[i]);
			}
		}

		final String name = Helpers.decodeRequest(request.getParameter("name"));
		description = request.getParameter("description");
		publik = request.getParameter("publik");
		manager = request.getParameter("manager"); // IF NOT admin THIS WILL BE
													// EMPTY !
		staffMember = request.getParameter("staffMember");
		deletedStaffMember = request.getParameter("deletedStaffMember");
		try {
			Project proj = projectService.getProjectInfoByName(name);
			if (proj == null) {
				throw new ServiceExDBFailure();
			}
			// request.setAttribute("project", proj);
			User signedUser = (User) (request.getSession(false)
					.getAttribute("signedUser"));
			RolesENUM role = signedUser.getRole();

			switch (role) {
			case ADMIN:
				request.setAttribute("admin", true);
				// only the admin can change the manager
				helperAllManagerNames(request);
				break;
			case MANAGER:
				request.setAttribute("manager", true);
				if (proj.getManager().getUsername()
						.equals(signedUser.getUsername())) {
					// Manager to this project
					manager = signedUser.getUsername();
					request.setAttribute("projectManager", true);
				}
				break;
			default:
				if (role == RolesENUM.GUEST) {
					request.setAttribute("guest", true);
				} else if (role == RolesENUM.STAFF) {
					request.setAttribute("staff", true);
				}
				if (!(proj.isPublik() || (addedStaff.contains(signedUser
						.getUsername())))) {
					response.sendRedirect(HOME_SERVLET);
					return;
				}
				break;
			}
			// AN PATH8HKE "ENHMERWSH"
			if (request.getParameter("updateProject") != null) {
				// validation twn pediwn
				boolean error = false;
				if (Validators.isNullOrEmpty(description)) {
					request.setAttribute("emptyDescription", true);
					error = true;
				}
				if (Validators.isNullOrEmpty(publik)) {
					request.setAttribute("emptyPublik", true);
					error = true;
				}
				if (Validators.isNullOrEmpty(manager)) {
					request.setAttribute("emptyManager", true);
					error = true;
				}
				if (addedStaff.isEmpty()) {
					request.setAttribute("emptyStaff", true);
					error = true;
				}
				if (!error) {
					System.out.println("ProjectController.doPost() - !error");
					proj = new Project();
					proj.setName(name);
					proj.setDescription(description);
					proj.setPublik("private".equals(publik) ? false : true);
					try {
						proj.setManager(userService
								.getUserWithUsername(manager));
						ArrayList<User> staff = getUsersFromUsernames(addedStaff);
						proj.setStaff(staff);
						projectService.updateProject(proj);
						response.sendRedirect(Helpers.encodeUri(
								"projectlist?name=", name));
						return;
					} catch (ServiceExProjectExists e) {
						log.debug("ProjectController::doPost", e);
						request.setAttribute("duplicateProjectName",
								e.getMessage());
					}
				}
			}

			request.setAttribute("name", name);
			request.setAttribute("description", description);
			request.setAttribute("publik", publik);
			request.setAttribute("selectedManager", manager);
			ArrayList<Job> jobs = null;
			jobs = projectService.getJobsForProject(name);
			request.setAttribute("jobs", jobs);
			helperProjectArrays(request, staffMember, deletedStaffMember,
					addedStaff);
		} catch (ServiceExDBFailure e) {
			log.debug("ProjectController::doPost", e);
			request.setAttribute("ErrorString", e.getMessage());
		}
		RequestDispatcher rd = sc.getRequestDispatcher(PROJECT_JSP);
		rd.forward(request, response);
		return;
	}

	private void helperProjectArrays(HttpServletRequest request,
			String staffMember, String deletedStaffMember,
			ArrayList<String> addedStaff) throws ServiceExDBFailure {

		// AN PATH8HKE TO PROS8HKH STAFF
		if (request.getParameter("addStaff") != null) {
			if (staffMember != null) {
				addedStaff.add(staffMember);
			}
		}
		// AN PATH8HKE TO DIAGRAFH STAFF
		if (request.getParameter("deleteStaff") != null) {
			if (deletedStaffMember != null) {
				addedStaff.remove(deletedStaffMember);
			}
		}
		request.setAttribute("addedStaff", addedStaff);

		ArrayList<String> allStaff = new ArrayList<>();
		ArrayList<User> allStaffUsers = userService.allStaff();
		for (User user : allStaffUsers) {
			allStaff.add(user.getUsername());
		}
		allStaff.removeAll(addedStaff);
		request.setAttribute("allStaff", allStaff);

		ArrayList<String> allManagers = new ArrayList<>();
		ArrayList<User> allManagersUsers = userService.allManagers();
		for (User user : allManagersUsers) {
			allManagers.add(user.getUsername());
		}
		request.setAttribute("allManagers", allManagers);
	}

	private ArrayList<User> getUsersFromUsernames(ArrayList<String> usernames)
			throws ServiceExDBFailure {
		ArrayList<User> users = new ArrayList<>();
		for (String username : usernames) {
			users.add(userService.getUserWithUsername(username));
		}
		return users;
	}

}
