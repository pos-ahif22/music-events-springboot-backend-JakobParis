package at.kaindorf.htl.event.repository;

import at.kaindorf.htl.event.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

}
