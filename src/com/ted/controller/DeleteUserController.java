package com.ted.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ted.helpers.Helpers;
import com.ted.service.ServiceExDBFailure;
import com.ted.service.UserService;

@WebServlet("/deleteuser")
public class DeleteUserController extends Controller {

	private static final long serialVersionUID = -8474121954416242060L;
	private UserService userService = new UserService();

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		final String userToDelete = Helpers.decodeRequest(request
				.getParameter("user"));
		try {
			if (userService.deleteUser(userToDelete)) {
				System.out.println("Esbhsa ton " + userToDelete);
				response.sendRedirect(USERLIST_SERVLET);
				return;
			} else {
				request.setAttribute("ErrorString",
						"Δεν μπορεί να διαγραφεί ο χρήστης - συμμετέχει σε έργα");
			}
		} catch (ServiceExDBFailure e) {
			log.debug("DeleteUserController::doPost", e);
			request.setAttribute("ErrorString", e.getMessage());
		}
		sc.getRequestDispatcher(USERLIST_JSP).forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
