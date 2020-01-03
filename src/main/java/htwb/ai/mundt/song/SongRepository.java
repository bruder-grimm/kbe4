package htwb.ai.mundt.song;

import htwb.ai.mundt.storage.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class SongRepository extends Repository<Integer, Song> implements ISongRepository {
    @Inject
    public SongRepository(EntityManagerFactory emf) {
        super(emf, Song.class);
    }
}
