package vn.backend.ksv.repository.sources.impl;

import vn.backend.ksv.common.exception.DataException;
import vn.backend.ksv.repository.base.BaseRepository;
import vn.backend.ksv.repository.base.CustomDSLContext;
import vn.backend.ksv.repository.generator.tables.records.UserRecord;
import vn.backend.ksv.repository.sources.IUserRepo;

import static vn.backend.ksv.repository.generator.Tables.USER;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:56 AM
 */
public class UserRepoImpl extends BaseRepository implements IUserRepo {

    @Override
    public boolean checkAccountRoot() {
        try (CustomDSLContext context = getDSLContext()) {
            context.select(USER.USERNAME)
                    .from(USER)
                    .where(USER.USERNAME.eq("root"))
                    .fetch().get(0);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        } catch (Exception e) {
            LOGGER.error("Error on check account it by username cause by {}", e.getMessage());
            throw new DataException.ExecuteException("Error on check account it by username");
        }
    }

    @Override
    public UserRecord getAccountUserByUserName(String userName) {
        LOGGER.info("Start get account Admin bay username {}",userName);
        try (CustomDSLContext context = getDSLContext()) {
            return context.selectFrom(USER)
                    .where(USER.USERNAME.eq(userName))
                    .fetch().get(0);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("Not found account Admin by username");
            throw new DataException.NotFoundException("Not found account Admin by username");
        } catch (Exception e) {
            LOGGER.error("Error on get account Admin by username cause by {}", e.getMessage());
            throw new DataException.ExecuteException("Error on get account Admin by username");
        }
    }
}
