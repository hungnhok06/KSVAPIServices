package vn.backend.ksv.common.pojo.common;

import com.google.gson.JsonObject;
import vn.backend.ksv.common.constant.key.CodeResponse;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:57 AM
 */
public class RestfulFailureResponse extends RestfulCommonResponse{

    public RestfulFailureResponse() {
        this.code = CodeResponse.HttpStatusCode.BAD_REQUEST.getCode();
        this.messages = CodeResponse.HttpStatusCode.BAD_REQUEST.getMessage();
        this.data = new JsonObject();
    }
}
