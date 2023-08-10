package vn.backend.ksv.services.impl;

import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.pojo.common.CommonRequest;
import vn.backend.ksv.common.pojo.common.RestfulCommonResponse;
import vn.backend.ksv.common.util.IValidationTool;
import vn.backend.ksv.config.Config;
import vn.backend.ksv.repository.sources.IAccountAdminRepo;
import vn.backend.ksv.services.IAuthServices;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 9:49 AM
 */
public class AuthServicesImpl implements IAuthServices {

    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    private IValidationTool validationTool;
    private IAccountAdminRepo accountAdminRepo;
    private Config config;
    @Override
    public void initKSVAccountRoot() {

    }

    @Override
    public RestfulCommonResponse adminLogin(CommonRequest request) {
        return null;
    }
}
