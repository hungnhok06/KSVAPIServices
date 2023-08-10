package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.backend.ksv.common.util.IValidationTool;
import vn.backend.ksv.common.util.ValidationTool;
import vn.backend.ksv.validation.AuthValidation;
import vn.backend.ksv.validation.IAuthValidation;

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
        bind(IAuthValidation.class).to(AuthValidation.class).in(Scopes.SINGLETON);
    }
}
