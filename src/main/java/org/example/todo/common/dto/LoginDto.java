package org.example.todo.common.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class LoginDto {
	private String username;

	@ToString.Exclude
	private String plainTextPassword;
}
