package com.ted.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ted.domain.User;
import com.ted.domain.User.RolesENUM;

public class UserDAO {

	public List<User> findAll(RolesENUM role) throws DBExFailure {
		List<User> uList = null;
		Connection conn = DBConnectionPool.getConnection();
		Statement statement = null;
		ResultSet set = null;
		String query = "SELECT * FROM users";
		if (role != null) {
			query += " WHERE role = " + role.ordinal();
		}
		if (conn != null) {
			try {
				statement = conn.createStatement();
				set = statement.executeQuery(query);
				uList = new ArrayList<User>();
				while (set.next()) {
					User user = new User();
					// user.setId(set.getInt("ID"));
					user.setUsername(set.getString("username"));
					user.setName(set.getString("name"));
					user.setSurname(set.getString("surname"));
					user.setPassword(set.getString("password"));
					user.setEmail(set.getString("email"));
					user.setRole(RolesENUM.values()[set.getInt("role")]);
					uList.add(user);
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return uList;
	}

	public List<User> findAllForProject(String project) throws DBExFailure {
		List<User> uList = null;
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT user FROM projhasstaff WHERE project=?";
		if (conn != null) {
			try {
				statement = conn.prepareStatement(query);
				statement.setString(1, project);
				set = statement.executeQuery();
				uList = new ArrayList<User>();
				while (set.next()) {
					User user = findByUsername(set.getString("user"));
					uList.add(user);
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return uList;
	}

	public List<User> findAllForJob(Integer id) throws DBExFailure {
		List<User> uList = null;
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT user FROM jobhasstaff WHERE jobs=?";
		if (conn != null) {
			try {
				statement = conn.prepareStatement(query);
				statement.setInt(1, id);
				set = statement.executeQuery();
				uList = new ArrayList<User>();
				while (set.next()) {
					User user = findByUsername(set.getString("user"));
					uList.add(user);
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return uList;
	}

	public List<User> findAllExceptRole(RolesENUM role) throws DBExFailure {
		List<User> uList = null;
		Connection conn = DBConnectionPool.getConnection();
		Statement statement = null;
		ResultSet set = null;
		String query = "SELECT * FROM users";
		if (role != null) {
			query += " WHERE role <> " + role.ordinal();
		}
		if (conn != null) {
			try {
				statement = conn.createStatement();
				set = statement.executeQuery(query);
				uList = new ArrayList<User>();
				while (set.next()) {
					User user = new User();
					// user.setId(set.getInt("ID"));
					user.setUsername(set.getString("username"));
					user.setName(set.getString("name"));
					user.setSurname(set.getString("surname"));
					user.setPassword(set.getString("password"));
					user.setEmail(set.getString("email"));
					user.setRole(RolesENUM.values()[set.getInt("role")]);
					uList.add(user);
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return uList;
	}

	public User findByUsername(String username) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM users WHERE username=?";
		if (conn != null) {
			try {
				statement = conn.prepareStatement(query);
				statement.setString(1, username);
				System.out.println(statement);
				set = statement.executeQuery();
				if (set.next()) {
					User user = new User();
					// user.setId(set.getInt("ID"));
					user.setUsername(set.getString("username"));
					user.setName(set.getString("name"));
					user.setSurname(set.getString("surname"));
					user.setPassword(set.getString("password"));
					user.setEmail(set.getString("email"));
					user.setRole(RolesENUM.values()[set.getInt("role")]);
					return user;
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return null;
	}

	public boolean userExists(String username) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM users WHERE username=?";
		if (conn != null) {
			try {
				statement = conn.prepareStatement(query);
				statement.setString(1, username);
				set = statement.executeQuery();
				return set.next();
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		} else {
			throw new DBExFailure();
		}
	}

	public User insert(User user) throws DBExFailure, DBExUserExists {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "INSERT INTO users (email, password, name, surname, username, role) VALUES (?,?,?,?,?,?)";
		try {
			statement = conn.prepareStatement(query);
			statement.setString(1, user.getEmail());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getName());
			statement.setString(4, user.getSurname());
			statement.setString(5, user.getUsername());
			statement.setInt(6, user.getRole().ordinal());
			System.out.println(statement);
			statement.executeUpdate();
			// user.setId(set.getInt(1));
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) {
				throw new DBExUserExists(e);
			} else {
				throw new DBExFailure(e);
			}
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return user;
	}

	public User updateRole(User user, RolesENUM role) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "UPDATE users SET role = ? WHERE username = ?";
		try {
			statement = conn.prepareStatement(query);
			statement.setInt(1, role.ordinal());
			statement.setString(2, user.getUsername());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return user;
	}

	public User updateUser(User user, String name, String surname,
			String email, String password) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "UPDATE users SET name = ?, surname = ?, email = ?,  password = ? WHERE username = ?";
		try {
			int update = 0;
			// if (!user.getName().equals(name)) {
			// query += "SET name = ? ";
			statement = conn.prepareStatement(query);
			statement.setString(++update, name);
			// }
			// if (!user.getSurname().equals(surname)) {
			// query += "SET surname = ? ";
			statement.setString(++update, surname);
			// }
			// if (!user.getEmail().equals(email)) {
			// query += "SET email = ? ";
			statement.setString(++update, email);
			// }
			// if (!user.getPassword().equals(password)) {
			// query += "SET password = ? ";
			statement.setString(++update, password);
			// }
			// if (update > 0) {
			// query += " WHERE username = ? ";
			statement.setString(++update, user.getUsername());
			System.out.println(statement);
			statement.executeUpdate();
			user.setName(name);
			user.setSurname(surname);
			user.setEmail(email);
			user.setPassword(password);
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return user;
	}

	/**
	 * @param username
	 * @param password
	 * @return null if the user was not found
	 * @throws DBExFailure
	 */
	public User authenticate(String username, String password)
			throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		// final String query =
		// "SELECT * FROM users WHERE username =? AND password=? LIMIT 1";
		final String query = "SELECT * FROM users WHERE username =? LIMIT 1";
		User user = null;
		try {
			statement = conn.prepareStatement(query);
			statement.setString(1, username);
			// statement.setString(2, password);
			System.out.println(statement);
			set = statement.executeQuery();
			while (set.next()) {
				user = new User();
				user.setUsername(set.getString("username"));
				user.setPassword(set.getString("password"));
				user.setEmail(set.getString("email"));
				user.setName(set.getString("name"));
				user.setSurname(set.getString("surname"));
				user.setRole(RolesENUM.values()[set.getInt("role")]);
			}
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return user;
	}

	public void delete(String username) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "DELETE from users where username = ?";
		try {
			statement = conn.prepareStatement(query);
			int i = 1;
			statement.setString(i, username);
			System.out.println("UserDAO.delete() : " + statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
	}
}
