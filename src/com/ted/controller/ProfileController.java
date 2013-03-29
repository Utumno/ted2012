package com.ted.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ted.domain.User;
import com.ted.domain.User.RolesENUM;
import com.ted.helpers.Helpers;
import com.ted.service.ServiceExDBFailure;
import com.ted.service.ServiceExUserIsProjectManager;
import com.ted.service.UserService;
import com.ted.validators.Validators;

@WebServlet("/profile")
public class ProfileController extends Controller {

	private static final long serialVersionUID = -4972499478831102726L;
	private UserService userService = new UserService();

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		final String username = request.getParameter("user");
		System.out.println(username);
		if (username != null) {
			User currentUser = (User) request.getSession(false).getAttribute(
					"signedUser");
			if (!RolesENUM.ADMIN.equals(currentUser.getRole())) {
				request.setAttribute("notAdmin", true);
				if (!RolesENUM.MANAGER.equals(currentUser.getRole())) {
					response.sendRedirect(HOME_SERVLET);
					return;
				}
			} else {
				request.setAttribute("admin", true);
			}
			User user = null;
			try {
				user = userService.getUserWithUsername(username);
				// An path8hke to update(gia to rolo enos xrhsth)
				if (request.getParameter("updateProfile") != null) {
					// allakse o admin to rolo tou xrhsth
					// ara to apo8hkeuoume sth bash kai ksanapairnoume ton user
					RolesENUM role = null;
					String roleParam = request.getParameter("role");
					if (roleParam != null) {
						if (roleParam.equals("Guest")) {
							role = RolesENUM.GUEST;
						} else if (roleParam.equals("Staff")) {
							role = RolesENUM.STAFF;
						} else if (roleParam.equals("Manager")) {
							role = RolesENUM.MANAGER;
						}
						user = userService.updateRole(user, role);
					}
				}
				if (userService.isMemberOfProject(username))
					request.setAttribute("cannotUpdateRole", true);
			} catch (ServiceExDBFailure | ServiceExUserIsProjectManager e) {
				log.debug("ProfileController::doPost", e);
				request.setAttribute("ErrorString", e.getMessage());
			}
			request.setAttribute("userToShow", user);
			sc.getRequestDispatcher(OTHERPROFILE_JSP)
					.forward(request, response);
			return;
		} else {
			// update to profile mou
			if (request.getParameter("updateMyProfile") != null) {
				User me = (User) request.getSession()
						.getAttribute("signedUser");

				String name = null, surname = null, email = null;
				name = request.getParameter("name");
				surname = request.getParameter("surname");
				email = request.getParameter("email");
				System.out.println("phra " + name + surname + email);

				// validation
				boolean error = false;
				if (Validators.isNullOrEmpty(name)) {
					request.setAttribute("emptyName", true);
					error = true;
				}
				if (Validators.isNullOrEmpty(surname)) {
					request.setAttribute("emptySurname", true);
					error = true;
				}
				if (Validators.isNullOrEmpty(email)) {
					request.setAttribute("emptyEmail", true);
					error = true;
				} else if (Validators.isEmailInvalid(email)) {
					request.setAttribute("invalidEmail", true);
					error = true;
				}
				if (!error) {
					try {
						me = userService.updateUser(me, name, surname, email,
								me.getPassword());
						request.getSession(false)
								.setAttribute("signedUser", me);
					} catch (ServiceExDBFailure e) {
						log.debug("ProfileController::doPost", e);
						request.setAttribute("ErrorString", e.getMessage());
					}
				}
				request.setAttribute("username", me.getUsername());
				request.setAttribute("name", name);
				request.setAttribute("surname", surname);
				request.setAttribute("email", email);
				request.setAttribute("role", me.getRole());
			}
			sc.getRequestDispatcher(MYPROFILE_JSP).forward(request, response);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		final String username = Helpers.decodeRequest(request
				.getParameter("user"));
		if (username != null) {
			User currentUser = (User) request.getSession(false).getAttribute(
					"signedUser");
			if (!RolesENUM.ADMIN.equals(currentUser.getRole())) {
				request.setAttribute("notAdmin", true);
				if (!RolesENUM.MANAGER.equals(currentUser.getRole())) {
					response.sendRedirect(HOME_SERVLET);
					return;
				}
			} else {
				request.setAttribute("admin", true);
			}
			User user = null;
			try {
				System.out
						.println("ProfileController.doGet() user name DECODED : "
								+ username);
				user = userService.getUserWithUsername(username);
				System.out.println("ProfileController.doGet() user : " + user);
				request.setAttribute("userToShow", user);
				if (userService.isMemberOfProject(username))
					request.setAttribute("cannotUpdateRole", true);
			} catch (ServiceExDBFailure e) {
				log.debug("ProfileController::doGet", e);
				request.setAttribute("ErrorString", e.getMessage());
			}
			sc.getRequestDispatcher(OTHERPROFILE_JSP)
					.forward(request, response);
			return;
		} else {
			User userToShow = (User) request.getSession(false).getAttribute(
					"signedUser");
			request.setAttribute("username", userToShow.getUsername());
			request.setAttribute("name", userToShow.getName());
			request.setAttribute("surname", userToShow.getSurname());
			request.setAttribute("email", userToShow.getEmail());
			request.setAttribute("role", userToShow.getRole());
			sc.getRequestDispatcher(MYPROFILE_JSP).forward(request, response);
			return;
		}
	}
}
