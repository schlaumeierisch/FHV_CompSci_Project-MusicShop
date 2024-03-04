package fhv.musicshop;

import fhv.musicshop.domain.Playlist;
import fhv.musicshop.domain.Song;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@OpenAPIDefinition(
        info = @Info(
                title = "OpenAPIDefinition",
                description = "Playlist Microservice REST API",
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        url = "http://localhost:9001/",
                        description = "Playlist Microservice REST"
                )
        }
)
@Path("")
public class RestController extends Application {

    @GET
    @Path("/welcome")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Welcome to the playlist-microservice :)";
    }

    @Transactional
    @GET
    @Path("/playlist/{ownerId}")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Playlist found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON,
                                            schema = @Schema(implementation = Playlist.class)
                                    )
                            }
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Playlist not found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response getPlaylistByOwnerId(@PathParam("ownerId") String ownerId) {
        PlaylistService playlistService = new PlaylistServiceImpl();
        Optional<Playlist> playlistOptional = playlistService.getPlaylistByOwnerId(ownerId);
        if(playlistOptional.isEmpty()){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Playlist not found")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(playlistOptional.get())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Transactional
    @POST
    @Path("/playlist/addSongs")
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(
            responseCode = "200",
            description = "Songs added to Playlist",
            content = {
                    @Content(
                            mediaType = MediaType.TEXT_PLAIN,
                            schema = @Schema()
                    )
            }
    )
    public Response addSongsToPlaylist(List<Song> songs, @HeaderParam("ownerId") String ownerId){
        PlaylistService playlistService = new PlaylistServiceImpl();
        for (Song song:songs) {
            System.out.println(song.getTitle());
        }
        playlistService.addSongsToPlaylist(ownerId, songs);
        return Response
                .status(Response.Status.OK)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    @Transactional
    @GET
    @Path("/song/owned/{songId}")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Success",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON,
                                            schema = @Schema(implementation = Boolean.class)
                                    )
                            }
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "SongId or OwnerId not found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            }
    )
    public Response isSongOwned(String songId, @HeaderParam("ownerId") String ownerId){
        PlaylistService playlistService = new PlaylistServiceImpl();
        boolean result = false;
        try {
            result = playlistService.isSongOwned(songId, ownerId);
        } catch (NotFoundException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("SongId or OwnerId not found")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .type(MediaType.TEXT_PLAIN)
                .entity(result)
                .build();
    }

}