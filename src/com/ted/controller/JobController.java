package com.ted.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.ted.domain.Project;
import com.ted.domain.Project.Job;
import com.ted.domain.Project.Job.Comment;
import com.ted.domain.Project.StatesENUM;
import com.ted.domain.User;
import com.ted.domain.User.RolesENUM;
import com.ted.service.ProjectService;
import com.ted.service.ServiceExDBFailure;
import com.ted.service.UserService;
import com.ted.validators.Validators;

@WebServlet("/job")
public class JobController extends Controller {

	private static final long serialVersionUID = 2390137174902920859L;
	private ProjectService projectService = new ProjectService();
	private UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Job job = null;
		User signedUser = (User) (request.getSession(false)
				.getAttribute("signedUser"));
		if (signedUser == null) { // should not be
			response.sendRedirect(HOME_SERVLET);
			return;
		}
		int jobId = Integer.parseInt(request.getParameter("id"));

		// pairnw to job kai 8etw ta aparaithtta attributes sto request
		try {
			Project proj = projectService.getProjectInfoByName(projectService
					.getProjectNameOfJob(jobId));
			job = projectService.getJobWithId(jobId);
			if (job == null || proj == null) {
				throw new ServiceExDBFailure();
			}

			helperBasicJobInitialization(request, job);
			RolesENUM role = signedUser.getRole();

			switch (role) {
			case GUEST:
				if (!proj.isPublik()) {
					System.out.println("oxi public project kai eisai guest");
					response.sendRedirect(HOME_SERVLET);
					return;
				}
				request.setAttribute("guest", true);
				break;
			case STAFF:
				request.setAttribute("staff", true);
				for (User user : job.getJobStaff()) {
					if (user.getUsername().equals(signedUser.getUsername())) {
						request.setAttribute("relativeStaff", true);
						break;
					}
				}
				boolean anhkwStoProject = false;
				for (User user : proj.getStaff()) {
					if (user.getUsername().equals(signedUser.getUsername())) {
						anhkwStoProject = true;
						break;
					}
				}
				if (!proj.isPublik() && !anhkwStoProject) {
					System.out
							.println("den einai public kai eisai staff pou den anhkeis sto project");
					response.sendRedirect(HOME_SERVLET);
					return;
				}
				break;
			case MANAGER:
				request.setAttribute("manager", true);
				if (proj.getManager().getUsername()
						.equals(signedUser.getUsername())) {
					// Manager to this project
					request.setAttribute("projectManager", true);
					helperAllStaffForJobInitialization(request,
							(ArrayList<String>) request
									.getAttribute("addedStaff"), proj.getName());
				} else {
					if (!proj.isPublik()) {
						response.sendRedirect(HOME_SERVLET);
						return;
					}
				}
				break;
			case ADMIN:
				request.setAttribute("admin", true);
				break;
			}
		} catch (ServiceExDBFailure e) {
			log.debug("JobController::doGet", e);
			request.setAttribute("ErrorString", e.getMessage());
		}
		sc.getRequestDispatcher(JOB_JSP).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Job job = null;
		User signedUser = (User) (request.getSession(false)
				.getAttribute("signedUser"));
		if (signedUser == null) { // should not be
			response.sendRedirect(HOME_SERVLET);
			return;
		}
		int jobId = Integer.parseInt(request.getParameter("id"));

		try {
			// BEGIN : POST SPECIFIC
			// Add new comment
			if (request.getParameter("submitcomment") != null) {
				if (!Validators.isNullOrEmpty(request.getParameter("comment"))) {
					projectService.addComment(jobId,
							request.getParameter("comment"),
							signedUser.getUsername());
				}
				// sql sleeps ! we redirect to see the comment immediately
				// aparaithto!! giati den uparxoun ta pedia ths formas!! prepei
				// na pame sto get opwsdhpote!!
				response.sendRedirect(JOB_SERVLET + "?id=" + jobId);
				return;
			}

			Integer state = 0;
			// JAVA 7 ONLY !!!!
			// http://stackoverflow.com/questions/338206/switch-statement-with-strings-in-java
			switch (request.getParameter("state")) {
			case "new":
				state = StatesENUM.NEW.ordinal();
				break;
			case "started":
				state = StatesENUM.STARTED.ordinal();
				break;
			case "done":
				state = StatesENUM.DONE.ordinal();
				break;
			}

			// An path8hke to Update State of Job apo kapoion staff
			// member-enhmerwnw th bash
			if (request.getParameter("updateJobState") != null) {
				projectService.updateStateofJob(jobId, state);
			}
			// END : POST SPECIFIC

			Project proj = projectService.getProjectInfoByName(projectService
					.getProjectNameOfJob(jobId));
			job = projectService.getJobWithId(jobId);
			if (job == null || proj == null) {
				throw new ServiceExDBFailure();
			}

			RolesENUM role = signedUser.getRole();

			switch (role) {
			case GUEST:
				if (!proj.isPublik()) {
					response.sendRedirect(HOME_SERVLET);
					return;
				}
				request.setAttribute("guest", true);
				break;
			case STAFF:
				request.setAttribute("staff", true);
				boolean relative = false;
				for (User user : job.getJobStaff()) {
					if (user.getUsername().equals(signedUser.getUsername())) {
						request.setAttribute("relativeStaff", true);
						relative = true;
						break;
					}
				}
				if (!relative) {
					if (!proj.isPublik()) {
						response.sendRedirect(HOME_SERVLET);
						return;
					}
				}
				break;
			case MANAGER:
				request.setAttribute("manager", true);
				if (proj.getManager().getUsername()
						.equals(signedUser.getUsername())) {
					// Manager to this project
					request.setAttribute("projectManager", true);
					// helperAllStaffForJobInitialization(request,
					// (ArrayList<String>) request
					// .getAttribute("addedStaff"), proj.getName());
				} else {
					if (!proj.isPublik()) {
						response.sendRedirect(HOME_SERVLET);
						return;
					}
				}
				break;
			case ADMIN:
				request.setAttribute("admin", true);
				break;
			}

			String description = request.getParameter("description");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String staffMember = request.getParameter("staffMember");
			String deletedStaffMember = request
					.getParameter("deletedStaffMember");

			String[] addedStaff1 = request.getParameterValues("added");
			ArrayList<String> addedStaff = new ArrayList<>(); // never null ****
			if (addedStaff1 != null) {
				for (int i = 0; i < addedStaff1.length; ++i) {
					addedStaff.add(addedStaff1[i]);
				}
			}

			// AN PATH8HKE "ENHMERWSH"
			if (request.getParameter("updateJob") != null) {

				// Validation twn pediwn
				boolean error = false;
				if (Validators.isNullOrEmpty(description)) {
					request.setAttribute("emptyDescription", true);
					error = true;
				}
				if (addedStaff.isEmpty()) {
					request.setAttribute("emptyStaff", true);
					error = true;
				}
				DateTime startDateTime = null;
				if (Validators.isNullOrEmpty(startDate)) {
					request.setAttribute("emptyStartDate", true);
					error = true;
				} else {
					try {
						startDateTime = getDateFromString(startDate);
						// if (startDate.isBeforeNow()) {
						// request.setAttribute("startDateBeforeNow", true);
						// error = true;
						// }
					} catch (IllegalArgumentException e) {
						log.info("User entered malformed Start Date");
						request.setAttribute("malformedStartDate", true);
						error = true;
						startDateTime = null;
					}
				}
				DateTime endDateTime = null;
				if (Validators.isNullOrEmpty(endDate)) {
					request.setAttribute("emptyEndDate", true);
					error = true;
				} else {
					try {
						endDateTime = getDateFromString(endDate);
						if (startDateTime != null) {
							if (startDateTime.isAfter(endDateTime)) {
								request.setAttribute("endDateBeforeStartDate",
										true);
								error = true;
							}
						}
					} catch (IllegalArgumentException e) {
						log.info("User entered malformed End Date");
						request.setAttribute("malformedEndDate", true);
						error = true;
					}
				}
				// Enhmerwsh ths bashs me thn updated job
				if (!error) {
					job.setDescription(description);
					job.setStartDate(startDateTime);
					job.setEndDate(endDateTime);
					ArrayList<User> staff = getUsersFromUsernames(addedStaff);
					job.setJobStaff(staff);
					job.setState(StatesENUM.values()[state]);
					projectService.updateJob(job);
					response.sendRedirect(JOB_SERVLET + "?id=" + jobId);
					return;
				}
			}

			request.setAttribute("id", job.getId());
			request.setAttribute("name", job.getName());
			request.setAttribute("description", description);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("state", StatesENUM.values()[state]);
			ArrayList<Comment> comments = null;
			comments = projectService.getCommentsOfJob(job.getId());
			request.setAttribute("jobComments", comments);
			// if (request.getAttribute("projectManager") != null) {
			// this in doGet is called in the switch
			helperJobArrays(request, staffMember, deletedStaffMember,
					addedStaff, proj.getName());
			// }
		} catch (ServiceExDBFailure e) {
			log.debug("JobController::doPost", e);
			request.setAttribute("ErrorString", e.getMessage());
		}
		sc.getRequestDispatcher(JOB_JSP).forward(request, response);
	}

	/**
	 * Initializes basic project information common to all Roles that have
	 * access to the project - the initialization is for DoGet() (in general
	 * when the user has not changed any attributes) - otherwise the init must
	 * happen based on the request
	 * 
	 * @param request
	 * @param job
	 * @throws ServiceExDBFailure
	 */
	private void helperBasicJobInitialization(HttpServletRequest request,
			Job job) throws ServiceExDBFailure {
		request.setAttribute("id", job.getId());
		request.setAttribute("name", job.getName());
		request.setAttribute("description", job.getDescription());
		request.setAttribute("startDate", job.getStartDate().getYear() + "/"
				+ job.getStartDate().getMonthOfYear() + "/"
				+ job.getStartDate().getDayOfMonth());
		request.setAttribute("endDate", job.getEndDate().getYear() + "/"
				+ job.getEndDate().getMonthOfYear() + "/"
				+ job.getEndDate().getDayOfMonth());
		request.setAttribute("state", job.getState().toString());

		ArrayList<String> addedStaff = new ArrayList<String>();
		ArrayList<User> addedStaffUsers = job.getJobStaff();
		for (User a : addedStaffUsers) {
			addedStaff.add(a.getUsername());
		}
		request.setAttribute("addedStaff", addedStaff);

		ArrayList<Comment> comments = null;
		comments = projectService.getCommentsOfJob(job.getId());
		request.setAttribute("jobComments", comments);
	}

	/**
	 * Only the project manager - addedStaff must be initialized - if there are
	 * request parameters use helperJobArrays
	 * 
	 * @param request
	 * @param addedStaff
	 * @param projectName
	 * @throws ServiceExDBFailure
	 */
	private void helperAllStaffForJobInitialization(HttpServletRequest request,
			ArrayList<String> addedStaff, String projectName)
			throws ServiceExDBFailure {
		ArrayList<String> allStaff = new ArrayList<>();
		ArrayList<User> allStaffUsers = userService
				.getAllStaffForProject(projectName);
		for (User user : allStaffUsers) {
			allStaff.add(user.getUsername());
		}
		allStaff.removeAll(addedStaff);
		request.setAttribute("allStaff", allStaff);
	}

	/**
	 * Only the project manager - addedStaff must be initialized - caters for
	 * updates (added/deleted staff)
	 * 
	 * @param request
	 * @param staffMember
	 * @param deletedStaffMember
	 * @param addedStaff
	 * @param projectName
	 * @throws ServiceExDBFailure
	 */
	private void helperJobArrays(HttpServletRequest request,
			String staffMember, String deletedStaffMember,
			ArrayList<String> addedStaff, String projectName)
			throws ServiceExDBFailure {

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
		ArrayList<User> allStaffUsers = userService
				.getAllStaffForProject(projectName);
		for (User user : allStaffUsers) {
			allStaff.add(user.getUsername());
		}
		allStaff.removeAll(addedStaff);
		request.setAttribute("allStaff", allStaff);
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
