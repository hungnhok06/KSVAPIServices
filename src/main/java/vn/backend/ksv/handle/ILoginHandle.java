package vn.backend.ksv.handle;

import vn.backend.ksv.common.reponse.LoginRes;
import vn.backend.ksv.repository.generator.tables.records.UserRecord;

import java.util.List;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 4:02 PM
 */
public interface ILoginHandle {

    LoginRes generateBackofficeToken(UserRecord record, List<String> decentralizationList) throws InterruptedException;
}
