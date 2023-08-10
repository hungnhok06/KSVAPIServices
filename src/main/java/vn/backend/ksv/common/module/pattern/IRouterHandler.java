package vn.backend.ksv.common.module.pattern;

import io.vertx.ext.web.RoutingContext;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:28 AM
 */
public interface IRouterHandler {

    void postHandler(RoutingContext context);

    void getHandler(RoutingContext context);

    void internalUserHandler(RoutingContext context);

    void serviceHandler(RoutingContext context, IServiceHandler serviceHandler);

    void successHandler(RoutingContext context);

    void errorHandler(RoutingContext context);
}
