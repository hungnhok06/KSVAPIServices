package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.backend.ksv.consumer.IConsumer;
import vn.backend.ksv.consumer.impl.ConsumerImpl;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 9:16 AM
 */
public class ConsumerModule extends AbstractModule {

    private static final Logger LOGGER = LogManager.getLogger(ConsumerModule.class);

    @Override
    protected void configure() {
        LOGGER.info("ConsumerModule configure");

        bind(IConsumer.class).to(ConsumerImpl.class).in(Scopes.SINGLETON);
    }
}
