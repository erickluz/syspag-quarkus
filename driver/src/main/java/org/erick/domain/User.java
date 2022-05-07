package org.erick.domain;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class User extends PanacheEntity {
	private String nome;
	private String cpf;
	private String phoneNumber;
	private String userName;
	private String password;
	private String email;
}
