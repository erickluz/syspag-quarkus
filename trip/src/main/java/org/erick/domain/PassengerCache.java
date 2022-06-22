package org.erick.domain;

import org.infinispan.protostream.annotations.ProtoField;

import lombok.Data;

@Data
public class PassengerCache {
	@ProtoField(1)
	private String addressOrigin;
	@ProtoField(2)
	private String addressDestiny;
	@ProtoField(3)
	private String district;
	
	public PassengerCache(String addressOrigin, String addressDestiny, String district) {
		this.addressOrigin = addressOrigin;
		this.addressDestiny = addressDestiny;
		this.district = district;
	}
	
}
