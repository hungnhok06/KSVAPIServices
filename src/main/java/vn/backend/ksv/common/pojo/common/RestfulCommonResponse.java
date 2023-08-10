package vn.backend.ksv.common.pojo.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import vn.backend.ksv.common.auth.response.AuthResponseCode;
import vn.backend.ksv.common.constant.key.CodeResponse;
import vn.backend.ksv.common.exception.CommonException;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:51 AM
 */
public class RestfulCommonResponse {

    protected String code;
    protected JsonElement data;
    protected String messages;

    public String getCode() {
        return code;
    }

    public RestfulCommonResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public JsonElement getData() {
        return data;
    }

    public RestfulCommonResponse setData(JsonElement data) {
        this.data = data;
        return this;
    }

    public RestfulCommonResponse putData(String key, JsonElement data) {
        if (!this.data.isJsonObject()) {
            throw new CommonException.UnknownException("Data not is json object, can't put more key");
        }
        this.data.getAsJsonObject().add(key, data);
        return this;
    }

    public String getMessages() {
        return messages;
    }

    public RestfulCommonResponse setMessages(String messages) {
        this.messages = messages;
        return this;
    }

    public RestfulCommonResponse setResponse(CodeResponse.HttpStatusCode code) {
        this.code = String.valueOf(code.getCode());
        this.messages = code.getMessage();
        return this;
    }

    public RestfulCommonResponse setResponse(CodeResponse.ServerErrorCode code) {
        this.code = String.valueOf(code.getCode());
        this.messages = code.getMessage();
        return this;
    }

    public RestfulCommonResponse setResponse(CodeResponse.ClientErrorCode code) {
        this.code = String.valueOf(code.getCode());
        this.messages = code.getMessage();
        return this;
    }

    public RestfulCommonResponse setResponse(CodeResponse.SuccessCode code) {
        this.code = String.valueOf(code.getCode());
        this.messages = code.getMessage();
        return this;
    }

    public RestfulCommonResponse setResponse(AuthResponseCode code) {
        this.code = String.valueOf(code.getCode());
        this.messages = code.getMessage();
        return this;
    }

    public static class Builder {
        public static String response(RestfulCommonResponse response) {
            return new Gson().toJson(response);
        }
    }
}
