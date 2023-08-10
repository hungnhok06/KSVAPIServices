package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.backend.ksv.repository.sources.IUserRepo;
import vn.backend.ksv.repository.sources.impl.UserRepoImpl;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:06 AM
 */
public class RepositoryModule extends AbstractModule {

    private static final Logger LOGGER = LogManager.getLogger(RepositoryModule.class);

    @Override
    protected void configure() {
        LOGGER.info("RepositoryModule configure");
        bind(IUserRepo.class).to(UserRepoImpl.class).in(Scopes.SINGLETON);
    }
}
