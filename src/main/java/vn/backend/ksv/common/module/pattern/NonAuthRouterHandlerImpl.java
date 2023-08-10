package vn.backend.ksv.common.module.pattern;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.constant.key.CodeResponse;
import vn.backend.ksv.common.pojo.common.CommonRequest;
import vn.backend.ksv.common.pojo.common.HeaderInternalRequest;
import vn.backend.ksv.common.pojo.common.RestfulCommonResponse;
import vn.backend.ksv.common.pojo.common.RestfulFailureResponse;
import vn.backend.ksv.common.util.DateTimes;
import vn.backend.ksv.common.util.Generator;
import vn.backend.ksv.common.util.JsonConverter;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 4:45 PM
 */
public class NonAuthRouterHandlerImpl implements INonAuthRouterHandler{

    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    private Vertx vertx;

    @Inject
    NonAuthRouterHandlerImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void postHandler(RoutingContext context) {
        context.request().bodyHandler(body -> {
            context.setBody(body);
            context.next();
        });
    }

    @Override
    public void getHandler(RoutingContext context) {
        context.setBody(Buffer.buffer(JsonConverter.toJson(JsonConverter.fromMultiMap(context.request().params()))));
        context.next();
    }

    @Override
    public void internalHandler(RoutingContext context) {
        try {
            LOGGER.info("Start handle uri {} with method {}, body {}", context.request().uri(), context.request().rawMethod(), context.getBodyAsString());
            JsonObject headers = JsonConverter.fromMultiMap(context.request().headers());
            JsonObject body = JsonConverter.fromJson(context.getBodyAsString(), JsonObject.class);
            String collationId = Generator.generate();
            LOGGER.info("Start handle request {} with collationId {}", body, collationId);
            HeaderInternalRequest headerInternalRequest = new HeaderInternalRequest();
            if (headers.get("X-Real-IP") != null) {
                headerInternalRequest.setDeviceIPAddress(headers.get("X-Real-IP").getAsString());
            }
            CommonRequest request = new CommonRequest()
                    .setData(body)
                    .setHeader(headerInternalRequest);
            context.put("request", request);
            context.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serviceHandler(RoutingContext context, IServiceHandler serviceHandler) {
        CommonRequest request = context.get("request");
        WorkerExecutor executor = vertx.createSharedWorkerExecutor("default");
        if (request.getHeader() != null && request.getHeader().getUserId() != null) {
            executor = vertx.createSharedWorkerExecutor(DateTimes.getCurrentTimeMillis() + "_" + request.getHeader().getUserId());
        }
        executor.executeBlocking(handle ->{
            try {
                LOGGER.info("Router Thread {}", Thread.currentThread().getName());
                context.put("response", serviceHandler.handle(request));
                handle.complete();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }, false, rs->{
            if (rs.succeeded()) {
                context.next();
            } else {
                context.fail(rs.cause());
            }
        });
    }

    @Override
    public void successHandler(RoutingContext context) {
        RestfulCommonResponse response = context.get("response");
        LOGGER.debug("Response: {}", JsonConverter.toJson(response));
        context.response()
                .putHeader("Content-Type", "application/json")
                .setStatusCode(CodeResponse.HttpStatusCode.OK.getIntCode())
                .setStatusMessage(CodeResponse.HttpStatusCode.OK.getMessage())
                .end(JsonConverter.toJson(response));
    }

    @Override
    public void errorHandler(RoutingContext context) {
        if (context.failure() != null) {
            LOGGER.warn("Cause class {}", context.failure());
            RestfulCommonResponse response = new RestfulFailureResponse()
                    .setResponse(CodeResponse.ServerErrorCode.INTERNAL_SERVER);
            LOGGER.debug("Response: {}", JsonConverter.toJson(response));
            context.response()
                    .putHeader("Content-Type", "application/json")
                    .setStatusCode(CodeResponse.HttpStatusCode.OK.getIntCode())
                    .setStatusMessage(CodeResponse.HttpStatusCode.OK.getMessage())
                    .end(JsonConverter.toJson(response));
        } else {
            LOGGER.warn("Cause status {}", context.statusCode());
            RestfulCommonResponse response = new RestfulFailureResponse()
                    .setCode(String.valueOf(context.statusCode()));
            LOGGER.debug("Response: {}", JsonConverter.toJson(response));
            context.response()
                    .putHeader("Content-Type", "application/json")
                    .setStatusCode(CodeResponse.HttpStatusCode.OK.getIntCode())
                    .setStatusMessage(CodeResponse.HttpStatusCode.OK.getMessage())
                    .end(JsonConverter.toJson(response));
        }
    }
}
