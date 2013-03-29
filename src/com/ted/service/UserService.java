package com.ted.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import com.ted.dao.DBExFailure;
import com.ted.dao.DBExUserExists;
import com.ted.dao.ProjectDAO;
import com.ted.dao.UserDAO;
import com.ted.domain.User;
import com.ted.domain.User.RolesENUM;

public class UserService {

	private UserDAO ud = new UserDAO();

	public User addUser(User u) throws ServiceExDBFailure, ServiceExUserExists {
		try {
			u.setPassword(Helpers.getHashedPassword(u.getPassword()));
			u = ud.insert(u);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		} catch (DBExUserExists e) {
			throw new ServiceExUserExists(e);
		}
		return u;
	}

	public boolean sendMailtoUser(User user, String subject, String message) {

		try {
			// Email message
			String toAddress = user.getEmail();
			String fromAddress = "ted.pm2012@yahoo.com";

			// Auth.
			String host = "smtp.mail.yahoo.com";
			String port = "465";
			String username = "ted.pm2012";
			String password = "projmanted";

			// Configure your JavaMail.
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtps");
			props.setProperty("mail.host", host);
			props.setProperty("mail.port", port);
			props.setProperty("mail.user", username);
			props.setProperty("mail.password", password);
			props.setProperty("mail.smtp.auth", "true");

			// Start an email session.
			Session session = Session.getDefaultInstance(props, null);
			Transport transport = session.getTransport("smtp");
			MimeMessage mimeMessage = new MimeMessage(session);
			Multipart multiPart = new MimeMultipart();

			mimeMessage.setSubject(subject);
			mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(
					toAddress));
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setContent(message, "text/plain; charset=ISO-8859-7");

			multiPart.addBodyPart(textBodyPart);
			mimeMessage.setContent(multiPart);
			mimeMessage.setFrom(new InternetAddress(fromAddress));

			// Send email.
			transport.connect("smtp.mail.yahoo.co.in", username, password);

			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			transport.close();
		} catch (MessagingException ex) {
			System.out.println(ex.toString());
			return false;
		}
		return true;
	}

	public User login(String username, String password)
			throws ServiceExDBFailure {
		User user = null;
		try {
			// user = ud.authenticate(username, password);
			user = ud.authenticate(username, null);
			if (user != null) {
				if (Helpers.isPasswordCorrect(password, user.getPassword())) {
					return user;
				}
			}
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		return null;
	}

	public boolean userExists(String username) throws ServiceExDBFailure {
		try {
			return ud.userExists(username);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public User getUserWithUsername(String username) throws ServiceExDBFailure {
		try {
			return ud.findByUsername(username);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public User updateUser(User user, String name, String surname,
			String email, String password) throws ServiceExDBFailure {
		try {
			return ud.updateUser(user, name, surname, email, password);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public ArrayList<User> allManagers() throws ServiceExDBFailure {

		ArrayList<User> managers = new ArrayList<>();
		try {
			managers = ud.findAll(RolesENUM.MANAGER);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		return managers;
	}

	public ArrayList<User> allStaff() throws ServiceExDBFailure {

		ArrayList<User> staff = new ArrayList<>();
		try {
			staff = ud.findAll(RolesENUM.STAFF);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		return staff;
	}

	public ArrayList<User> getAllStaffForProject(String project)
			throws ServiceExDBFailure {
		ArrayList<User> staff = new ArrayList<>();
		try {
			staff = ud.findAllForProject(project);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		return staff;
	}

	public ArrayList<User> getAllStaffForJob(Integer id)
			throws ServiceExDBFailure {
		ArrayList<User> staff = new ArrayList<>();
		try {
			staff = ud.findAllForJob(id);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		return staff;
	}

	public ArrayList<User> allUsersExceptAdmin() throws ServiceExDBFailure {

		ArrayList<User> allUsers = new ArrayList<>();
		try {
			allUsers = ud.findAllExceptRole(RolesENUM.ADMIN);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		return allUsers;
	}

	public boolean isManagerInProject(String username)
			throws ServiceExDBFailure {
		HashSet<String> projectManagers;
		try {
			projectManagers = (new ProjectDAO()).getAllManagers();
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		if (projectManagers.contains(username))
			return true;
		return false;
	}

	public boolean isMemberOfProject(String username) throws ServiceExDBFailure {
		HashSet<String> projectManagers;
		try {
			projectManagers = (new ProjectDAO()).getAllStaff();
			projectManagers.addAll((new ProjectDAO()).getAllManagers());
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		if (projectManagers.contains(username))
			return true;
		return false;
	}

	public User updateRole(User user, RolesENUM role)
			throws ServiceExUserIsProjectManager, ServiceExDBFailure {
		try {
			if (isMemberOfProject(user.getUsername()))
				throw new ServiceExUserIsProjectManager();
			ud.updateRole(user, role);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		user.setRole(role);
		return user;
	}

	/**
	 * 
	 * @param user
	 * @return true an ton esbhse , false an den epitrepetai na sbhstei ,
	 *         exception an egine sfalma sth bash
	 * @throws ServiceExDBFailure
	 *             an egine sfalma sth bash
	 */
	public boolean deleteUser(String username) throws ServiceExDBFailure {
		if (isMemberOfProject(username) || isManagerInProject(username)) {
			return false;
		}
		try {
			new ProjectService().deleteCommentsOfUser(username);
			ud.delete(username);
			return true;
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}

	}

	static class Helpers {

		/**
		 * Hashes a password with a random salt (oi prwtoi 8 xarakthres toy
		 * hashed pass einai to salt poy xrhsimopoih8hke)
		 * 
		 * @param password
		 *            to password pou 8es hasharismeno
		 * @return to hasharismeno pass(oi prwtoi 8 xarakthres toy einai to salt
		 *         poy xrhsimopoih8hke)
		 */
		static public String getHashedPassword(String password) {
			String salt;
			Random rand = new Random(System.nanoTime());

			// dhmiourgw to salt(8 xarakthres apo a ews z)
			salt = new String();
			for (int i = 0; i < 8; i++) {
				salt += (char) (rand.nextInt(26) + 97);
			}
			byte[] bSalt = salt.getBytes();

			// pairnw to hash tou pass xrhsimopoiwntas to salt pou brhka prin
			byte[] hashedpass = getHash(password, bSalt);

			// Pairnoyme th hex anaparasth tou hashedpass
			String hexString = new String();
			for (int i = 0; i < hashedpass.length; i++) {
				hexString += Integer.toHexString(0xFF & hashedpass[i]);
			}

			return salt + hexString.toString();
		}

		/**
		 * Elegxei an to passwordFromUser tairiazei me ayto pou exei
		 * apo8hkeymeno(kai hasharismeno h bash)
		 * 
		 * @param passwordFromUser
		 *            to password pou 8a tsekaroyme an einai to swsto
		 * @param passwordFromDataBase
		 *            to apo8hkeumeno password enos xrhsth
		 * @return true an passwordFromUser tairiazei me ayto pou exei h bash,
		 *         false alliws
		 */
		static public boolean isPasswordCorrect(String passwordFromUser,
				String passwordFromDataBase) {
			// Ksexwrizeis to salt kai to hasharismeno pass apo to String pou
			// exei
			// apo8hkeymeno h bash
			String basesalt = passwordFromDataBase.substring(0, 8);
			String basehasshedpass = passwordFromDataBase.substring(8);

			// Briskeis to hash toy pass pou edwse o user,xrhsimopoiwntas to
			// salt
			// pou phres apo bash
			byte[] bSalt = basesalt.getBytes();
			byte[] hashedpass = getHash(passwordFromUser, bSalt);

			// Pairneis th hex anaparastash toy hased password toy xrhsth
			String hexString = new String();
			for (int i = 0; i < hashedpass.length; i++) {
				hexString += (Integer.toHexString(0xFF & hashedpass[i]));
			}

			System.out.println(hexString);
			// Elegxeis gia isothta me ayto poy einai apo8hkeymeno sth bash
			if (basehasshedpass.equals(hexString)) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * Dinei to hash enos password xrhsimopoiwntas to dosmeno salt
		 * 
		 * @param password
		 * @param salt
		 * @return hasharismeno to pass xrhsimopoiwntas to dosmeno salt
		 */
		static private byte[] getHash(String password, byte[] salt) {
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				digest.reset();
				digest.update(salt);
				return digest.digest(password.getBytes("UTF-8"));
			} catch (NoSuchAlgorithmException ex) {
				// TODO
				return null;
			} catch (UnsupportedEncodingException ex) {
				// TODO
				return null;
			}
		}
	}

}
