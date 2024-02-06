package pw.react.backend.dto.Reservation;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPatchDto {
    @DecimalMin("0.0")
    private double cost;

    private String status;
}
