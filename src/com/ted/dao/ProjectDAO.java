package com.ted.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import org.joda.time.DateTime;

import com.ted.domain.Project;
import com.ted.domain.Project.Job;
import com.ted.domain.Project.Job.Comment;
import com.ted.domain.Project.StatesENUM;
import com.ted.domain.User;
import com.ted.service.ServiceExDBFailure;

public class ProjectDAO {

	private JobsDAO jobsDAO = new JobsDAO();

	public boolean deleteJob(Job job) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;

		try {
			conn.setAutoCommit(false);
			for (Comment com : job.getComments()) {
				deleteComment(com.getId(), conn, statement, set);
			}
			deleteJob(job.getId(), conn, statement, set);
			conn.commit();
		} catch (DBExFailure | SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(); // TODO
				}
			}
			throw new DBExFailure(e);
		}
		return true;
	}

	public boolean deleteJob(Job job, Connection conn,
			PreparedStatement statement, ResultSet set) throws DBExFailure {
		try {
			for (Comment com : job.getComments()) {
				deleteComment(com.getId(), conn, statement, set);
			}
			deleteJob(job.getId(), conn, statement, set);
		} catch (DBExFailure e) {
			throw new DBExFailure(e);
		}
		return true;
	}

	public boolean deleteProject(Project project) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		try {
			conn.setAutoCommit(false);
			for (Job job : project.getJobs()) {
				deleteJob(job, conn, statement, set);
			}
			delete(project.getName(), conn, statement, set);
			conn.commit();
		} catch (DBExFailure | SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(); // TODO
				}
			}
			throw new DBExFailure(e);
		}
		return true;
	}

	public ArrayList<Job> getJobsForProject(String projectName)
			throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM jobs INNER JOIN projhasjobs WHERE id = job AND project = ?";
		ArrayList<Job> allJobs = new ArrayList<>();
		try {
			statement = conn.prepareStatement(query);
			int i = 0;
			statement.setString(++i, projectName);
			System.out.println(statement);
			set = statement.executeQuery();
			while (set.next()) {
				Job job = (getProjectByName(projectName)).new Job();
				job.setId(set.getInt("id"));
				job.setName(set.getString("name"));
				job.setDescription(set.getString("description"));
				job.setStartDate(new DateTime(set.getDate("startdate")));
				job.setEndDate(new DateTime(set.getDate("enddate")));
				job.setState(StatesENUM.values()[set.getInt("state")]);
				job.setComments(getCommentsOfJob(set.getInt("id")));
				job.setJobStaff(((new UserDAO()).findAllForJob(set.getInt("id"))));
				allJobs.add(job);
			}
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return allJobs;
	}

	public Project insert(Project project) throws DBExFailure,
			DBExProjectExists {
		ArrayList<User> staff = project.getStaff();
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		try {
			conn.setAutoCommit(false);

			final String queryProjects = "INSERT INTO projects (name, description, public, manager) VALUES (?,?,?,?)";
			final String queryProjHasStaff = "INSERT INTO projhasstaff (project, user) VALUES (?,?)";
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setString(++i, project.getName());
			statement.setString(++i, project.getDescription());
			System.out.println(project.getName() + project.getDescription());
			statement.setBoolean(++i, project.isPublik());
			statement.setString(++i, project.getManager().getUsername());
			System.out.println("ProjectDAO.insert() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryProjHasStaff);
			for (User user : staff) {
				i = 0;
				statement.setString(++i, project.getName());
				statement.setString(++i, user.getUsername());
				System.out.println("ProjectDAO.insert() staff : " + statement);
				statement.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(); // TODO
				}
			}
			if (e.getErrorCode() == 1062) {
				throw new DBExProjectExists(e);
			} else {
				throw new DBExFailure(e);
			}
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return project;
	}

	public Project update(Project project) throws DBExFailure {
		ArrayList<User> staff = project.getStaff();
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		try {
			conn.setAutoCommit(false);
			final String queryProjects = "UPDATE projects SET description=? , public=? , manager=? WHERE name=?";
			final String queryDeleteProjHasStaff = "DELETE FROM projhasstaff WHERE project=?";
			final String queryProjHasStaff = "INSERT INTO projhasstaff (project, user) VALUES (?,?)";
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setString(++i, project.getDescription());
			statement.setBoolean(++i, project.isPublik());
			statement.setString(++i, project.getManager().getUsername());
			statement.setString(++i, project.getName());
			System.out.println("ProjectDAO.update() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryDeleteProjHasStaff);
			i = 0;
			statement.setString(++i, project.getName());
			System.out.println("ProjectDAO.delete() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryProjHasStaff);
			for (User user : staff) {
				i = 0;
				statement.setString(++i, project.getName());
				statement.setString(++i, user.getUsername());
				System.out.println("ProjectDAO.insert() staff : " + statement);
				statement.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(); // TODO
				}
			}
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return project;
	}

	public ArrayList<String> getAllProjectNames() throws DBExFailure {
		ArrayList<String> pList = null;
		Connection conn = DBConnectionPool.getConnection();
		Statement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM projects";
		if (conn != null) {
			try {
				statement = conn.createStatement();
				set = statement.executeQuery(query);
				pList = new ArrayList<String>();
				while (set.next()) {
					pList.add(set.getString("name"));
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return pList;
	}

	public ArrayList<String> getAllProjectNamesForUser(String username)
			throws DBExFailure {
		ArrayList<String> pList = null;
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "(SELECT name FROM projects WHERE manager=?) UNION (SELECT project as name FROM projhasstaff WHERE user=?)";
		if (conn != null) {
			try {
				statement = conn.prepareStatement(query);
				statement.setString(1, username);
				statement.setString(2, username);
				set = statement.executeQuery();
				pList = new ArrayList<String>();
				while (set.next()) {
					pList.add(set.getString("name"));
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return pList;
	}

	public ArrayList<String> getAllProjectNames(boolean isPublic)
			throws DBExFailure {
		Integer publik = null;
		if (isPublic) {
			publik = 1;
		} else {
			publik = 0;
		}
		ArrayList<String> pList = null;
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT name FROM projects WHERE public=?";
		if (conn != null) {
			try {
				statement = conn.prepareStatement(query);
				statement.setInt(1, publik);
				set = statement.executeQuery();
				pList = new ArrayList<String>();
				while (set.next()) {
					pList.add(set.getString("name"));
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return pList;
	}

	public HashSet<String> getAllManagers() throws DBExFailure {
		HashSet<String> pSet = null;
		Connection conn = DBConnectionPool.getConnection();
		Statement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM projects";
		if (conn != null) {
			try {
				statement = conn.createStatement();
				set = statement.executeQuery(query);
				pSet = new HashSet<String>();
				while (set.next()) {
					pSet.add(set.getString("manager"));
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return pSet;
	}

	public HashSet<String> getAllStaff() throws DBExFailure {
		HashSet<String> pSet = null;
		Connection conn = DBConnectionPool.getConnection();
		Statement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM projhasstaff";
		if (conn != null) {
			try {
				statement = conn.createStatement();
				set = statement.executeQuery(query);
				pSet = new HashSet<String>();
				while (set.next()) {
					pSet.add(set.getString("user"));
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return pSet;
	}

	public Project getProjectByName(String name) throws DBExFailure {
		Project proj = null;
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM projects where name = ?";
		if (conn != null) {
			try {
				statement = conn.prepareStatement(query);
				statement.setString(1, name);
				set = statement.executeQuery();
				if (set.next()) {
					proj = new Project();
					proj.setDescription(set.getString("description"));
					proj.setManager((new UserDAO()).findByUsername(set
							.getString("manager")));
					proj.setName(set.getString("name"));
					proj.setPublik(set.getBoolean("public"));
					ArrayList<User> staff = new ArrayList<User>();
					proj.setStaff(staff);
					proj.setJobs(null);
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return proj;
	}

	public Project getProjectInfoByName(String name) throws DBExFailure {
		Project proj = null;
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query1 = "SELECT * FROM projects where name = ?";
		final String query2 = "SELECT user FROM projhasstaff where project = ?";
		final String query3 = "SELECT * FROM jobs INNER JOIN projhasjobs WHERE id = job AND project = ?";
		if (conn != null) {
			try {
				conn.setAutoCommit(false);
				statement = conn.prepareStatement(query1);
				statement.setString(1, name);
				set = statement.executeQuery();
				if (set.next()) {
					proj = new Project();
					proj.setDescription(set.getString("description"));
					proj.setManager((new UserDAO()).findByUsername(set
							.getString("manager")));
					proj.setName(set.getString("name"));
					proj.setPublik(set.getBoolean("public"));

					ArrayList<User> staff = new ArrayList<User>();
					statement = conn.prepareStatement(query2);
					statement.setString(1, name);
					set = statement.executeQuery();
					while (set.next()) {
						User a = new UserDAO().findByUsername(set
								.getString("user"));
						staff.add(a);
					}
					proj.setStaff(staff);

					ArrayList<Job> jobs = new ArrayList<Job>();
					statement = conn.prepareStatement(query3);
					statement.setString(1, name);
					set = statement.executeQuery();
					while (set.next()) {
						Job a = new Project().new Job();
						a.setComments(getCommentsOfJob(set.getInt("id")));
						a.setDescription(set.getString("description"));
						a.setStartDate(new DateTime((set.getDate("startdate"))));
						a.setEndDate(new DateTime(set.getDate("enddate")));
						a.setId(set.getInt("id"));
						a.setName(set.getString("name"));
						a.setState(StatesENUM.values()[set.getInt("state")]);
						a.setJobStaff(null);
						jobs.add(a);
					}
					proj.setJobs(jobs);
				}
				conn.commit();
			} catch (SQLException e) {
				if (conn != null) {
					try {
						conn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace(); // TODO
					}
				}
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return proj;
	}

	public String getProjectNameOfJob(Integer jobId) throws DBExFailure {
		String proj = null;
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query1 = "SELECT project FROM projhasjobs WHERE job = ?";
		if (conn != null) {
			try {
				statement = conn.prepareStatement(query1);
				statement.setInt(1, jobId);
				set = statement.executeQuery();
				if (set.next()) {
					proj = set.getString("project");
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return proj;
	}

	public boolean delete(String proj) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String queryProjhasstaff = "DELETE from projhasstaff where project = ?";
		final String queryProjects = "DELETE from projects where name = ?";
		try {
			statement = conn.prepareStatement(queryProjhasstaff);
			int i = 1;
			statement.setString(i, proj);
			System.out.println("ProjectDAO.delete1() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryProjects);
			statement.setString(i, proj);
			System.out.println("ProjectDAO.delete2() : " + statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return true;
	}

	public void delete(String proj, Connection conn,
			PreparedStatement statement, ResultSet set) throws DBExFailure {
		try {
			final String queryProjhasstaff = "DELETE from projhasstaff where project = ?";
			final String queryProjects = "DELETE from projects where name = ?";
			statement = conn.prepareStatement(queryProjhasstaff);
			int i = 1;
			statement.setString(i, proj);
			System.out.println("ProjectDAO.delete1() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryProjects);
			statement.setString(i, proj);
			System.out.println("ProjectDAO.delete2() : " + statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		}
	}

	public Job addJob(Project project, Job job) throws DBExFailure,
			DBExJobExists {
		return jobsDAO.insert(project, job);
	}

	public boolean jobWithSameNameAlreadyExistsForProject(Project project,
			String name) throws DBExFailure {
		return jobsDAO.existsJobWithSameNameForProject(project, name);
	}

	public ArrayList<String> getAllJobNamesForUser(String username)
			throws DBExFailure {
		return jobsDAO.getAllJobNamesForUser(username);
	}

	public Comment addComment(Integer job, String com, String writer)
			throws DBExFailure {
		return jobsDAO.insert(job, com, writer);
	}

	public boolean deleteComment(Integer com) throws DBExFailure {
		return jobsDAO.deleteComment(com);
	}

	public void deleteComment(Integer com, Connection conn,
			PreparedStatement statement, ResultSet set) throws DBExFailure {
		jobsDAO.deleteComment(com, conn, statement, set);
	}

	public boolean deleteJob(Integer jobId) throws DBExFailure {
		return jobsDAO.delete(jobId);
	}

	public void deleteJob(Integer com, Connection conn,
			PreparedStatement statement, ResultSet set) throws DBExFailure {
		jobsDAO.delete(com, conn, statement, set);
	}

	public boolean deleteCommentsOfUser(String username) throws DBExFailure {
		return jobsDAO.deleteCommentsOfUser(username);
	}

	public ArrayList<Comment> getCommentsOfUser(String username)
			throws DBExFailure {
		return jobsDAO.getCommentsOfUser(username);
	}

	public ArrayList<Comment> getCommentsOfJob(Integer id) throws DBExFailure {
		return jobsDAO.getCommentsOfJob(id);
	}

	public Comment getCommentWithId(Integer id) throws DBExFailure {
		return jobsDAO.getCommentWithId(id);
	}

	public Job getJobWithId(Integer id) throws DBExFailure {
		return jobsDAO.getJobWithId(id);
	}

	public ArrayList<Job> getJobsForUser(String username) throws DBExFailure {
		return jobsDAO.getJobsForUser(username);
	}

	public ArrayList<Job> getAllJobsForUserInProject(String username,
			String projectName) throws DBExFailure {
		return jobsDAO.getJobsForUserInProject(username, projectName);
	}

	public void updateStateofJob(Integer jobId, Integer state)
			throws DBExFailure {
		jobsDAO.updateStateofJob(jobId, state);
	}

	public Job update(Job job) throws DBExFailure {
		return jobsDAO.update(job);
	}
}

class JobsDAO {
	private CommentsDAO commentDAO = new CommentsDAO();

	public Job update(Job job) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		try {
			conn.setAutoCommit(false);
			final String queryProjects = "UPDATE jobs SET description=? , startdate=? , enddate=? , state=? WHERE id=?";
			final String queryDeleteJobHasStaff = "DELETE FROM jobhasstaff WHERE jobs=?";
			final String queryJobHasStaff = "INSERT INTO jobhasstaff (jobs, user) VALUES (?,?)";
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setString(++i, job.getDescription());
			statement.setDate(
					++i,
					java.sql.Date.valueOf(job.getStartDate().getYear() + "-"
							+ job.getStartDate().getMonthOfYear() + "-"
							+ job.getStartDate().getDayOfMonth()));
			statement.setDate(
					++i,
					java.sql.Date.valueOf(job.getEndDate().getYear() + "-"
							+ job.getEndDate().getMonthOfYear() + "-"
							+ job.getEndDate().getDayOfMonth()));
			statement.setInt(++i, job.getState().ordinal());
			statement.setInt(++i, job.getId());
			System.out.println("ProjectDAO.update() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryDeleteJobHasStaff);
			i = 0;
			statement.setInt(++i, job.getId());
			System.out.println("ProjectDAO.delete() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryJobHasStaff);
			ArrayList<User> jobUsers = job.getJobStaff();
			for (User user : jobUsers) {
				i = 0;
				statement.setInt(++i, job.getId());
				statement.setString(++i, user.getUsername());
				System.out.println("ProjectDAO.insert() staff : " + statement);
				statement.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(); // TODO
				}
			}
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return job;
	}

	public void updateStateofJob(Integer jobId, Integer state)
			throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		try {
			final String queryProjects = "UPDATE jobs SET state=? WHERE id=?";
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setInt(++i, state);
			statement.setInt(++i, jobId);
			System.out.println("ProjectDAO.update() : " + statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
	}

	public ArrayList<Job> getJobsForUser(String username) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM jobs INNER JOIN jobhasstaff WHERE id = jobs AND user = ?";
		ArrayList<Job> allJobs = new ArrayList<>();
		try {
			statement = conn.prepareStatement(query);
			int i = 0;
			statement.setString(++i, username);
			System.out.println(statement);
			set = statement.executeQuery();
			while (set.next()) {
				Job job = new Project().new Job();
				job.setId(set.getInt("id"));
				job.setName(set.getString("name"));
				job.setDescription(set.getString("description"));
				job.setStartDate(new DateTime(set.getDate("startdate")));
				job.setEndDate(new DateTime(set.getDate("enddate")));
				job.setState(StatesENUM.values()[set.getInt("state")]);
				job.setComments(getCommentsOfJob(set.getInt("id")));
				job.setJobStaff(((new UserDAO()).findAllForJob(set.getInt("id"))));
				allJobs.add(job);
			}
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return allJobs;
	}

	public ArrayList<Job> getJobsForUserInProject(String username,
			String projectName) throws DBExFailure {

		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM jobs INNER JOIN projhasjobs INNER JOIN jobhasstaff "
				+ "WHERE id = job AND jobs = id AND user = ? AND project = ?";
		ArrayList<Job> allJobs = new ArrayList<>();
		try {
			statement = conn.prepareStatement(query);
			int i = 0;
			statement.setString(++i, username);
			statement.setString(++i, projectName);
			System.out.println(statement);
			set = statement.executeQuery();
			while (set.next()) {
				Job job = new Project().new Job();
				job.setId(set.getInt("id"));
				job.setName(set.getString("name"));
				job.setDescription(set.getString("description"));
				job.setStartDate(new DateTime(set.getDate("startdate")));
				job.setEndDate(new DateTime(set.getDate("enddate")));
				job.setState(StatesENUM.values()[set.getInt("state")]);
				job.setComments(getCommentsOfJob(set.getInt("id")));
				job.setJobStaff(((new UserDAO()).findAllForJob(set.getInt("id"))));
				allJobs.add(job);
			}
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return allJobs;
	}

	public ArrayList<String> getAllJobNamesForUser(String username)
			throws DBExFailure {
		ArrayList<String> jList = null;
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT name FROM jobs INNER JOIN jobhasstaff WHERE id = jobs AND user = ?";
		if (conn != null) {
			try {
				statement = conn.prepareStatement(query);
				statement.setString(1, username);
				set = statement.executeQuery();
				jList = new ArrayList<String>();
				while (set.next()) {
					jList.add(set.getString("name"));
				}
			} catch (SQLException e) {
				throw new DBExFailure(e);
			} finally {
				DBConnectionPool.closeResources(set, statement, conn);
			}
		}
		return jList;
	}

	public boolean existsJobWithSameNameForProject(Project p, String jobname)
			throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String query = "SELECT * FROM jobs INNER JOIN projhasjobs WHERE id = job AND project = ? AND name = ?";
		try {
			statement = conn.prepareStatement(query);
			int i = 0;
			statement.setString(++i, p.getName());
			statement.setString(++i, jobname);
			set = statement.executeQuery();
			if (set.next())
				return true;
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return false;
	}

	public Job insert(Project project, Job job) throws DBExFailure,
			DBExJobExists {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String queryJobs = "INSERT INTO jobs (name, description, startdate, enddate,state) VALUES (?,?,?,?,?)";
		final String queryProjectHasJobs = "INSERT INTO projhasjobs (project, job) VALUES (?,?)";
		final String queryJobHasStaff = "INSERT INTO jobhasstaff (user, jobs) VALUES (?,?)";
		try {
			conn.setAutoCommit(false);
			statement = conn.prepareStatement(queryJobs,
					Statement.RETURN_GENERATED_KEYS);
			int i = 0;
			statement.setString(++i, job.getName());
			statement.setString(++i, job.getDescription());
			statement.setDate(
					++i,
					java.sql.Date.valueOf(job.getStartDate().getYear() + "-"
							+ job.getStartDate().getMonthOfYear() + "-"
							+ job.getStartDate().getDayOfMonth()));
			statement.setDate(
					++i,
					java.sql.Date.valueOf(job.getEndDate().getYear() + "-"
							+ job.getEndDate().getMonthOfYear() + "-"
							+ job.getEndDate().getDayOfMonth()));
			statement.setInt(++i, StatesENUM.NEW.ordinal());
			statement.executeUpdate();
			set = statement.getGeneratedKeys();
			set.next();
			int id = set.getInt(1);
			job.setId(id);

			statement = conn.prepareStatement(queryProjectHasJobs);
			i = 0;
			statement.setString(++i, project.getName());
			statement.setInt(++i, id);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryJobHasStaff);
			ArrayList<User> jobUsers = job.getJobStaff();
			for (User user : jobUsers) {
				i = 0;
				statement.setString(++i, user.getUsername());
				statement.setInt(++i, id);
				statement.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(); // TODO
				}
			}
			if (e.getErrorCode() == 1062) {
				throw new DBExJobExists(e);
			} else {
				throw new DBExFailure(e);
			}
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return job;
	}

	public boolean delete(Integer job) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		boolean result = true;
		final String queryJobhasstaff = "DELETE from jobhasstaff where jobs = ?";
		final String queryProjhasjobs = "DELETE from projhasjobs where job = ?";
		final String queryJobs = "DELETE from jobs where id = ?";
		try {
			statement = conn.prepareStatement(queryJobhasstaff);
			int i = 1;
			statement.setInt(i, job);
			System.out.println("JobDAO.delete1() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryProjhasjobs);
			statement.setInt(i, job);
			System.out.println("JobDAO.delete2() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryJobs);
			statement.setInt(i, job);
			System.out.println("JobDAO.delete3() : " + statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return result;
	}

	public void delete(Integer job, Connection conn,
			PreparedStatement statement, ResultSet set) throws DBExFailure {
		try {
			final String queryJobhasstaff = "DELETE from jobhasstaff where jobs = ?";
			final String queryProjhasjobs = "DELETE from projhasjobs where job = ?";
			final String queryJobs = "DELETE from jobs where id = ?";
			statement = conn.prepareStatement(queryJobhasstaff);
			int i = 1;
			statement.setInt(i, job);
			System.out.println("JobDAO.delete1() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryProjhasjobs);
			statement.setInt(i, job);
			System.out.println("JobDAO.delete2() : " + statement);
			statement.executeUpdate();

			statement = conn.prepareStatement(queryJobs);
			statement.setInt(i, job);
			System.out.println("JobDAO.delete3() : " + statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		}
	}

	public Job getJobWithId(Integer id) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		Job job = null;
		final String query = "SELECT * FROM jobs WHERE id = ?";
		try {
			statement = conn.prepareStatement(query);
			int i = 0;
			statement.setInt(++i, id);
			System.out.println(statement);
			set = statement.executeQuery();
			if (set.next()) {
				job = new Project().new Job();
				job.setId(set.getInt("id"));
				job.setName(set.getString("name"));
				job.setDescription(set.getString("description"));
				job.setStartDate(new DateTime(set.getDate("startdate")));
				job.setEndDate(new DateTime(set.getDate("enddate")));
				job.setState(StatesENUM.values()[set.getInt("state")]);
				job.setComments(getCommentsOfJob(set.getInt("id")));
				job.setJobStaff(((new UserDAO()).findAllForJob(set.getInt("id"))));
			}
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return job;
	}

	public Comment insert(Integer job, String com, String writer)
			throws DBExFailure {
		return commentDAO.insert(com, job, writer);
	}

	public boolean deleteComment(Integer com) throws DBExFailure {
		return commentDAO.delete(com);
	}

	public void deleteComment(Integer com, Connection conn,
			PreparedStatement statement, ResultSet set) throws DBExFailure {
		commentDAO.delete(com, conn, statement, set);
	}

	public boolean deleteCommentsOfUser(String username) throws DBExFailure {
		return commentDAO.deleteCommentsOfUser(username);
	}

	public ArrayList<Comment> getCommentsOfUser(String username)
			throws DBExFailure {
		return commentDAO.getCommentsOfUser(username);
	}

	public ArrayList<Comment> getCommentsOfJob(Integer id) throws DBExFailure {
		return commentDAO.getCommentsOfJob(id);
	}

	public Comment getCommentWithId(Integer id) throws DBExFailure {
		return commentDAO.getCommentWithId(id);
	}
}

class CommentsDAO {
	private UserDAO userDAO = new UserDAO();

	public Comment insert(String comment, int idJob, String writerUsername)
			throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String queryJobs = "INSERT INTO comments (comment, job, writer) VALUES (?,?,?)";
		Comment com = null;
		try {
			statement = conn.prepareStatement(queryJobs,
					Statement.RETURN_GENERATED_KEYS);
			int i = 0;
			statement.setString(++i, comment);
			statement.setInt(++i, idJob);
			statement.setString(++i, writerUsername);
			System.out.println("CommentDAO.insert() : " + statement);
			statement.executeUpdate();
			set = statement.getGeneratedKeys();
			set.next();
			int id = set.getInt(1);
			com = new Project().new Job().new Comment();
			com.setCommenter(userDAO.findByUsername(writerUsername));
			com.setId(id);
			com.setComment(comment);
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return com;
	}

	public boolean delete(Integer com) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		boolean result = true;
		final String queryProjects = "DELETE from comments where id = ?";
		try {
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setInt(++i, com);
			System.out.println("CommentDAO.delete() : " + statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return result;
	}

	public void delete(Integer com, Connection conn,
			PreparedStatement statement, ResultSet set) throws DBExFailure {
		try {
			final String queryProjects = "DELETE from comments where id = ?";
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setInt(++i, com);
			System.out.println("CommentDAO.delete() : " + statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		}
	}

	public boolean deleteCommentsOfUser(String username) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		final String queryProjects = "DELETE from comments where writer = ?";
		try {
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setString(++i, username);
			System.out.println("CommentDAO.delete() : " + statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return true;
	}

	public ArrayList<Comment> getCommentsOfUser(String username)
			throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		ArrayList<Comment> allComments = new ArrayList<Comment>();
		final String queryProjects = "SELECT * from comments where writer = ?";
		try {
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setString(++i, username);
			System.out.println("CommentDAO.select() : " + statement);
			set = statement.executeQuery();
			while (set.next()) {
				Comment a = new Project().new Job().new Comment();
				a.setComment(set.getString("comment"));
				a.setCommenter(set.getString("writer"));
				a.setId(set.getInt("id"));
				allComments.add(a);
			}
		} catch (SQLException | ServiceExDBFailure e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return allComments;
	}

	public ArrayList<Comment> getCommentsOfJob(Integer id) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		ArrayList<Comment> allComments = new ArrayList<Comment>();
		final String queryProjects = "SELECT * from comments where job = ?";
		try {
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setInt(++i, id);
			System.out.println("CommentDAO.select() : " + statement);
			set = statement.executeQuery();
			while (set.next()) {
				Comment a = new Project().new Job().new Comment();
				a.setComment(set.getString("comment"));
				a.setCommenter(set.getString("writer"));
				a.setId(set.getInt("id"));
				allComments.add(a);
			}
		} catch (SQLException | ServiceExDBFailure e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return allComments;
	}

	public Comment getCommentWithId(Integer id) throws DBExFailure {
		Connection conn = DBConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet set = null;
		Comment comment = null;
		final String queryProjects = "SELECT * from comments where id = ?";
		try {
			statement = conn.prepareStatement(queryProjects);
			int i = 0;
			statement.setInt(++i, id);
			System.out.println("CommentDAO.select() : " + statement);
			set = statement.executeQuery();
			if (set.next()) {
				comment = new Project().new Job().new Comment();
				comment.setComment(set.getString("comment"));
				comment.setCommenter(set.getString("writer"));
				comment.setId(set.getInt("id"));
			}
		} catch (SQLException | ServiceExDBFailure e) {
			throw new DBExFailure(e);
		} finally {
			DBConnectionPool.closeResources(set, statement, conn);
		}
		return comment;
	}

}
