package vn.backend.ksv.repository.base;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.exception.DataException;
import vn.backend.ksv.common.reponse.QueryResponse;
import vn.backend.ksv.common.request.QueryRequest;
import vn.backend.ksv.config.Config;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:01 AM
 */
public class BaseRepository implements IBaseRepo{

    protected final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());


    private HikariDataSource dataSource;
    private Config config;

    @Inject
    public BaseRepository setDataSource(HikariDataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Inject
    public BaseRepository setDbConfig(Config config) {
        this.config = config;
        return this;
    }

    protected Condition equalKey(List<TableField> tableFields, Record record) {
        if (tableFields.size() == 1) {
            //Only 1 primary field
            TableField field = tableFields.get(0);
            return field.eq(field.getDataType().convert(record.getValue(field.getName())));
        } else {
            Condition condition = DSL.trueCondition();
            for (int i = 0; i < tableFields.size(); i++) {
                TableField field = tableFields.get(i);
                condition.and(field.eq(field.getDataType().convert(record.getValue(field.getName()))));
            }
            return condition;
        }
    }

    protected CustomDSLContext getDSLContext() throws SQLException {
        Settings settings = new Settings();
        settings.setDebugInfoOnStackTrace(config.getDbConfig().getDebugInfoOnStackTrace());
        settings.setExecuteLogging(config.getDbConfig().getExecuteLogging());
        return new CustomDSLContext(dataSource.getConnection(), SQLDialect.MARIADB, settings);
    }

    @Override
    public int save(Record record) throws DataException.ExecuteException {
        return 0;
    }

    @Override
    public int saveOrUpdate(Record record) throws DataException.ExecuteException {
        return 0;
    }

    @Override
    public <T extends Record> T findOneById(T record, Condition... conditions) throws DataException.MultiValuesException, DataException.NotFoundException, DataException.QueryException {
        return null;
    }

    @Override
    public <T extends Record, R> R findOneIntoById(T record, Class<R> clazz, Condition... conditions) throws DataException.QueryException {
        return null;
    }

    @Override
    public <T extends Record> List<T> findByCondition(T record, Condition... conditions) throws DataException.QueryException {
        return null;
    }

    @Override
    public <T extends Record, R> List<R> findIntoByCondition(T record, Class<R> clazz, Condition... conditions) throws DataException.QueryException {
        return null;
    }

    @Override
    public int update(Record record, Condition... conditions) throws DataException.ExecuteException {
        return 0;
    }

    @Override
    public int update(Record record) throws DataException.ExecuteException {
        return 0;
    }

    @Override
    public int updateNotNull(Record record) throws DataException.ExecuteException {
        return 0;
    }

    @Override
    public int updateNotNull(Record record, Condition... conditions) throws DataException.ExecuteException {
        return 0;
    }

    @Override
    public int update(Table table, Map<Field, Object> params, Condition... conditions) {
        return 0;
    }

    @Override
    public int count(Table table) throws DataException.QueryException {
        return 0;
    }

    @Override
    public int count(SelectSelectStep select, Table table, Condition condition) {
        return 0;
    }

    @Override
    public int count(SelectSelectStep select, Table table, Condition condition, SelectSelectStep select2, Table table2, Condition condition2) {
        return 0;
    }

    @Override
    public <R extends Record> R fetchOneNotNull(Table<R> table, Condition condition) {
        return null;
    }

    @Override
    public <R extends Record> R fetchOneCanNull(Table<R> table, Condition condition) {
        return null;
    }

    @Override
    public <T> T fetchOneNotNull(Table<?> table, Condition condition, Class<? extends T> type) {
        return null;
    }

    @Override
    public <T> T fetchOneNotNull(Table<?> table, Condition condition, Class<? extends T> type, Field<?>[] fields) {
        return null;
    }

    @Override
    public boolean fetchExists(Table<?> table, Condition condition) {
        return false;
    }

    @Override
    public <R extends Record> List<R> fetch(Table<R> table, Condition condition) {
        return null;
    }

    @Override
    public <T> QueryResponse<T> fetch(QueryRequest request, Table<?> table, Condition condition, Class<? extends T> type) {
        return null;
    }

    @Override
    public <T> QueryResponse<T> fetch(QueryRequest request, Table<?> table, Condition condition, Class<? extends T> type, Field<?>[] fields) {
        return null;
    }

    @Override
    public void batchTransaction(CustomDSLContext context, Collection<Query> queries) {

    }

    @Override
    public void batch(CustomDSLContext context, Collection<Query> queries) {

    }
}
