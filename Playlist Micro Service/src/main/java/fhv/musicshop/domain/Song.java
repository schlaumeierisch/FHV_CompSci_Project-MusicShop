package fhv.musicshop.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;


@Entity
public class Song extends PanacheEntityBase {

    @Id
    private long longId;
    private String genre;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Artist> artists;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Album> inAlbum;
    private String title;
    private String releaseDate;

    private BigDecimal price;
    private int stock;
    @Enumerated(EnumType.STRING)
    private MediumType mediumType;

    public Song() {
    }

    public Song(String title, String releaseDate, String genre, List<Artist> artists, Set<Album> inAlbum) {
        this.genre = genre;
        this.artists = artists;
        this.title = title;
        this.releaseDate = releaseDate;
        this.inAlbum = inAlbum;
        this.mediumType = MediumType.DIGITAL;
        this.stock = -1;
        this.price = BigDecimal.TEN;
    }

    public String getGenre() {
        return genre;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public Set<Album> getInAlbum() {
        return inAlbum;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
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

    public long getLongId() {
        return longId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return longId == song.longId && stock == song.stock && genre.equals(song.genre) && artists.equals(song.artists) && inAlbum.equals(song.inAlbum) && title.equals(song.title) && releaseDate.equals(song.releaseDate) && price.equals(song.price) && mediumType == song.mediumType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longId, genre, artists, inAlbum, title, releaseDate, price, stock, mediumType);
    }
}
