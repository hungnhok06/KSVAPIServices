package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.backend.ksv.common.module.pattern.INonAuthRouterHandler;
import vn.backend.ksv.common.module.pattern.IRouterHandler;
import vn.backend.ksv.common.module.pattern.NonAuthRouterHandlerImpl;
import vn.backend.ksv.common.module.pattern.RouterHandlerImpl;
import vn.backend.ksv.handle.ILoginHandle;
import vn.backend.ksv.handle.impl.LoginHandleImpl;

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
        bind(INonAuthRouterHandler.class).to(NonAuthRouterHandlerImpl.class).in(Scopes.SINGLETON);
        bind(ILoginHandle.class).to(LoginHandleImpl.class).in(Scopes.SINGLETON);
    }
}
