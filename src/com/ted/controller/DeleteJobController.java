package com.ted.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ted.domain.Project.Job;
import com.ted.helpers.Helpers;
import com.ted.service.ProjectService;
import com.ted.service.ServiceExDBFailure;

@WebServlet("/deletejob")
public class DeleteJobController extends Controller {

	private static final long serialVersionUID = -2702849042443709119L;
	private static final String confirmationOfSingleJobDeletion = "Είστε σίγουρος οτι θέλετε να διαγράψετε την εργασία ";
	private static final String confirmationOfMultipleJobsDeletion = "Είστε σίγουρος οτι θέλετε να διαγράψετε τις εργασίες ";
	private static final String succesOfMultipleJobsDeletion = "Οι εργασίες διεγράφησαν !";
	private static final String succesOfOfSingleJobDeletion = "Η εργασία διεγράφη !";
	private final String servlet = PROJECT_SERVLET;
	private ProjectService projectService = new ProjectService();

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String projNameOfJobToBeDeleted = null;
		String redirectURIIfNotDeleting = null;
		String redirectURIAfterDeleting = null;
		Integer idOfJobToDelete = null;
		try {
			// pairnw oles tis jobs pou exoun epilegei kai tis diagrafw 1-1
			String[] jobsIdsToDelete = request
					.getParameterValues("arrayOfKeysOfObjectsToBeDeleted");
			if (jobsIdsToDelete != null) {
				idOfJobToDelete = Integer.parseInt(jobsIdsToDelete[0]);
				projNameOfJobToBeDeleted = projectService
						.getProjectNameOfJob(idOfJobToDelete);
				redirectURIAfterDeleting = Helpers.encodeUri(PROJECT_SERVLET
						+ "?name=", projNameOfJobToBeDeleted);
				for (String jobId : jobsIdsToDelete) {
					idOfJobToDelete = Integer.parseInt(jobId);
					Job job = projectService.getJobWithId(idOfJobToDelete);
					projectService.deleteJob(job);
				}
				String message = (jobsIdsToDelete.length == 1) ? succesOfOfSingleJobDeletion
						: succesOfMultipleJobsDeletion;
				String rand = messageKey(request, message);
				response.sendRedirect(redirectURIAfterDeleting + "&r=" + rand);
				// // SINCE THE ORIGINAL REQUEST WAS post THIS WILL CALL
				// doPost()
				// //
				// http://stackoverflow.com/questions/4374548/does-jspforward-or-requestdispatcher-forward-use-get-or-post
				// // SO WE MUST SET ALL THE PARAMS FOR POST
				// sc.getRequestDispatcher(
				// Helpers.encodeUri("/" + PROJECT_SERVLET + "?name=",
				// projNameOfJobToBeDeleted)).forward(request,
				// response);
				return;
			}
			jobsIdsToDelete = request.getParameterValues("deleteJobId");
			// An den exei epilegei kamia job
			if (jobsIdsToDelete == null) {
				response.sendRedirect(Helpers.encodeUri(PROJECT_SERVLET
						+ "?name=", request.getParameter("project")));
				return;
			}
			int numOfJobs = jobsIdsToDelete.length;
			idOfJobToDelete = Integer.parseInt(jobsIdsToDelete[0]);
			projNameOfJobToBeDeleted = projectService
					.getProjectNameOfJob(idOfJobToDelete);
			if (projNameOfJobToBeDeleted != null) {
				List<Job> jobsToBeDeleted = new ArrayList<>();
				for (String jobId : jobsIdsToDelete) {
					idOfJobToDelete = Integer.parseInt(jobId);
					Job job = projectService.getJobWithId(idOfJobToDelete);
					jobsToBeDeleted.add(job);
				}
				String message = (numOfJobs == 1) ? confirmationOfSingleJobDeletion
						: confirmationOfMultipleJobsDeletion;
				request.setAttribute("confirmationMessage", message);
				request.setAttribute("arrayOfObjectsToDelete", jobsToBeDeleted);
				redirectURIIfNotDeleting = Helpers.encodeUri("",
						projNameOfJobToBeDeleted);
				request.setAttribute("redirectUrlIfUserDecidesNOTToDelete",
						redirectURIIfNotDeleting);
				request.setAttribute("servlet", servlet);
				request.setAttribute("redirectUrlIfUserDecidesTODelete",
						"deletejob");
				sc.getRequestDispatcher(CONFIRM_JOB_DELETION_JSP).forward(
						request, response);
				return;
			}
		} catch (ServiceExDBFailure e) {
			log.debug("DeleteJobController::doPost", e);
			request.setAttribute("ErrorString", e.getMessage());
			if (projNameOfJobToBeDeleted != null) { // to see the error
				response.sendRedirect(Helpers.encodeUri(PROJECT_SERVLET
						+ "?name=", projNameOfJobToBeDeleted));
				return;
			}
		}
		response.sendRedirect(Helpers.encodeUri(PROJECT_SERVLET + "?name=",
				request.getParameter("project")));
		return;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String projNameOfJobToBeDeleted = null;
		try {
			Integer idOfJobToDelete = Integer.parseInt(request
					.getParameter("id"));
			if (idOfJobToDelete == null) {
				response.sendRedirect(HOME_SERVLET);
				return;
			}
			projNameOfJobToBeDeleted = projectService
					.getProjectNameOfJob(idOfJobToDelete);
			if (projNameOfJobToBeDeleted != null) {
				Job jobToBeDeleted = projectService
						.getJobWithId(idOfJobToDelete);
				String message = confirmationOfSingleJobDeletion;
				request.setAttribute("confirmationMessage", message);
				List<Job> jobsToBeDeleted = new ArrayList<>();
				jobsToBeDeleted.add(jobToBeDeleted);
				request.setAttribute("arrayOfObjectsToDelete", jobsToBeDeleted);
				String redirectURIIfNotDeleting = Helpers.encodeUri("",
						projNameOfJobToBeDeleted);
				request.setAttribute("redirectUrlIfUserDecidesNOTToDelete",
						redirectURIIfNotDeleting);
				request.setAttribute("servlet", servlet);
				request.setAttribute("redirectUrlIfUserDecidesTODelete",
						"deletejob");
				sc.getRequestDispatcher(CONFIRM_JOB_DELETION_JSP).forward(
						request, response);
				return;
			}
		} catch (ServiceExDBFailure e) { // to see the error
			log.debug("DeleteJobController::doGet", e);
			request.setAttribute("ErrorString", e.getMessage());
			if (projNameOfJobToBeDeleted != null) {
				response.sendRedirect(Helpers.encodeUri(PROJECT_SERVLET
						+ "?name=", projNameOfJobToBeDeleted));
				return;
			}
		}
		response.sendRedirect(HOME_SERVLET); // maybe malicious call
		return;
	}
}
