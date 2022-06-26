package org.erick.domain.travel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Travel extends PanacheEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long idPassenger;
	private Long idDriver;
}