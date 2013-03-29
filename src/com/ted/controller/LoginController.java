package com.ted.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ted.domain.User;
import com.ted.service.ServiceExDBFailure;
import com.ted.service.UserService;
import com.ted.validators.Validators;

@WebServlet("/login")
public class LoginController extends Controller {

	private static final long serialVersionUID = -8420055833341622782L;

	private UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		sc.getRequestDispatcher(HOME_JSP).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		boolean error = false;
		if (Validators.isNullOrEmpty(username)) {
			request.setAttribute("emptyUsername", true);
			error = true;
		} else {
			request.setAttribute("username", username);
		}
		if (Validators.isNullOrEmpty(password)) {
			request.setAttribute("emptyPassword", true);
			error = true;
		}
		// errorMessage default - won't be used if(!error)
		String errorMessage = "";
		if (!error) {
			User user = null;
			try {
				user = userService.login(username, password);
				if ((user != null)) {
					// HERE WE CREATE A SESSION FOR THE FIRST TIME !
					HttpSession session = request.getSession();
					session.setAttribute("signedUser", user);
					System.out.println("SESSION CREATED  - user : "
							+ session.getAttribute("signedUser"));
					// send redirect so the session is acknowledged by the
					// browser
					response.sendRedirect(HOME_SERVLET);
					return;
				} else {
					errorMessage = "Username or password incorrect";
					System.out.println("username h pass incorrect");
				}
			} catch (ServiceExDBFailure e) {
				log.debug("LoginController::doPost", e);
				user = null; // to be sure
				errorMessage = "DB failure";
			}
		}
		RequestDispatcher rd = sc.getRequestDispatcher(HOME_JSP);
		request.setAttribute("LoginFailed", errorMessage);
		rd.forward(request, response);
	}
}
