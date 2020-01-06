package htwb.ai.mundt.playlist;


import htwb.ai.mundt.storage.CRUD;

import javax.inject.Inject;
import java.util.List;

public class PlayListService extends CRUD<Integer, PlayList> implements IPlayListService {

    @Inject
    public PlayListService(IPlayListRepository repository) {
        super(repository);
    }

    public List<PlayList> getAll() {
        return repository.getAll();
    }
}
