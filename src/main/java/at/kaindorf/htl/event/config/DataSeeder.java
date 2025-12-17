package at.kaindorf.htl.event.config;

import at.kaindorf.htl.event.dto.ArtistDTO;
import at.kaindorf.htl.event.dto.EventDTO;
import at.kaindorf.htl.event.dto.RatingDTO;
import at.kaindorf.htl.event.entity.Artist;
import at.kaindorf.htl.event.entity.Event;
import at.kaindorf.htl.event.entity.Rating;
import at.kaindorf.htl.event.repository.ArtistRepository;
import at.kaindorf.htl.event.repository.EventRepository;
import at.kaindorf.htl.event.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final RatingRepository ratingRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // prevent duplicate seeding on every restart
        if (artistRepository.count() > 0 || eventRepository.count() > 0) {
            return;
        }

        ClassPathResource resource = new ClassPathResource("data/music-data-2024.json");
        if (!resource.exists()) {
            // Put the file into: src/main/resources/music-data-2024.json
            return;
        }

        JsonDataWrapper data;
        try (InputStream is = resource.getInputStream()) {
            data = objectMapper.readValue(is, JsonDataWrapper.class);
        }

        // 1) persist artists and remember mapping jsonId -> db entity
        Map<Long, Artist> artistByJsonId = new HashMap<>();
        for (ArtistDTO dto : data.artists()) {
            Artist a = new Artist(dto.firstname(), dto.lastname(), dto.description(), dto.imageUrl());
            a = artistRepository.save(a);
            artistByJsonId.put(dto.artistId(), a);
        }

        // 2) persist events + many-to-many links
        for (EventDTO dto : data.events()) {
            Event e = new Event();
            e.setTitle(dto.title());
            e.setDescription(dto.description());
            e.setLocation(dto.location());
            e.setBegin(dto.begin());
            e.setEnd(dto.end());
            e.setImageUrl(dto.imageUrl());

            List<Artist> artists = dto.artistIds().stream()
                    .map(artistByJsonId::get)
                    .filter(java.util.Objects::nonNull)
                    .toList();

            e.getArtists().addAll(artists);
            // keep both sides in sync (optional but nice)
            Event finalE = e;
            artists.forEach(a -> a.getEvents().add(finalE));

            e = eventRepository.save(e);

            // 3) persist ratings (no cascade in entity right now)
            if (dto.ratings() != null) {
                for (RatingDTO rDto : dto.ratings()) {
                    Rating r = new Rating(rDto.stars(), rDto.comment());
                    r.setEvent(e);
                    ratingRepository.save(r);
                    e.getRatings().add(r);
                }
            }
        }
    }

    /** Matches the top-level JSON structure: { "artists": [...], "events": [...] } */
    public record JsonDataWrapper(List<ArtistDTO> artists, List<EventDTO> events) {}
}