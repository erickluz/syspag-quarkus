package org.erick.domain;

import lombok.Data;

@Data
public class DriverAvailability {
    private Long idDriver;
    private String currentAddress;
    private String district;
}