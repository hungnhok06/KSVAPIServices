package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.backend.ksv.common.module.pattern.IRouterHandler;
import vn.backend.ksv.common.module.pattern.RouterHandlerImpl;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:06 AM
 */
public class HandlerModule extends AbstractModule{

    private static final Logger LOGGER = LogManager.getLogger(HandlerModule.class);

    @Override
    protected void configure() {
        LOGGER.info("HandlerModule configure");

        bind(IRouterHandler.class).to(RouterHandlerImpl.class).in(Scopes.SINGLETON);
    }
}
