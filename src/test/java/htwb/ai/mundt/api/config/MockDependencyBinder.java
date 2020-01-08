package htwb.ai.mundt.api.config;

import htwb.ai.mundt.api.database.InMemorySongRepository;
import htwb.ai.mundt.api.database.InMemoryUserRepository;
import htwb.ai.mundt.filter.Authenticator;
import htwb.ai.mundt.filter.IAuthenticator;
import htwb.ai.mundt.song.ISongRepository;
import htwb.ai.mundt.song.ISongService;
import htwb.ai.mundt.song.SongService;
import htwb.ai.mundt.user.IUserRepository;
import htwb.ai.mundt.user.IUserService;
import htwb.ai.mundt.user.UserService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MockDependencyBinder extends AbstractBinder {
    @Override
    protected void configure() {
        InMemorySongRepository str = new InMemorySongRepository();
        InMemoryUserRepository utr = new InMemoryUserRepository();

        bind(str).to(ISongRepository.class);
        bind(utr).to(IUserRepository.class);

        UserService us = new UserService(utr);

        bind(new SongService(str)).to(ISongService.class);
        bind(us).to(IUserService.class);
        bind(new Authenticator(us)).to(IAuthenticator.class);
    }
}