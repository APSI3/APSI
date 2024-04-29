package apsi.team3.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "capacity", nullable = true)
    private int capacity;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = true)
    private String street;

    @Column(name = "building_nr", nullable = true)
    private String building_nr;

    @Column(name = "apartment_nr", nullable = true)
    private String apartment_nr;

    @Column(name = "zip_code", nullable = true)
    private String zip_code;
}