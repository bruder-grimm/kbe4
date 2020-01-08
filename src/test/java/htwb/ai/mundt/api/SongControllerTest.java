package htwb.ai.mundt.api;

import htwb.ai.mundt.api.config.MockDependencyBinder;
import htwb.ai.mundt.services.SongController;
import htwb.ai.mundt.song.Song;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SongControllerTest extends JerseyTest {
    private static final String BASE_URI = "/songs";

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0");
        return new ResourceConfig(SongController.class).register(new MockDependencyBinder());
    }

    @Test
    void putSongWithEqualIdButNoTitleJSON(){
        Song song = songWith(9, "Black Eyed Peas", "", "Universal Music", 2005);
        Response response = target(pathWithId(song.getId())).request().put(getJsonEntity(song));

        assertEquals(400, response.getStatus());
    }

    @Test
    void putSongWithEqualIdButNoTitleXML(){
        Song song = songWith(9, "Black Eyed Peas", "", "Universal Music", 2005);
        Response response = target(pathWithId(song.getId())).request().put(getXMLEntity(song));

        assertEquals(400, response.getStatus());
    }

    @Test
    void putSongWithUnequalIdsAndNoTitleJSON(){
        Song song = songWith(9, "Black Eyed Peas", "", "Universal Music", 2005);
        Response response = target(pathWithId(8)).request().put(getJsonEntity(song));

        assertEquals(400, response.getStatus());
    }

    @Test
    void putSongWithUnequalIdsAndNoTitleXML(){
        Song song = songWith(9, "Black Eyed Peas", "", "Universal Music", 2005);
        Response response = target(pathWithId(8)).request().put(getXMLEntity(song));

        assertEquals(400, response.getStatus());
    }

    @Test
    void putSongWithEqualIdsAndWithTitleJSON(){
        Song song = songWith(9, "Black Eyed Peas", "My Humps", "Universal Music", 2005);
        Response response = target(pathWithId(9)).request().put(getJsonEntity(song));

        assertEquals(204, response.getStatus());
    }

    @Test
    void putSongWithEqualIdsAndWithTitleXML(){
        Song song = songWith(9, "Black Eyed Peas", "My Humps", "Universal Music", 2005);
        Response response = target(pathWithId(song.getId())).request().put(getXMLEntity(song));

        assertEquals(204, response.getStatus());
    }

    @Test
    void putSongWithUnequalIdsAndWithTitleJSON(){
        Song song = songWith(9, "Black Eyed Peas", "My Humps", "Universal Music", 2005);
        Response response = target(pathWithId(8)).request().put(getJsonEntity(song));

        assertEquals(400, response.getStatus());
    }

    @Test
    void putSongWithUnequalIdsAndWithTitleXML(){
        Song song = songWith(9, "Black Eyed Peas", "My Humps", "Universal Music", 2005);
        Response response = target(pathWithId(8)).request().put(getXMLEntity(song));

        assertEquals(400, response.getStatus());
    }

    @Test
    void putSongWithWithoutPayloadJSON(){
        Response response = target(pathWithId(9)).request().put(Entity.text("wrongInput")); // send PUT

        assertEquals(415, response.getStatus());
    }

    @Test
    void postWithPayloadJSON() {
        Song song = songWith("TestArtist", "TestTitle", "TestLabel", 1337);

        Response response = target(BASE_URI).request().post(getJsonEntity(song));

        assertEquals(201, response.getStatus());
        assertTrue(response.getLocation().toString().startsWith("http://"));
    }

    @Test
    void postWithPayloadXML() {
        Song song = songWith("TestArtist", "TestTitle", "TestLabel", 1337);

        Response response = target(BASE_URI).request().post(getXMLEntity(song));

        assertEquals(201, response.getStatus());
        String res = response.getLocation().toString();
        System.out.println(res);
        assertTrue(response.getLocation().toString().startsWith("http://"));
    }

    @Test
    void postWithoutPayload() {
        Response response = target(BASE_URI).request().post(Entity.text("wrongInput")); // send PUT
        assertEquals(415, response.getStatus());
    }

    @Test
    void deleteExisting() {
        int existingId = 3;

        Response response = target(pathWithId(existingId)).request().delete();
        assertEquals(204, response.getStatus());
    }

    @Test
    void deleteExistingNonExisting() {
        int nonExistingId = 1000;

        Response response = target(pathWithId(nonExistingId)).request().delete();
        assertEquals(400, response.getStatus());
    }

    private static Song songWith(String artist, String title, String label, int released) {
        Song song = new Song();
        song.setArtist(artist);
        song.setTitle(title);
        song.setLabel(label);
        song.setReleased(released);
        return song;
    }

    private static Song songWith(int id, String artist, String title, String label, int released) {
        Song song = songWith(artist, title, label, released);
        song.setId(id);
        return song;
    }

    private static Entity<Song> getJsonEntity(Song song) {
        return Entity.entity(song, MediaType.APPLICATION_JSON_TYPE);
    }

    private static Entity<Song> getXMLEntity(Song song) {
        return Entity.entity(song, MediaType.APPLICATION_XML_TYPE);
    }

    private static String pathWithId(int id) {
        return BASE_URI + "/" + id;
    }
}