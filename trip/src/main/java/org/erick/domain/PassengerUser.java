package org.erick.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class PassengerUser extends PanacheEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPassenger;
	private String name;
}