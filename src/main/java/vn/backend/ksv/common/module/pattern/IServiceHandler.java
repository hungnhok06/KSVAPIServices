package vn.backend.ksv.common.module.pattern;

import vn.backend.ksv.common.pojo.common.CommonRequest;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:49 AM
 */
@FunctionalInterface
public interface IServiceHandler {

    Object handle(CommonRequest request);
}
