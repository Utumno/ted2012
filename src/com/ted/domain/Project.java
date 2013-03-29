package com.ted.domain;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.ted.service.ServiceExDBFailure;
import com.ted.service.UserService;

public class Project {
	public static enum StatesENUM {
		NEW, STARTED, DONE
	}

	private String name;
	private String description;
	private boolean publik;
	private User manager;
	private ArrayList<User> staff;
	private ArrayList<Job> jobs;

	public Project() {
		this.staff = new ArrayList<User>();
		this.jobs = new ArrayList<Job>();
	}

	public class Job {
		private String name;
		private String description;
		private DateTime startDate;
		private DateTime endDate;
		private ArrayList<User> jobStaff;
		private ArrayList<Comment> comments;
		private int id;
		private StatesENUM state;

		public Job() {
			this.jobStaff = new ArrayList<User>();
			this.comments = new ArrayList<Comment>();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public DateTime getStartDate() {
			return startDate;
		}

		public void setStartDate(DateTime startDate) {
			this.startDate = startDate;
		}

		public DateTime getEndDate() {
			return endDate;
		}

		public void setEndDate(DateTime endDate) {
			this.endDate = endDate;
		}

		public ArrayList<User> getJobStaff() {
			return jobStaff;
		}

		public void setJobStaff(ArrayList<User> jobStaff) {
			this.jobStaff = jobStaff;
		}

		public ArrayList<Comment> getComments() {
			return comments;
		}

		public void setComments(ArrayList<Comment> comments) {
			this.comments = comments;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public StatesENUM getState() {
			return state;
		}

		public void setState(StatesENUM state) {
			this.state = state;
		}

		public class Comment {
			Integer id;
			private String comment;
			private User commenter;

			public Integer getId() {
				return id;
			}

			public void setId(Integer id) {
				this.id = id;
			}

			public String getComment() {
				return comment;
			}

			public void setComment(String comment) {
				this.comment = comment;
			}

			public User getCommenter() {
				return commenter;
			}

			public void setCommenter(User commenter) {
				this.commenter = commenter;
			}

			public void setCommenter(String commenter)
					throws ServiceExDBFailure {
				this.commenter = new UserService()
						.getUserWithUsername(commenter);
			}

		}
	}

	public Job addJobToProject(Job job) {
		// https://www.securecoding.cert.org/confluence/pages/viewpage.action?pageId=43647087
		synchronized (Project.class) {
			jobs.add(job);
		}
		return job;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublik() {
		return publik;
	}

	public void setPublik(boolean publik) {
		this.publik = publik;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public ArrayList<User> getStaff() {
		return staff;
	}

	public void setStaff(ArrayList<User> staff) {
		this.staff = staff;
	}

	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}
}
