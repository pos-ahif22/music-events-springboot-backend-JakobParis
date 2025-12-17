package at.kaindorf.htl.event.dto;

public record ArtistDTO(
        Long artistId,
        String firstname,
        String lastname,
        String description,
        String imageUrl
) {}