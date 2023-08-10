package vn.backend.ksv.common.pojo.common;

import com.google.gson.JsonElement;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:50 AM
 */
public class CommonRequest {

    private String code;
    private JsonElement data ;
    private HeaderInternalRequest header;

    public String getCode() {
        return code;
    }

    public CommonRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public JsonElement getData() {
        return data;
    }

    public CommonRequest setData(JsonElement data) {
        this.data = data;
        return this;
    }

    public HeaderInternalRequest getHeader() {
        return header;
    }

    public CommonRequest setHeader(HeaderInternalRequest header) {
        this.header = header;
        return this;
    }
}
