package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.backend.ksv.services.IAuthServices;
import vn.backend.ksv.services.impl.AuthServicesImpl;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:16 AM
 */
public class ServiceModule extends AbstractModule {

    private static final Logger LOGGER = LogManager.getLogger(ServiceModule.class);

    @Override
    protected void configure() {
        LOGGER.info("ServiceModule configure");

        bind(IAuthServices.class).to(AuthServicesImpl.class).in(Scopes.SINGLETON);
    }
}
