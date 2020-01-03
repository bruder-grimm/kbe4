package htwb.ai.mundt.config;

import htwb.ai.mundt.filter.Authenticator;
import htwb.ai.mundt.filter.IAuthenticator;
import htwb.ai.mundt.song.ISongRepository;
import htwb.ai.mundt.song.ISongService;
import htwb.ai.mundt.song.SongRepository;
import htwb.ai.mundt.song.SongService;
import htwb.ai.mundt.user.IUserRepository;
import htwb.ai.mundt.user.IUserService;
import htwb.ai.mundt.user.UserRepository;
import htwb.ai.mundt.user.UserService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DependencyBinder extends AbstractBinder {
    @Override protected void configure() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("songdb");

        SongRepository sr = new SongRepository(emf);
        UserRepository ur = new UserRepository(emf);

        bind(sr).to(ISongRepository.class);
        bind(ur).to(IUserRepository.class);

        UserService us = new UserService(ur);

        bind(new SongService(sr)).to(ISongService.class);
        bind(us).to(IUserService.class);

        bind(new Authenticator(us)).to(IAuthenticator.class);
    }
}
