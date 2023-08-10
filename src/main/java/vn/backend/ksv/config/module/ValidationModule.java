package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.backend.ksv.common.module.pattern.IRouterHandler;
import vn.backend.ksv.common.module.pattern.RouterHandlerImpl;
import vn.backend.ksv.common.util.IValidationTool;
import vn.backend.ksv.common.util.ValidationTool;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:07 AM
 */
public class ValidationModule extends AbstractModule {

    private static final Logger LOGGER = LogManager.getLogger(ValidationModule.class);

    @Override
    protected void configure() {
        LOGGER.info("ValidationModule configure");

        bind(IValidationTool.class).to(ValidationTool.class).in(Scopes.SINGLETON);
    }
}
