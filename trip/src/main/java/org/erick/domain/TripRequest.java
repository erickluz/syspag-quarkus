package org.erick.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TripRequest {
    private Long idPassenger;
    private String addressOrigin;
    private String addressDestiny;
    private String district;
}
