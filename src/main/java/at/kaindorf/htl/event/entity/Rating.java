package at.kaindorf.htl.event.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    private Integer stars;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Rating(Integer stars, String comment) {
        this.stars = stars;
        this.comment = comment;
    }
}
