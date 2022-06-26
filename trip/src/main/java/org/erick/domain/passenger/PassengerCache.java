package org.erick.domain.passenger;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PassengerCache {
	@ProtoField(number = 1)
	Long id;
	@ProtoField(number = 2)
	String addressOrigin;
	@ProtoField(number = 3)
	String addressDestiny;
	@ProtoField(number = 4)
	String district;
	
	@ProtoFactory
	public PassengerCache(Long id, String addressOrigin, String addressDestiny, String district) {
		this.id = id;
		this.addressOrigin = addressOrigin;
		this.addressDestiny = addressDestiny;
		this.district = district;
	}
	
}
