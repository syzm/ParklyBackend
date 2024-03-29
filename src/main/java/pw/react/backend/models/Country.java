package pw.react.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ISO3166_name", nullable = false)
    private String iso3166Name;
}
