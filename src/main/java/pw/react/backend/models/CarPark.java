package pw.react.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car_parks")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarPark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "street_id", nullable = false)
    private Street street;

    @Column(name = "building_number", nullable = false)
    private String buildingNumber;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "daily_cost", nullable = false)
    private double dailyCost;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
