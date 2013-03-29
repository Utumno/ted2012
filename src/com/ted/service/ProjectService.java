package com.ted.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ted.dao.DBExFailure;
import com.ted.dao.DBExJobExists;
import com.ted.dao.DBExProjectExists;
import com.ted.dao.ProjectDAO;
import com.ted.domain.Project;
import com.ted.domain.Project.Job;
import com.ted.domain.Project.Job.Comment;

public class ProjectService {
	private ProjectDAO projectDAO = new ProjectDAO();

	public Project createProject(Project project) throws ServiceExDBFailure,
			ServiceExProjectExists {
		try {
			project = projectDAO.insert(project);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		} catch (DBExProjectExists e) {
			throw new ServiceExProjectExists(e);
		}
		return project;
	}

	public Comment addComment(Integer job, String com, String writer)
			throws ServiceExDBFailure {
		try {
			return projectDAO.addComment(job, com, writer);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public ArrayList<Comment> getCommentsOfUser(String username)
			throws ServiceExDBFailure {
		try {
			return projectDAO.getCommentsOfUser(username);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public ArrayList<Comment> getCommentsOfJob(Integer id)
			throws ServiceExDBFailure {
		try {
			return projectDAO.getCommentsOfJob(id);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public Comment getCommentWithId(Integer id) throws ServiceExDBFailure {
		try {
			return projectDAO.getCommentWithId(id);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public ArrayList<Job> getJobsForProject(String project)
			throws ServiceExDBFailure {
		try {
			return projectDAO.getJobsForProject(project);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public ArrayList<Job> getJobsForUser(String username)
			throws ServiceExDBFailure {
		try {
			return projectDAO.getJobsForUser(username);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public Job getJobWithId(Integer id) throws ServiceExDBFailure {
		try {
			return projectDAO.getJobWithId(id);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public Project updateProject(Project project) throws ServiceExDBFailure,
			ServiceExProjectExists {
		try {
			project = projectDAO.update(project);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		return project;
	}

	public Job updateJob(Job job) throws ServiceExDBFailure {
		try {
			job = projectDAO.update(job);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
		return job;
	}

	public void updateStateofJob(Integer jobId, Integer state)
			throws ServiceExDBFailure {
		try {
			projectDAO.updateStateofJob(jobId, state);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public boolean isProjectNameDuplicate(String projectName)
			throws ServiceExDBFailure {
		try {
			Project project = projectDAO.getProjectByName(projectName);
			if (project == null) {
				return false;
			}
			return true;
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}

	}

	public Project getProjectInfoByName(String projectName)
			throws ServiceExDBFailure {
		try {
			return projectDAO.getProjectInfoByName(projectName);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public String getProjectNameOfJob(Integer jobId) throws ServiceExDBFailure {
		try {
			return projectDAO.getProjectNameOfJob(jobId);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	// lol
	public boolean jobExistsWithThisNameForThisProject(Project project,
			String name) throws ServiceExDBFailure {
		try {
			return projectDAO.jobWithSameNameAlreadyExistsForProject(project,
					name);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public Project addJob(Project project, Job job) throws ServiceExDBFailure,
			ServiceExJobExists {
		// First add the job to the DB - if this fails do not add to project
		try {
			projectDAO.addJob(project, job);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		} catch (DBExJobExists e) {
			throw new ServiceExJobExists(e);
		}
		return project;
	}

	public ArrayList<String> getAllProjectNamesForUser(String username)
			throws ServiceExDBFailure {
		try {
			if (username == null) {
				return projectDAO.getAllProjectNames();
			} else {
				return projectDAO.getAllProjectNamesForUser(username);
			}
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public ArrayList<String> getAllProjectNames(boolean isPublic)
			throws ServiceExDBFailure {
		try {
			return projectDAO.getAllProjectNames(isPublic);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public ArrayList<String> getAllJobNamesForUser(String username)
			throws ServiceExDBFailure {
		try {
			return projectDAO.getAllJobNamesForUser(username);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public ArrayList<Job> getAllJobsForUserInProject(String username,
			String projectName) throws ServiceExDBFailure {
		try {
			return projectDAO.getAllJobsForUserInProject(username, projectName);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public boolean deleteComment(Integer com) throws ServiceExDBFailure {
		try {
			return projectDAO.deleteComment(com);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public void deleteComment(Integer com, Connection conn,
			PreparedStatement statement, ResultSet set) throws DBExFailure {
		projectDAO.deleteComment(com, conn, statement, set);
	}

	public boolean deleteCommentsOfUser(String username)
			throws ServiceExDBFailure {
		try {
			return projectDAO.deleteCommentsOfUser(username);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public boolean deleteJob(Job job) throws ServiceExDBFailure {
		try {
			return projectDAO.deleteJob(job);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}

	public boolean deleteProject(Project project) throws ServiceExDBFailure {
		try {
			return projectDAO.deleteProject(project);
		} catch (DBExFailure e) {
			throw new ServiceExDBFailure(e);
		}
	}
}
