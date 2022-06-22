package org.erick.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Passenger {
	@Id 
	@GeneratedValue
	private Long id;
	private String name;
	private String cpf;
	private String phoneNumber;
	private String userName;
	private String password;
	private String email;
}