package vn.backend.ksv.services;

import vn.backend.ksv.common.pojo.common.CommonRequest;
import vn.backend.ksv.common.pojo.common.RestfulCommonResponse;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 9:49 AM
 */
public interface IAuthServices {

    void initKSVAccountRoot();

    RestfulCommonResponse adminLogin(CommonRequest request);
}
