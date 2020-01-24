package htwb.ai.mundt.services;

import htwb.ai.mundt.playlist.PlayList;
import htwb.ai.mundt.playlist.PlayListService;
import htwb.ai.mundt.song.Song;
import htwb.ai.mundt.song.SongService;
import htwb.ai.mundt.user.User;
import htwb.ai.mundt.user.UserService;
import org.brudergrimm.jmonad.option.Option;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.*;

@Path("/songLists")
public class PlayListController extends RestController {
    private SongService songService;
    private PlayListService playListService;
    private UserService userService;

    @Inject
    public PlayListController(SongService songService, PlayListService playListService, UserService userService) {
        this.songService = songService;
        this.playListService = playListService;
        this.userService = userService;
    }

    @GET
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public Response getForUser(@QueryParam("userId") String userId, @Context ContainerRequestContext context) {
        return Option.apply(userId)
                .flatMap(userService::read)
                .map(requestedUser -> {
                    logInfo(String.format("Found %s", requestedUser));

                    User authenticatedUser = (User) context.getProperty("authenticatedUser");
                    boolean visibleToUser = requestedUser.equals(authenticatedUser);

                    List<PlayList> filtered = playListService.getAll().stream()
                            .filter(playList ->
                                    (!playList.isInvisible() || visibleToUser) &&
                                    playList.getOwner().equals(requestedUser)
                            )
                            .collect(Collectors.toList());

                    logInfo(String.format("Found Playlists: %s", filtered));

                    return Response.ok(new GenericEntity<>(filtered) {});
                })
                .orElseGet(() -> {
                    logInfo(String.format("Didn't find user with id '%s', returning 404", userId));
                    return Response.status(NOT_FOUND);
                })
                .build();
    }

    @GET
    @Path("/{playListId}")
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public Response getForId(@PathParam("playListId") Integer playListId, @Context ContainerRequestContext context) {
        return Option.apply(playListId)
                .flatMap(playListService::read)
                .map(playList -> {
                    logInfo(String.format("Found %s", playList));

                    User authenticatedUser = getUserFromContext(context);
                    if (playList.isInvisible() || playList.getOwner().equals(authenticatedUser)) {
                        return Response.ok(playList);
                    } else return Response.status(FORBIDDEN);
                })
                .orElseGet(() -> {
                    logInfo(String.format("Didn't find playlist with id '%s', returning 404", playListId));
                    return Response.status(NOT_FOUND);
                })
                .build();
    }

    @POST
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    public Response postPlayList(PlayList playlist, @Context ContainerRequestContext context, @Context UriInfo uriInfo) {
        List<Option<Song>> possibleSongs = playlist.getSongs().stream()
                .map(song -> songService.read(song.getId()))
                .collect(Collectors.toList());

        boolean anySongDoesntExist = possibleSongs.stream().anyMatch(Option::isEmpty);

        if (anySongDoesntExist) return Response.status(BAD_REQUEST).build();

        List<Song> songs = possibleSongs.stream()
                .map(Option::get)
                .collect(Collectors.toList());

        User authenticatedUser = getUserFromContext(context);

        playlist.setOwner(authenticatedUser);
        playlist.setSongs(songs);

        return playListService.create(playlist)
                .map(id -> {
                    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                    uriBuilder.path(Integer.toString(id));
                    return Response.created(uriBuilder.build());
                })
                .orElseGet(() -> {
                    logInfo("Couldn't create PlayList");
                    return Response.notAcceptable(null);
                })
                .build();
    }

    @DELETE
    public Response deleteStub() {
        return Response.status(METHOD_NOT_ALLOWED).build();
    }

    @PUT
    public Response putStub() {
        return Response.status(METHOD_NOT_ALLOWED).build();
    }
}
