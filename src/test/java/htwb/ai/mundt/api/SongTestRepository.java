package htwb.ai.mundt.api;

import htwb.ai.mundt.song.ISongRepository;
import htwb.ai.mundt.song.Song;
import org.brudergrimm.jmonad.option.Option;
import org.brudergrimm.jmonad.tried.Success;
import org.brudergrimm.jmonad.tried.Try;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SongTestRepository implements ISongRepository {
    private static Map<Integer, Song> storage;

    public SongTestRepository() {
        storage = new ConcurrentHashMap<Integer,Song>();
        initSomeContacts();
    }
    private static void initSomeContacts() {
        Song song1 = new Song();
        song1.setId(1);
        song1.setTitle("MacArthur Park");
        song1.setArtist("Richard Harris");
        song1.setLabel("Dunhill Records");
        song1.setReleased(1968);
        storage.put(song1.getId(),song1);

        Song song2 = new Song();
        song2.setId(2);
        song2.setTitle("Afternoon Delight");
        song2.setArtist("Starland Vocal Band");
        song2.setLabel("Windsong");
        song2.setReleased(1976);
        storage.put(song2.getId(),song2);

        Song song3 = new Song();
        song3.setId(3);
        song3.setTitle("Muskrat Love");
        song3.setArtist("Captain and Tennille");
        song3.setLabel("A&M");
        song3.setReleased(1976);
        storage.put(song3.getId(),song3);

        Song song4 = new Song();
        song4.setId(4);
        song4.setTitle("Sussudio");
        song4.setArtist("Phil Collins");
        song4.setLabel("Virgin");
        song4.setReleased(1985);
        storage.put(song4.getId(),song4);

        Song song5 = new Song();
        song5.setId(5);
        song5.setTitle("We Built This City");
        song5.setArtist("Starship");
        song5.setLabel("Grunt/RCA");
        song5.setReleased(1985);
        storage.put(song5.getId(),song5);

        Song song6 = new Song();
        song6.setId(6);
        song6.setTitle("Achy Breaky Heart");
        song6.setArtist("Billy Ray Cyrus");
        song6.setLabel("PolyGram Mercury");
        song6.setReleased(1992);
        storage.put(song6.getId(),song6);

        Song song7 = new Song();
        song7.setId(7);
        song7.setTitle("What’s Up?");
        song7.setArtist("4 Non Blondes");
        song7.setLabel("Interscope");
        song7.setReleased(1993);
        storage.put(song7.getId(),song7);

        Song song8 = new Song();
        song8.setId(8);
        song8.setTitle("Who Let the Dogs Out?");
        song8.setArtist("Baha Men");
        song8.setLabel("S-Curve");
        song8.setReleased(2000);
        storage.put(song8.getId(),song8);

        Song song9 = new Song();
        song9.setId(9);
        song9.setTitle("My Humps");
        song9.setArtist("Black Eyed Peas");
        song9.setLabel("Universal Music");
        song9.setReleased(2005);
        storage.put(song9.getId(),song9);

        Song song10 = new Song();
        song10.setId(10);
        song10.setTitle("Chinese Food");
        song10.setArtist("Alison Gold");
        song10.setLabel("PMW Live");
        song10.setReleased(2013);
        storage.put(song10.getId(),song10);
    }

    @Override
    public List<Song> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Option<Song> findBy(Integer id) {
        return Option.apply(storage.get(id));
    }

    @Override
    public Try<Integer> create(Song entity) {
        int nextId = Collections.max(storage.keySet()) + 1;
        storage.put(nextId, entity);
        return Success.apply(nextId);
    }

    @Override
    public boolean update(Song entity) {
        if(storage.containsKey(entity.getId())){
            storage.put(entity.getId(),entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        if(storage.containsKey(id)){
            storage.remove(id);
            return true;
        };
        return false;
    }

    @Override
    public void close() throws IOException {

    }
}
