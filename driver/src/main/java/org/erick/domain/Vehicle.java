package org.erick.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Vehicle extends PanacheEntity{
	private String alias;
	private String licensePlate;
	private String vehicleManufacturer;
	private String modelName;
	private String color;
	@ManyToOne
	@JoinColumn(name = "idUser")
	private User user;
}
