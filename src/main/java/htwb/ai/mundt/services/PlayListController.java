package htwb.ai.mundt.services;

import htwb.ai.mundt.playlist.PlayList;
import htwb.ai.mundt.playlist.PlayListForm;
import htwb.ai.mundt.playlist.PlayListService;
import htwb.ai.mundt.song.Song;
import htwb.ai.mundt.song.SongService;
import htwb.ai.mundt.user.User;
import htwb.ai.mundt.user.UserService;
import org.brudergrimm.jmonad.option.Option;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

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
                                    (playList.isVisible() || visibleToUser) &&
                                    playList.getOwner().equals(requestedUser)
                            )
                            .collect(Collectors.toList());

                    return Response.ok(filtered);
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

                    User authenticatedUser = (User) context.getProperty("authenticatedUser");
                    if (playList.isVisible() || playList.getOwner().equals(authenticatedUser)) {
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
    public Response postPlayList(PlayListForm playListForm, @Context ContainerRequestContext context, @Context UriInfo uriInfo) {
        List<Option<Song>> possibleSongs = playListForm.getSongs().stream()
                .map(songService::read)
                .collect(Collectors.toList());

        boolean anySongDoesntExist = possibleSongs.stream().anyMatch(Option::isEmpty);

        if (anySongDoesntExist) return Response.status(BAD_REQUEST).build();

        List<Song> songs = possibleSongs.stream()
                .map(Option::get)
                .collect(Collectors.toList());

        User authenticatedUser = (User) context.getProperty("authenticatedUser");

        PlayList playList = new PlayList();
        playList.setName(playListForm.getName());
        playList.setOwner(authenticatedUser);
        playList.setSongs(songs);
        playList.setVisible(playListForm.isVisible());

        return playListService.create(playList)
                .map(id ->{
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
}
