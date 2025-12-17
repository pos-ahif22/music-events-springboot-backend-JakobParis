package at.kaindorf.htl.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record EventDTO(
        Long eventId,
        String title,
        String description,
        String location,
        LocalDateTime begin,
        LocalDateTime end,
        String imageUrl,
        List<Long> artistIds,
        List<RatingDTO> ratings
) {

    @JsonCreator
    public EventDTO(
            @JsonProperty("eventId") Long eventId,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("location") String location,
            @JsonProperty("begin") LocalDateTime begin,
            @JsonProperty("end") LocalDateTime end,
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("artistIds") String artistIds,
            @JsonProperty("ratings") List<RatingDTO> ratings
    ) {
        this(
                eventId,
                title,
                description,
                location,
                begin,
                end,
                imageUrl,
                Arrays.stream(artistIds.split(","))
                        .map(String::trim)
                        .map(Long::valueOf)
                        .toList(),
                ratings
        );
    }
}
