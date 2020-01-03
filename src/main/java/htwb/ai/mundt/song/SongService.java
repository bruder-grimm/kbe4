package htwb.ai.mundt.song;


import htwb.ai.mundt.storage.CRUD;

import javax.inject.Inject;
import java.util.List;

public class SongService extends CRUD<Integer, Song> implements ISongService {

    @Inject
    public SongService(ISongRepository repository) {
        super(repository);
    }

    public List<Song> getAll() {
        return repository.getAll();
    }
}
