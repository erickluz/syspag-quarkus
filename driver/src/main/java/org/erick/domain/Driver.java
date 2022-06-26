package org.erick.domain;

import java.io.Serializable;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Driver extends PanacheEntity implements Serializable {
	private String name;
	private String cpf;
	private String phoneNumber;
	private String userName;
	private String password;
	private String email;
}
