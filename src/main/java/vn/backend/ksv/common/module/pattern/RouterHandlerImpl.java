package vn.backend.ksv.common.module.pattern;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.auth.IJwtAuth;
import vn.backend.ksv.common.auth.multisession.IJwtAuthNonCache;
import vn.backend.ksv.common.constant.key.CodeResponse;
import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.common.pojo.common.CommonRequest;
import vn.backend.ksv.common.pojo.common.HeaderInternalRequest;
import vn.backend.ksv.common.pojo.common.RestfulCommonResponse;
import vn.backend.ksv.common.pojo.common.RestfulFailureResponse;
import vn.backend.ksv.common.util.Generator;
import vn.backend.ksv.common.util.JsonConverter;

import java.util.List;
import java.util.Map;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:29 AM
 */
public class RouterHandlerImpl implements IRouterHandler{

    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    private IJwtAuthNonCache jwtAuthNonCache;
    private IJwtAuth jwtAuth;


    @Inject
    RouterHandlerImpl(IJwtAuthNonCache jwtAuthNonCache,
                      IJwtAuth jwtAuth

    ) {
        this.jwtAuthNonCache = jwtAuthNonCache;
        this.jwtAuth =jwtAuth;
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
    public void internalUserHandler(RoutingContext context) {
        try {
            JsonObject headers = JsonConverter.fromMultiMap(context.request().headers());
            JsonObject body = JsonConverter.fromJson(context.getBodyAsString(), JsonObject.class);
            String collationId = Generator.generate();
            LOGGER.info("Start handle request {} with collationId {}", body, collationId);
            HeaderInternalRequest header = jwtAuth.validateToken(headers.get("token").getAsString(), collationId, StaticEnum.TokenType.ADMIN_LOGIN);
            CommonRequest request = new CommonRequest()
                    .setHeader(header)
                    .setData(body);
            context.put("request", request);
            context.next();
        } catch (CommonException.ForbiddenException e) {
            context.fail(Integer.parseInt(CodeResponse.ClientErrorCode.FORBIDDEN_ERROR.getCode()));
        } catch (CommonException.TokenExpiredByTimeoutException e) {
            context.fail(Integer.parseInt(CodeResponse.ClientErrorCode.AUTHENTICATION_TOKEN_EXPIRE_BY_TIMEOUT.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void serviceHandler(RoutingContext context, IServiceHandler serviceHandler) {
        try {
            CommonRequest request = context.get("request");
            context.put("response", serviceHandler.handle(request));
            context.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void successHandler(RoutingContext context) {
        Object response = context.get("response");
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
                    .setCode(String.valueOf(context.statusCode()))
                    .setMessages(null)
                    .setData(new JsonObject());
            LOGGER.debug("Response: {}", JsonConverter.toJson(response));
            context.response()
                    .putHeader("Content-Type", "application/json")
                    .setStatusCode(CodeResponse.HttpStatusCode.OK.getIntCode())
                    .setStatusMessage(CodeResponse.HttpStatusCode.OK.getMessage())
                    .end(JsonConverter.toJson(response));
        }
    }
}
