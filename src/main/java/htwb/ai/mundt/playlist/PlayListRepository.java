package htwb.ai.mundt.playlist;

import htwb.ai.mundt.storage.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class PlayListRepository extends Repository<Integer, PlayList> implements IPlayListRepository {
    @Inject
    public PlayListRepository(EntityManagerFactory emf) {
        super(emf, PlayList.class);
    }
}
