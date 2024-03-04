package sharedrmi.application.dto;

import lombok.Builder;
import lombok.Getter;
import sharedrmi.domain.enums.MediumType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
public class SongDTO implements Serializable {


    private long longId;
    private String title;
    private BigDecimal price;
    private int stock;
    private MediumType mediumType;
    private String releaseDate;
    private String genre;
    private List<ArtistDTO> artists;
    private Set<AlbumDTO> inAlbum;

    public SongDTO() {
    }

    @Builder
    public SongDTO(String title, BigDecimal price, int stock, MediumType mediumType, String releaseDate, String genre, List<ArtistDTO> artists, Set<AlbumDTO> inAlbum, long longId) {
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.mediumType = mediumType;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.artists = artists;
        this.inAlbum = inAlbum;
        this.longId = longId;
    }

}