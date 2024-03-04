package sharedrmi.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ArtistDTO implements Serializable {

    private String name;

    public ArtistDTO() {
    }

    @Builder
    public ArtistDTO(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
