package htwb.ai.mundt.api;
import htwb.ai.mundt.song.*;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import htwb.ai.mundt.services.SongController;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;





class SongControllerTest extends JerseyTest {

    String BASE_URI = "/songs";

    @Override
    protected Application configure() {

        forceSet(TestProperties.CONTAINER_PORT, "0"); // Ohne das laufen die tests nicht nacheinander durch sondern blockieren mit "adress already in use"
                                                            // Quelle: https://stackoverflow.com/a/39835039/5476399
        return new ResourceConfig(SongController.class).register(new TestDependencyBinder());
    }

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

    /*
    with Payload JSON
    No title
    songId == payload id

    expect 400
     */
    @Test
    void putSongWithEqualIdButNoTitleJSON(){

        int equalId = 9;

        Song song = new Song();
        song.setId(equalId);
        song.setTitle("");
        song.setArtist("Black Eyed Peas");
        song.setLabel("Universal Music");
        song.setReleased(2005);

        String path = BASE_URI + "/" + equalId; //path with unequal id

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_JSON_TYPE);

        Response response = target(path).request().put(songEntity); // send PUT
        Assert.assertEquals(400, response.getStatus());
    }

    /*
    with Payload XML
    No title
    songId == payload id

    expect 400
     */
    @Test
    void putSongWithEqualIdButNoTitleXML(){

        int equalId = 9;
        Song song = new Song();
        song.setId(equalId);
        song.setTitle("");
        song.setArtist("Black Eyed Peas");
        song.setLabel("Universal Music");
        song.setReleased(2005);

        String path = BASE_URI + "/" + equalId; //path with unequal id

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_XML_TYPE);

        Response response = target(path).request().put(songEntity); // send PUT
        Assert.assertEquals(400, response.getStatus());
    }

    /*
    with Payload JSON
    No title
    songId != payloadId

    expect 400
     */
    @Test
    void putSongWithUnequalIdsAndNoTitleJSON(){

        int unequalId = 8; // unequal id

        Song song = new Song();
        song.setId(9);
        song.setArtist("Black Eyed Peas");
        song.setLabel("Universal Music");
        song.setReleased(2005);

        String path = BASE_URI + "/" + unequalId; //path with unequal id

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_JSON_TYPE);

        Response response = target(path).request().put(songEntity); // send PUT
        Assert.assertEquals(400, response.getStatus());
    }

    /*
    with Payload XML
    No title
    songId != payloadId

    expect 400
     */
    @Test
    void putSongWithUnequalIdsAndNoTitleXML(){

        int unequalId = 8; // unequal id

        Song song = new Song();
        song.setId(9);
        song.setArtist("Black Eyed Peas");
        song.setLabel("Universal Music");
        song.setReleased(2005);

        String path = BASE_URI + "/" + unequalId; //path with unequal id

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_XML_TYPE);


        Response response = target(path).request().put(songEntity); // send PUT
        Assert.assertEquals(400, response.getStatus());
    }

    /*
    With Payload JSON
    with title
    songId == payloadId

    expect 204
     */
    @Test
    void putSongWithEqualIdsAndWithTitleJSON(){

        int equalId = 9;

        Song song = new Song();
        song.setId(equalId);
        song.setTitle("My Humps");
        song.setArtist("Black Eyed Peas");
        song.setLabel("Universal Music");
        song.setReleased(2005);

        String path = BASE_URI + "/" + equalId;

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_JSON_TYPE);

        Response response = target(path).request().put(songEntity); //send PUT
        Assert.assertEquals(204, response.getStatus());
    }

    /*
    With Payload XML
    with title
    songId == payloadId

    expect 204
     */
    @Test
    void putSongWithEqualIdsAndWithTitleXML(){
        Song song = new Song();
        song.setId(9);
        song.setTitle("My Humps");
        song.setArtist("Black Eyed Peas");
        song.setLabel("Universal Music");
        song.setReleased(2005);

        String path = BASE_URI + "/" + song.getId();

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_XML_TYPE);

        Response response = target(path).request().put(songEntity); //send PUT
        Assert.assertEquals(204, response.getStatus());
    }

    /*
    with payload
    with title
    songId != payloadId

    expect 400
     */
    @Test
    void putSongWithUnequalIdsAndWithTitleJSON(){

        int unequalId = 8; // unequal id

        Song song = new Song();
        song.setId(9);
        song.setTitle("My Humps");
        song.setArtist("Black Eyed Peas");
        song.setLabel("Universal Music");
        song.setReleased(2005);

        String path = BASE_URI + "/" + unequalId; //path with unequal id

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_JSON_TYPE);


        Response response = target(path).request().put(songEntity); // send PUT
        Assert.assertEquals(400, response.getStatus());
    }

    /*
    with payload
    with title
    songId != payloadId

    expect 400
     */
    @Test
    void putSongWithUnequalIdsAndWithTitleXML(){

        int unequalId = 8; // unequal id

        Song song = new Song();
        song.setId(9);
        song.setTitle("My Humps");
        song.setArtist("Black Eyed Peas");
        song.setLabel("Universal Music");
        song.setReleased(2005);

        String path = BASE_URI + "/" + unequalId; //path with unequal id

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_XML_TYPE);

        Response response = target(path).request().put(songEntity); // send PUT
        Assert.assertEquals(400, response.getStatus());
    }

    /*
    without payload -> without title
    with songId


    expect 400
     */
    @Test
    void putSongWithWithoutPayloadJSON(){
        int id = 9; // unequal id

        String path = BASE_URI + "/" + id; //path with unequal id

        Response response = target(path).request().put(Entity.text("wrongInput")); // send PUT
        Assert.assertEquals(415, response.getStatus());
    }

    /*
    with payload JSON

    expect 201 and location string in header
     */
    @Test
    void postWithPayloadJSON() {
        Song song = new Song();
        song.setTitle("TestTitle");
        song.setArtist("TestArtist");
        song.setLabel("TestLabel");
        song.setReleased(1337);

        String path = BASE_URI;  //path with unequal id

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_JSON_TYPE);

        Response response = target(path).request().post(songEntity); // send PUT


        assertEquals(201, response.getStatus());
        String res = response.getLocation().toString();
        System.out.println(res);
        assertTrue(response.getLocation().toString().startsWith("http://"));
    }

    /*
    with payload XML

    expect 201 and location string in header
     */
    @Test
    void postWithPayloadXML() {
        Song song = new Song();
        song.setTitle("TestTitle");
        song.setArtist("TestArtist");
        song.setLabel("TestLabel");
        song.setReleased(1337);

        String path = BASE_URI;  //path with unequal id

        Entity<Song> songEntity = Entity.entity(song, MediaType.APPLICATION_XML_TYPE);

        Response response = target(path).request().post(songEntity); // send PUT

        assertEquals(201, response.getStatus());
        String res = response.getLocation().toString();
        System.out.println(res);
        assertTrue(response.getLocation().toString().startsWith("http://"));
    }

    /*
    without payload JSON

    expect 400
     */
    @Test
    void postWithoutPayload() {

        String path = BASE_URI;  //path with unequal id
        Response response = target(path).request().post(Entity.text("wrongInput")); // send PUT
        assertEquals(415, response.getStatus());

    }


    /*
    with existing id

    expect 204
     */
    @Test
    void deleteExisting() {

        int existingId = 3;

        Response response = target(BASE_URI + "/" + existingId).request().delete();
        assertEquals(204, response.getStatus());
    }

    /*
    with existing id

    expect 400
     */
    @Test
    void deleteExistingNonExisting() {

        int nonExistingId = 1000;

        Response response = target(BASE_URI + "/" + nonExistingId).request().delete();
        assertEquals(400, response.getStatus());
    }
}