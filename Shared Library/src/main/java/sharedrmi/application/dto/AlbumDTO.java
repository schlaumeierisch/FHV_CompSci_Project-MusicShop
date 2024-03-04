package sharedrmi.application.dto;

import lombok.Builder;
import lombok.Getter;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.AlbumId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
public class AlbumDTO implements Serializable {

    private long longId;
    private String title;
    private String imageUrl = " ";
    private BigDecimal price;
    private int stock;
    private MediumType mediumType;
    private String releaseDate;
    private AlbumId albumId;
    private String label;
    private Set<SongDTO> songs;
    private int quantityToAddToCart;

    public AlbumDTO() {}

    @Builder
    public AlbumDTO(String title, String imageUrl, BigDecimal price, int stock, MediumType mediumType, String releaseDate, AlbumId albumId, String label, Set<SongDTO> songs, int quantityToAddToCart, long longId) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stock = stock;
        this.mediumType = mediumType;
        this.releaseDate = releaseDate;
        this.albumId = albumId;
        this.label = label;
        this.songs = songs;
        this.quantityToAddToCart = quantityToAddToCart;
        this.longId = longId;
    }

}
