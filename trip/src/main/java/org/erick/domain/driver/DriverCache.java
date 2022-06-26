package org.erick.domain.driver;

import java.io.Serializable;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DriverCache implements Serializable {
	@ProtoField(number = 1)
	Long id;
	@ProtoField(number = 2)
	String address;
	@ProtoField(number = 3)
	String district;
	@ProtoField(number = 4)
	Long ordering;
	
	@ProtoFactory
	public DriverCache(Long id, String address, String district, Long ordering) {
		this.id = id;
		this.address = address;
		this.district = district;
		this.ordering = ordering;
	}
}