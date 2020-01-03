package htwb.ai.mundt.user;

import htwb.ai.mundt.storage.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class UserRepository extends Repository<String, User> implements IUserRepository {
    @Inject
    public UserRepository(EntityManagerFactory emf) {
        super(emf, User.class);
    }
}
