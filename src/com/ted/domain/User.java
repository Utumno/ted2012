package com.ted.domain;

public class User {

	public static enum RolesENUM {
		ADMIN, GUEST, MANAGER, STAFF
	}

	private RolesENUM role = RolesENUM.GUEST;
	private String username;
	private String password;
	private String name;
	private String surname;
	private String email;

	public RolesENUM getRole() {
		return role;
	}

	public void setRole(RolesENUM role) {
		this.role = role;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
