package com.ted.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ted.domain.User;
import com.ted.domain.User.RolesENUM;
import com.ted.service.ServiceExDBFailure;
import com.ted.service.ServiceExUserExists;
import com.ted.service.UserService;
import com.ted.validators.Validators;

@WebServlet("/register")
public class RegisterController extends Controller {

	private static final long serialVersionUID = 4414683568007415540L;
	private UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// disallow GET
		sc.getRequestDispatcher(REGISTER_JSP).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String username = null, password1 = null, password2 = null, name = null, surname = null, email = null;
		username = request.getParameter("username");
		password1 = request.getParameter("password1");
		password2 = request.getParameter("password2");
		name = request.getParameter("name");
		surname = request.getParameter("surname");
		email = request.getParameter("email");
		// term = request.getParameter("agree"); // terms of service
		boolean error = false;
		// check if password1 is too small
		if (Validators.isNullOrEmpty(password1)) {
			request.setAttribute("emptyPassword1", true);
			error = true;
		} else if (Validators.isPasswordTooShort(password1)) {
			request.setAttribute("smallPassword1", true);
			error = true;
		} else if (!password1.equals(password2)) {
			request.setAttribute("diffPasswords", true);
			error = true;
		}
		// check if password2 is too small
		if (Validators.isNullOrEmpty(password2)) {
			request.setAttribute("emptyPassword2", true);
			error = true;
		} else if (Validators.isPasswordTooShort(password2)) {
			request.setAttribute("smallPassword2", true);
			error = true;
		}
		// check if name is empty
		if (Validators.isNullOrEmpty(name)) {
			request.setAttribute("emptyName", true);
			error = true;
		}
		// check if surname is empty
		if (Validators.isNullOrEmpty(surname)) {
			request.setAttribute("emptySurname", true);
			error = true;
		}
		// check if email is empty
		if (Validators.isNullOrEmpty(email)) {
			request.setAttribute("emptyEmail", true);
			error = true;
		} else if (Validators.isEmailInvalid(email)) {
			request.setAttribute("invalidEmail", true);
			error = true; // a way to get rid of this ?
		}
		// check if username is empty
		if (Validators.isNullOrEmpty(username)) {
			request.setAttribute("emptyUsername", true);
			error = true;
		} else if (Validators.isUsernameameTooShort(username)) {
			request.setAttribute("smallUsername", true);
			error = true;
		} else {
			UserService us = new UserService();
			try {
				if (us.userExists(username)) {
					request.setAttribute("duplicateUsername", true);
					error = true;
				}
			} catch (ServiceExDBFailure e) {
				request.setAttribute("StringErrorInRegistration",
						e.getMessage());
				log.debug("RegisterController::doPost", e);
				error = true;
			}
		}
		// no errors - persist the user
		if (!error) {
			User signedUser = new User();
			signedUser.setUsername(username);
			signedUser.setPassword(password1);
			signedUser.setName(name);
			signedUser.setSurname(surname);
			signedUser.setEmail(email);
			signedUser.setRole(RolesENUM.GUEST);
			try {
				signedUser = userService.addUser(signedUser);
				userService.sendMailtoUser(signedUser, "welcome",
						"Συγχαρητηρια για την εγγραφή σου");
				// HttpSession session = request.getSession(false);
				// Random rn = new Random();
				// int r = rn.nextInt();
				// session.setAttribute("state", r);
				// session.setAttribute("signedUser", signedUser);
				// session.setAttribute("id", signedUser.getId());
				response.sendRedirect(HOME_SERVLET);
				return;
			} catch (ServiceExDBFailure | ServiceExUserExists e) {
				log.debug("RegisterController::doPost", e);
				request.setAttribute("StringErrorInRegistration",
						e.getMessage());
			}
		}
		request.setAttribute("username", username);
		request.setAttribute("password1", password1);
		request.setAttribute("password2", password2);
		request.setAttribute("name", name);
		request.setAttribute("surname", surname);
		request.setAttribute("email", email);
		RequestDispatcher rd = sc.getRequestDispatcher(REGISTER_JSP);
		rd.forward(request, response);
		return;
	}
}
