package at.kaindorf.htl.event.dto;

public record RatingDTO(
        Long ratingId,
        Integer stars,
        String comment
) {}
