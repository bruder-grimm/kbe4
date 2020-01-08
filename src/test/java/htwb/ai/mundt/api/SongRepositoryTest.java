package htwb.ai.mundt.api;

import htwb.ai.mundt.song.Song;
import htwb.ai.mundt.song.SongRepository;
import org.brudergrimm.jmonad.option.Option;
import org.brudergrimm.jmonad.tried.Try;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SongRepositoryTest {
    private static EntityManagerFactory emf;
    private SongRepository dao;

    @BeforeAll
    public static void initEMF () {
        emf = Persistence.createEntityManagerFactory("songdb_test");
    }

    @BeforeEach
    public void setUpDao() {
        dao = new SongRepository(emf);
    }

    @Test
    public void testCreate() {
        Song song = new Song();
        song.setTitle("What’s Up?");
        song.setArtist("4 Non Blondes");
        song.setLabel("Interscope");
        song.setReleased(1993);

        Try<Integer> songId = dao.create(song);

        assertTrue(songId.isSuccess());

        Option<Song> possibleSongFromDB = dao.findBy(songId.get());

        assertTrue(possibleSongFromDB.isDefined());

        Song songFromDB = possibleSongFromDB.get();

        assertEquals(song.getArtist(), songFromDB.getArtist());
        assertEquals(song.getLabel(), songFromDB.getLabel());
        assertEquals(song.getReleased(), songFromDB.getReleased());
        assertEquals(song.getTitle(), songFromDB.getTitle());
    }

    @Test
    public void testFindAll() {
        List<Song> songs = dao.getAll();
        assertTrue(songs.size() >=1);
        Song contactFromDB = songs.stream().findFirst().get();
        songMatchesExpected(contactFromDB);
    }

    @Test
    public void testFindById() {
        Song song = dao.findBy(1).get();
        songMatchesExpected(song);
    }

    @Test
    public void testUpdate() {
        String expected = "Some cool new title";
        Song song = dao.findBy(7).get();
        song.setTitle(expected);

        assertTrue(dao.update(song));
        assertEquals(expected, dao.findBy(7).get().getTitle());
    }

    @Test
    public void testDelete() {
        Song song = new Song();
        song.setTitle("What’s Up?");
        song.setArtist("4 Non Blondes");
        song.setLabel("Interscope");
        song.setReleased(1993);

        Integer songId = dao.create(song).get();

        dao.delete(songId);

        assertTrue(dao.findBy(songId).isEmpty());
    }

    private void songMatchesExpected(Song song) {
        assertEquals(1, song.getId());
        assertEquals("MacArthur Park", song.getTitle());
        assertEquals("Richard Harris", song.getArtist());
        assertEquals("Dunhill Records", song.getLabel());
        assertEquals(1968, song.getReleased());
    }
}

