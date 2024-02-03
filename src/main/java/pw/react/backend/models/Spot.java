package pw.react.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spots")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_park_id", nullable = false)
    private CarPark carPark;

    @Column(name = "name", nullable = true)
    private String name;
}
