package org.erick.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DriverAvailability {
    private Long idDriver;
    private String currentAddress;
    private String district;
}