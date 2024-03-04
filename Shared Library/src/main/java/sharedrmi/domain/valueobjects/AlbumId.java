package sharedrmi.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
public class AlbumId implements Serializable {

    private final UUID albumId;

    public AlbumId() {
        this.albumId = UUID.randomUUID();
    }

    public AlbumId(String albumId) {
        this.albumId = UUID.fromString(albumId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumId albumId1 = (AlbumId) o;
        return Objects.equals(albumId, albumId1.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId);
    }
}