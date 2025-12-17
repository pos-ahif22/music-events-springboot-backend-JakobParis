package at.kaindorf.htl.event.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
@Getter
@Setter
@NoArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;

    private String firstname;
    private String lastname;
    private String description;
    private String imageUrl;

    @ManyToMany(mappedBy = "artists")
    private List<Event> events = new ArrayList<>();

    public Artist(String firstname, String lastname, String description, String imageUrl) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}