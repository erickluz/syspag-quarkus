package org.erick.domain;

import java.time.LocalDateTime;

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
public class PassengerHistory extends PanacheEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDriverHistory;
	@ManyToOne
	@JoinColumn(name = "idPassenger")
	private PassengerUser passenger;
	private LocalDateTime datetimeStatus;
	private String observation;
}