package vn.backend.ksv.repository.sources;

import vn.backend.ksv.repository.base.IBaseRepo;
import vn.backend.ksv.repository.generator.tables.records.UserRecord;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:56 AM
 */
public interface IUserRepo extends IBaseRepo {

    boolean checkAccountRoot();

    UserRecord getAccountUserByUserName(String userName);
}
