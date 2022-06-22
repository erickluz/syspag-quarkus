package org.erick.domain;

import org.infinispan.protostream.annotations.ProtoField;

import lombok.Data;

@Data
public class DriverCache {
	@ProtoField(1)
	private String address;
	@ProtoField(2)
	private String district;
	@ProtoField(3)
	private Long ordering;
	
	public DriverCache(String address, String district, Long ordering) {
		this.address = address;
		this.district = district;
		this.ordering = ordering;
	}
}