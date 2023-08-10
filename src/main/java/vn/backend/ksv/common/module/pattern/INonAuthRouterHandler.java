package vn.backend.ksv.common.module.pattern;

import io.vertx.ext.web.RoutingContext;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 4:44 PM
 */
public interface INonAuthRouterHandler {

    void postHandler(RoutingContext context);

    void getHandler(RoutingContext context);

    void internalHandler(RoutingContext context);

    void serviceHandler(RoutingContext context, IServiceHandler serviceHandler);

    void successHandler(RoutingContext context);

    void errorHandler(RoutingContext context);
}
