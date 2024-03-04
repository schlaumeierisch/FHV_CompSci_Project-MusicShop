package fhv.musicshop.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Entity
public class Playlist extends PanacheEntityBase {

    @Id
    private String ownerId;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Song> songs;

    public Playlist(String ownerId) {
        this.ownerId = ownerId;
        this.songs = new LinkedList<>();
    }

    public Playlist() {
    }

    public static Optional<Playlist> findByOwnerId(String ownerId){
        return find("ownerId", ownerId).firstResultOptional();
    }

    public void addSong(Song song){
        songs.add(song);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public List<Song> getSongs() {
        return songs;
    }
}
