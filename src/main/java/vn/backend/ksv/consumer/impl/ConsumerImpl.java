package vn.backend.ksv.consumer.impl;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import lombok.RequiredArgsConstructor;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.constant.key.ConfigAPI;
import vn.backend.ksv.common.module.pattern.INonAuthRouterHandler;
import vn.backend.ksv.common.module.pattern.IRouterHandler;
import vn.backend.ksv.config.Config;
import vn.backend.ksv.consumer.IConsumer;
import vn.backend.ksv.services.IAuthServices;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 4:05 PM
 */
public class ConsumerImpl implements IConsumer {

    private final static int MB_10 = 1024 * 1000 * 10;

    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());
    //
    private final Vertx vertx;
    private final Gson gson;
    private final Config config;
    private final IAuthServices authServices;
    private final IRouterHandler routerHandler;
    private final INonAuthRouterHandler nonAuthRouterHandler;

    @Inject
    ConsumerImpl(Vertx vertx,
                 Config config,
                 Gson gson,
                 IAuthServices authServices,
                 IRouterHandler routerHandler,
                 INonAuthRouterHandler nonAuthRouterHandler){

        this.vertx = vertx;
        this.config = config;
        this.gson = gson;
        this.authServices = authServices;
        this.routerHandler = routerHandler;
        this.nonAuthRouterHandler = nonAuthRouterHandler;
    }


    @Override
    public IConsumer usingRestful() {
        HttpServer server = vertx.createHttpServer(new HttpServerOptions()
                .setHost(config.getHttpServer().getHost())
                .setPort(config.getHttpServer().getPort())
                .setIdleTimeout(config.getIdleTimeout())
        );
        Router router = Router.router(vertx);

        apiKyc(router);

        server.requestHandler(router);
        LOGGER.info("Start restful user services");
        server.listen(event -> {
            if (event.succeeded()) {
                LOGGER.info("Listen on port {}", config.getHttpServer().getPort());
            } else {
                LOGGER.info("Listen failed on port {} cause by {}", config.getHttpServer().getPort(), event.cause().getMessage());
            }
        });
        return this;
    }

    @Override
    public IConsumer initKSVAccountRoot() {
        LOGGER.info("Init Account admin ksv");
        authServices.initKSVAccountRoot();
        return this;
    }


    private void apiKyc(Router router) {
        router.post(ConfigAPI.Admin.getLogin())
                .handler(nonAuthRouterHandler::postHandler)
                .blockingHandler(nonAuthRouterHandler::internalHandler)
                .blockingHandler(ctx -> nonAuthRouterHandler.serviceHandler(ctx, authServices::adminLogin), false)
                .blockingHandler(nonAuthRouterHandler::successHandler).failureHandler(nonAuthRouterHandler::errorHandler);
    }
}
