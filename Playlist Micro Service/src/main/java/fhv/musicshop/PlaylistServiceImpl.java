package fhv.musicshop;

import fhv.musicshop.domain.Album;
import fhv.musicshop.domain.Artist;
import fhv.musicshop.domain.Playlist;
import fhv.musicshop.domain.Song;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlaylistServiceImpl implements PlaylistService {

    @Override
    public void addSongsToPlaylist(String ownerId, List<Song> songs) {

        List<Song> deduped = songs.stream().distinct().collect(Collectors.toList());

        Optional<Playlist> playlistOpt = Playlist.findByOwnerId(ownerId);
        Playlist playlist;
        if (playlistOpt.isEmpty()) {
            playlist = new Playlist(ownerId);
            playlist.persist();
        } else {
            playlist = playlistOpt.get();
        }

        for (Song s : deduped) {

            for (Artist artist : s.getArtists()) {
                Optional<Artist> existingArtist = Artist.find("name", artist.getName()).firstResultOptional();
                if (existingArtist.isEmpty()) {
                    artist.persist();
                }
            }
            for (Album album : s.getInAlbum()) {
                Optional<Album> existingAlbum = Album.find("albumId", album.getAlbumId()).firstResultOptional();
                if (existingAlbum.isEmpty()) {
                    album.persist();
                }
            }
            Optional<Song> existingSong = Song.find("longId", s.getLongId()).firstResultOptional();
            if (existingSong.isEmpty()) {
                s.persist();
            }
            if(playlist.getSongs().stream().noneMatch(song -> song.getLongId() == s.getLongId())) {
                playlist.addSong(s);
            }
        }
    }


    @Override
    public Optional<Playlist> getPlaylistByOwnerId(String ownerId) {
        return Playlist.findByOwnerId(ownerId);
    }

    @Override
    public boolean isSongOwned(String songId, String ownerId) {
        Optional<Playlist> playlist = Playlist.findByOwnerId(ownerId);
        if (playlist.isEmpty()) {
            throw new NotFoundException();
        }
        Optional<Song> song = Song.find("id", Long.parseLong(songId)).firstResultOptional();
        if (song.isEmpty()) {
            throw new NotFoundException();
        }

        return playlist.get().getSongs().contains(song.get());
    }
}
