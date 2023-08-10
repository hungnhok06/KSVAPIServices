package vn.backend.ksv.services;

import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.common.pojo.common.CommonRequest;
import vn.backend.ksv.common.pojo.common.RestfulCommonResponse;
import vn.backend.ksv.repository.generator.tables.records.UserRecord;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 9:49 AM
 */
public interface IAuthServices {

    void initKSVAccountRoot();

    void createAdmin(String username, String password);

    void checkPassword(UserRecord record, String password);


    RestfulCommonResponse adminLogin(CommonRequest request);
}
