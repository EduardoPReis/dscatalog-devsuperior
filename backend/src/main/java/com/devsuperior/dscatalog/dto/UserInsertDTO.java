package com.devsuperior.dscatalog.dto;

public class UserInsertDTO extends UserDTO {
	private static final long serialVersionUID = 1L;

	private String password;

	UserInsertDTO() {
	}

	public UserInsertDTO(String password) {
		super();
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPasswords(String password) {
		this.password = password;
	}

}
