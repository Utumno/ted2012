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
import com.ted.domain.User;
import com.ted.helpers.Helpers;
import com.ted.service.ProjectService;
import com.ted.service.ServiceExDBFailure;
import com.ted.service.ServiceExJobExists;
import com.ted.service.UserService;
import com.ted.validators.Validators;

@WebServlet("/createjob")
public class CreateJobController extends Controller {

	private static final long serialVersionUID = 350118892218083137L;
	private UserService userService = new UserService();
	private ProjectService projectService = new ProjectService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("project", request.getAttribute("project"));
		helperJobArrays(request, null, new ArrayList<String>(), null);
		sc.getRequestDispatcher(CREATE_JOB_JSP).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Briskw project kai 8etw attributes gia to jsp
		Project enclosingProject = null;
		try {
			enclosingProject = projectService.getProjectInfoByName(request
					.getParameter("project"));
		} catch (ServiceExDBFailure e1) {
			request.setAttribute("ErrorString", e1.getMessage());
			sc.getRequestDispatcher(CREATE_JOB_JSP).forward(request, response);
			return;
		}
		request.setAttribute("project", request.getParameter("project"));

		String name = null, description = null, staffMember = null;
		DateTime startDate = null, endDate = null;
		String[] addedStaff1 = request.getParameterValues("added");
		ArrayList<String> addedStaff = new ArrayList<>();
		if (addedStaff1 != null) {
			for (int i = 0; i < addedStaff1.length; ++i) {
				addedStaff.add(addedStaff1[i]);
			}
		}

		name = request.getParameter("name");
		description = request.getParameter("description");
		staffMember = request.getParameter("staffMember");
		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		String deletedStaffMember = request.getParameter("deletedStaffMember");

		// AN PATH8HKE TO DHMIOYRGIA
		if (request.getParameter("createJob") != null) {
			boolean error = false;

			// elegxos twn pediwn
			if (Validators.isNullOrEmpty(name)) {
				request.setAttribute("emptyName", true);
				error = true;
			} else
				try {
					if (projectService.jobExistsWithThisNameForThisProject(
							enclosingProject, name)) {
						request.setAttribute("jobNameExists", true);
						error = true;
					}
				} catch (ServiceExDBFailure e) {
					log.debug("CreateJobController::doPost", e);
					request.setAttribute("StringErrorInJobCreation",
							e.getMessage());
				}

			if (Validators.isNullOrEmpty(description)) {
				request.setAttribute("emptyDescription", true);
				error = true;
			}

			if (Validators.isNullOrEmpty(startDateStr)) {
				request.setAttribute("emptyStartDate", true);
				error = true;
			} else {
				try {
					startDate = getDateFromString(request
							.getParameter("startDate"));
					// if (startDate.isBeforeNow()) {
					// request.setAttribute("startDateBeforeNow", true);
					// error = true;
					// }
				} catch (IllegalArgumentException e) {
					log.info("User entered malformed Start Date");
					request.setAttribute("malformedStartDate", true);
					error = true;
					startDate = null;
				}
			}

			if (Validators.isNullOrEmpty(endDateStr)) {
				request.setAttribute("emptyEndDate", true);
				error = true;
			} else {
				try {
					endDate = getDateFromString(request.getParameter("endDate"));
					if (startDate != null) {
						if (startDate.isAfter(endDate)) {
							request.setAttribute("endDateBeforeStartDate", true);
							error = true;
						}
					}
				} catch (IllegalArgumentException e) {
					log.info("User entered malformed End Date");
					request.setAttribute("malformedEndDate", true);
					error = true;
				}
			}

			if (addedStaff.isEmpty()) {
				request.setAttribute("emptyStaff", true);
				error = true;
			}

			// an ola kala...apo8hkeush tou job
			if (!error) {
				Job job = enclosingProject.new Job();
				job.setName(name);
				job.setDescription(description);
				job.setStartDate(startDate);
				job.setEndDate(endDate);
				try {
					ArrayList<User> staff = getUsersFromUsernames(addedStaff);
					job.setJobStaff(staff);
					projectService.addJob(enclosingProject, job);
					response.sendRedirect(Helpers.encodeUri(PROJECT_SERVLET
							+ "?name=", enclosingProject.getName()));
					return;
				} catch (ServiceExDBFailure | ServiceExJobExists e) {
					log.debug("CreateJobController::doPost", e);
					request.setAttribute("ErrorString", e.getMessage());
				}
			}
		}

		request.setAttribute("name", name);
		request.setAttribute("description", description);
		request.setAttribute("startDate", startDateStr);
		request.setAttribute("endDate", endDateStr);
		helperJobArrays(request, staffMember, addedStaff, deletedStaffMember);
		sc.getRequestDispatcher(CREATE_JOB_JSP).forward(request, response);
		return;
	}

	private void helperJobArrays(HttpServletRequest request,
			String staffMember, ArrayList<String> addedStaff,
			String deletedStaffMember) {

		try {
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
					.getAllStaffForProject(request.getParameter("project"));
			for (User user : allStaffUsers) {
				allStaff.add(user.getUsername());
			}
			allStaff.removeAll(addedStaff);
			request.setAttribute("allStaff", allStaff);
		} catch (ServiceExDBFailure e) {
			log.debug("CreateJobController::helperJobArrays", e);
			request.setAttribute("ErrorString", e.getMessage());
		}
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
