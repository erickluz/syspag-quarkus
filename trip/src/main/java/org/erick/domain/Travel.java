package org.erick.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Travel extends PanacheEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTravel;
	@ManyToOne
	@JoinColumn(name = "idPassenger")
	private PassengerUser passenger;
	@ManyToOne
	@JoinColumn(name = "idDriver")
	private DriverUser driver;
}