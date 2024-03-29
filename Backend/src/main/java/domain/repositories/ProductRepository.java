package domain.repositories;

import domain.Album;
import domain.Artist;
import domain.Song;
import sharedrmi.domain.enums.MediumType;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends Serializable {

    Set<Album> findAlbumsBySongTitle(String title);

    Album findAlbumByLongId(long id);

    Song findSongByLongId(long id);

    List<Song> findSongsByTitle(String title);

    List<Artist> findArtistsByName(String name);

    List<Album> findAlbumsByAlbumTitle(String title);

    Album findAlbumByAlbumTitleAndMedium(String title, MediumType mediumType);

    Optional<Album> findAlbumByAlbumId(String albumId);

    void updateAlbum(Album album);
}
