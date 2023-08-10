package vn.backend.ksv.common.pojo.common;

import com.google.gson.JsonObject;
import vn.backend.ksv.common.constant.key.CodeResponse;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 4:13 PM
 */
public class RestfulSuccessResponse extends RestfulCommonResponse {

    public RestfulSuccessResponse() {
        this.code = CodeResponse.HttpStatusCode.OK.getCode();
        this.messages = CodeResponse.HttpStatusCode.OK.getMessage();
        this.data = new JsonObject();
    }
}
