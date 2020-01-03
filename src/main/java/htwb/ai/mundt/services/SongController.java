package htwb.ai.mundt.services;

import htwb.ai.mundt.song.ISongService;
import htwb.ai.mundt.song.Song;
import htwb.ai.mundt.song.SongService;
import org.brudergrimm.jmonad.option.Option;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/songs")
public class SongController extends RestController {
    private final ISongService service;

    @Inject
    public SongController(SongService service) {
        this.service = service;
    }

    @GET
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public List<Song> getAll() {
        logInfo("Getting all songs");
        return service.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public Response get(@PathParam("id") Integer songId) {
        Option<Song> possibleSong = Option.apply(songId).flatMap(service::read);
        if (possibleSong.isDefined()) {
            Song song = possibleSong.get();
            logInfo(String.format("Found %s, returning", song));
            return Response.ok(song).build();
        }
        logInfo(String.format("Didn't find song with id '%s', returning 404", songId));
        return Response.status(NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    public Response put(@PathParam("id") Integer songId, Song song) {
        return Option.apply(songId)
                .filter(id -> song.getId().equals(id) && !song.getTitle().isEmpty())
                .map(id -> {
                    logInfo(String.format("Putting song with id '%s'", id));
                    return service.update(song);
                })
                .filter(updated -> updated == true)
                .map(i -> {
                    logInfo(String.format("Song with id '%s' successfully updated", songId));
                    return Response.noContent();
                })
                .orElseGet(() -> {
                    logInfo(String.format("Couldn't update Song with id '%s'", songId));
                    return Response.status(BAD_REQUEST);
                })
                .build();
    }

    @POST
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    public Response post(Song song, @Context UriInfo uriInfo) {
        return service.create(song)
                .map(id -> {
                    logInfo(String.format("Song created successfully with id '%s'", id));
                    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                    uriBuilder.path(Integer.toString(id));
                    return Response.created(uriBuilder.build());
                })
                .orElseGet(() -> {
                    logInfo("Couldn't create Song");
                    return Response.notAcceptable(null);
                })
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer songId) {
        return Option.apply(songId)
                .map(service::delete)
                .filter(deleted -> deleted == true) // could have filtered for boolean value but this reads nicer
                .map(i -> Response.noContent())
                .getOrElse(Response.status(BAD_REQUEST))
                .build();
    }
}