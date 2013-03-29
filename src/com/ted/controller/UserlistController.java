package com.ted.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ted.domain.User;
import com.ted.service.ServiceExDBFailure;
import com.ted.service.UserService;

@WebServlet("/userlist")
public class UserlistController extends Controller {
	private static final long serialVersionUID = 6985171580055528176L;
	private UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ArrayList<User> allUsers = null;
		try {
			allUsers = userService.allUsersExceptAdmin();
			// ArrayList<String> allUsers = new ArrayList<>();
			// for (User user : all) {
			// allUsers.add(user.getUsername());
			// }
			request.setAttribute("allUsers", allUsers);
		} catch (ServiceExDBFailure e) {
			log.debug("UserlistController::doGet", e);
			request.setAttribute("ErrorString", e.getMessage());
		}
		sc.getRequestDispatcher(USERLIST_JSP).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
