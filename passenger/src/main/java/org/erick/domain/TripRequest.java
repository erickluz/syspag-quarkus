package org.erick.domain;

import lombok.Data;

@Data
public class TripRequest {
    private Long idPassenger;
    private String addressOrigin;
    private String addressDestiny;
    private String district;
}