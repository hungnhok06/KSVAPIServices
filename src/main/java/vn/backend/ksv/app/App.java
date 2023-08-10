package vn.backend.ksv.app;

import vn.backend.ksv.common.LogAdapter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import vn.backend.ksv.config.ModuleConfig;
import vn.backend.ksv.consumer.impl.ConsumerImpl;
import vn.backend.ksv.consumer.IConsumer;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 3:55 PM
 */
public class App extends AbstractVerticle {

    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());
    public static Injector injector;

    @Override
    public void start() throws Exception {
        LOGGER.info("Starting ksv-backend...");
        if (!context.config().isEmpty()) {
            LOGGER.info("Begin setup ksv backend......");
            injector = Guice.createInjector(new ModuleConfig(context));
            IConsumer consumer = injector.getInstance(ConsumerImpl.class);
            consumer
                    .initKSVAccountRoot()
                    .usingRestful();
        } else {
            LOGGER.error("Not found config file");
            LOGGER.info("ksv has been deployed failed");
            throw new Exception("Not found config file");
        }
    }
}
