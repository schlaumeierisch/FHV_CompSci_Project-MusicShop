package fhv.musicshop.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Album extends PanacheEntityBase {

    private long longId;
    private String title;
    private String imageUrl;
    private BigDecimal price;
    private int stock;
    @Enumerated(EnumType.STRING)
    private MediumType mediumType;
    private String releaseDate;
    @Id
    private AlbumId albumId;
    private String label;
    private int quantityToAddToCart;



    public Album() {
    }

    public Album(String title, String releaseDate, AlbumId albumId, String label) {
        this.label = label;
        this.albumId = albumId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.mediumType = MediumType.DIGITAL;
        this.stock = -1;
        this.quantityToAddToCart = 0;
    }

    public String getLabel() {
        return label;
    }

    public AlbumId getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public MediumType getMediumType() {
        return mediumType;
    }

    public int getQuantityToAddToCart() {
        return quantityToAddToCart;
    }

    public long getLongId() {
        return longId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return longId == album.longId && stock == album.stock && quantityToAddToCart == album.quantityToAddToCart && title.equals(album.title) && imageUrl.equals(album.imageUrl) && price.equals(album.price) && mediumType == album.mediumType && releaseDate.equals(album.releaseDate) && albumId.equals(album.albumId) && label.equals(album.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longId, title, imageUrl, price, stock, mediumType, releaseDate, albumId, label, quantityToAddToCart);
    }
}
