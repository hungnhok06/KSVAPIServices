package vn.backend.ksv.repository.base;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.TableRecordImpl;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.exception.DataException;
import vn.backend.ksv.common.reponse.QueryResponse;
import vn.backend.ksv.common.request.common.QueryRequest;
import vn.backend.ksv.common.util.States;
import vn.backend.ksv.config.Config;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
        try (CustomDSLContext context = getDSLContext()) {
            record.attach(context.configuration());
            return ((TableRecordImpl) record).insert();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Failed to insert new record into database cause by {}", e.getMessage());
            throw new DataException.ExecuteException("Failed to insert new record: " + e.getMessage());
        }
    }

    @Override
    public int saveOrUpdate(Record record) throws DataException.ExecuteException {
        try (CustomDSLContext context = getDSLContext()) {
            return context
                    .insertInto(((TableRecord<?>) record).getTable())
                    .set(record)
                    .onDuplicateKeyUpdate()
                    .set(record)
                    .execute();
        } catch (Exception e) {
            LOGGER.error("Error on save or update record into database cause by {}", e.getMessage());
            throw new DataException.ExecuteException("Failed to insert or update new record: " + e.getMessage());
        }
    }

    @Override
    public <T extends Record> T findOneById(T record, Condition... conditions) throws DataException.MultiValuesException, DataException.NotFoundException, DataException.QueryException {
        try (CustomDSLContext context = getDSLContext()) {
            List<T> lst = (List<T>) context.selectFrom(((TableRecord<?>) record).getTable())
                    .where(conditions)
                    .fetch();
            if (lst.size() > 1) throw new DataException.MultiValuesException("Result is more than 1 value");
            return lst.get(0);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("Not found record by condition");
            throw new DataException.NotFoundException("Not found find record by condition");
        } catch (Exception e) {
            LOGGER.error("Error on find by condition cause by {}", e.getMessage());
            throw new DataException.QueryException("Error on find by condition " + e.getMessage());
        }
    }

    @Override
    public <T extends Record, R> R findOneIntoById(T record, Class<R> clazz, Condition... conditions) throws DataException.QueryException {
        try (CustomDSLContext context = getDSLContext()) {
            List<R> lst = context.selectFrom(((TableRecord<?>) record).getTable())
                    .where(conditions)
                    .fetchInto(clazz);
            if (lst.size() > 1) throw new DataException.MultiValuesException("Result is more than 1 value");
            return lst.get(0);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("Not found record by condition");
            throw new DataException.NotFoundException("Not found find record by condition");
        } catch (Exception e) {
            LOGGER.error("Error on find by condition cause by {}", e.getMessage());
            throw new DataException.QueryException("Error on find by condition " + e.getMessage());
        }
    }

    @Override
    public <T extends Record> List<T> findByCondition(T record, Condition... conditions) {
        try (CustomDSLContext context = getDSLContext()) {
            return (List<T>) context.selectFrom(((TableRecord<?>) record).getTable())
                    .where(conditions)
                    .fetch();
        } catch (Exception e) {
            LOGGER.error("Error on find by condition cause by {}", e.getMessage());
            throw new DataException.QueryException("Error on find by condition " + e.getMessage());
        }
    }

    @Override
    public <T extends Record, R> List<R> findIntoByCondition(T record, Class<R> clazz, Condition... conditions) throws DataException.QueryException {
        try (CustomDSLContext context = getDSLContext()) {
            return context.selectFrom(((TableRecord<?>) record).getTable())
                    .where(conditions)
                    .fetchInto(clazz);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("Not found record by condition");
            throw new DataException.NotFoundException("Not found find record by condition");
        } catch (Exception e) {
            LOGGER.error("Error on find by condition cause by {}", e.getMessage());
            throw new DataException.QueryException("Error on find by condition " + e.getMessage());
        }

    }

    @Override
    public int update(Record record, Condition... conditions) throws DataException.ExecuteException {
        try (CustomDSLContext context = getDSLContext()) {
            return context.update(((TableRecord<?>) record).getTable())
                    .set(record)
                    .where(conditions)
                    .execute();
        } catch (Exception e) {
            LOGGER.error("Failed to update record: {}", e.getMessage());
            throw new DataException.ExecuteException("Failed to update record " + e.getMessage());
        }
    }

    @Override
    public int update(Record record) throws DataException.ExecuteException {
        try (CustomDSLContext context = getDSLContext()) {
            Table table = ((TableRecord<?>) record).getTable();
            return context.update(table)
                    .set(record)
                    .where(equalKey(table.getPrimaryKey().getFields(), record))
                    .execute();
        } catch (Exception e) {
            LOGGER.error("Failed to update record: {}", e.getMessage());
            throw new DataException.ExecuteException("Failed to update record " + e.getMessage());
        }
    }

    @Override
    public int updateNotNull(Record record) throws DataException.ExecuteException {
        try (CustomDSLContext context = getDSLContext()) {
            Table table = ((TableRecord<?>) record).getTable();
            for (Field f : record.fields()) {
                if (record.getValue(f) == null) {
                    record.changed(f, false);
                } else {
                    record.changed(f, true);
                }
            }
            return context.update(table)
                    .set(record)
                    .where(equalKey(table.getPrimaryKey().getFields(), record))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Failed to update record: {}", e.getMessage());
            throw new DataException.ExecuteException("Failed to update record " + e.getMessage());
        }
    }

    @Override
    public int updateNotNull(Record record, Condition... conditions) throws DataException.ExecuteException {
        try (CustomDSLContext context = getDSLContext()) {
            for (Field f : record.fields()) {
                if (record.getValue(f) == null) {
                    record.changed(f, false);
                } else {
                    record.changed(f, true);
                }
            }
            return context.update(((TableRecord<?>) record).getTable())
                    .set(record)
                    .where(conditions)
                    .execute();
        } catch (Exception e) {
            LOGGER.error("Failed to update record: {}", e.getMessage());
            throw new DataException.ExecuteException("Failed to update record " + e.getMessage());
        }
    }

    @Override
    public int update(Table table, Map<Field, Object> params, Condition... conditions) {
        try (CustomDSLContext context = getDSLContext()) {
            UpdateQuery query = context.updateQuery(table);
            for (Map.Entry<Field, Object> item : params.entrySet()) {
                query.addValue(item.getKey(), item.getValue());
            }
            query.addConditions(conditions);
            return query.execute();
        } catch (Exception e) {
            LOGGER.error("Failed to update record: {}", e.getMessage());
            throw new DataException.ExecuteException("Failed to update record " + e.getMessage());
        }
    }

    @Override
    public int count(Table table) throws DataException.QueryException {
        try (CustomDSLContext context = getDSLContext()) {
            return context.fetchCount(table);
        } catch (Exception e) {
            LOGGER.error("failed to count number of records: " + e.getMessage(), e);
            throw new DataException.QueryException("failed to count number of records: " + e.getMessage(), e);
        }
    }

    @Override
    public int count(SelectSelectStep select, Table table, Condition condition) throws DataException.QueryException {
        try (CustomDSLContext context = getDSLContext()) {
            return context.fetchCount(select.from(table).where(condition));
        } catch (Exception e) {
            LOGGER.error("failed to count number of records: " + e.getMessage(), e);
            throw new DataException.QueryException("failed to count number of records: " + e.getMessage(), e);
        }
    }

    @Override
    public int count(SelectSelectStep select, Table table, Condition condition, SelectSelectStep select2, Table table2, Condition condition2) throws DataException.QueryException {
        try (CustomDSLContext context = getDSLContext()) {
            return context.fetchCount(select.from(table).where(condition).unionAll(select2.from(table2).where(condition2)));
        } catch (Exception e) {
            LOGGER.error("failed to count number of records: " + e.getMessage(), e);
            throw new DataException.QueryException("failed to count number of records: " + e.getMessage(), e);
        }
    }

    @Override
    public <R extends Record> R fetchOneNotNull(Table<R> table, Condition condition) {
        try (CustomDSLContext context = getDSLContext()) {
            return context.fetch(table, condition).get(0);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("Not found record: {}", e.getMessage());
            throw new DataException.NotFoundException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error when fetch one not null record: {}", e.getMessage());
            throw new DataException.QueryException(e.getMessage());
        }
    }

    @Override
    public <R extends Record> R fetchOneCanNull(Table<R> table, Condition condition) {
        try (CustomDSLContext context = getDSLContext()) {
            return context.fetchOne(table, condition);
        } catch (Exception e) {
            LOGGER.error("Error when fetch one can null record: {}", e.getMessage());
            throw new DataException.QueryException(e.getMessage());
        }
    }

    @Override
    public <T> T fetchOneNotNull(Table<?> table, Condition condition, Class<? extends T> type) {
        try (CustomDSLContext context = getDSLContext()) {
            return context.selectFrom(table)
                    .where(condition)
                    .fetchInto(type)
                    .get(0);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("Not found record: {}", e.getMessage());
            throw new DataException.NotFoundException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error when fetch one not null record: {}", e.getMessage());
            throw new DataException.QueryException(e.getMessage());
        }
    }

    @Override
    public <T> T fetchOneNotNull(Table<?> table, Condition condition, Class<? extends T> type, Field<?>[] fields) {
        try (CustomDSLContext context = getDSLContext()) {
            return context
                    .select(fields)
                    .from(table)
                    .where(condition)
                    .fetchInto(type)
                    .get(0);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("Not found record: {}", e.getMessage());
            throw new DataException.NotFoundException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error when fetch one not null record: {}", e.getMessage());
            throw new DataException.QueryException(e.getMessage());
        }
    }

    @Override
    public boolean fetchExists(Table<?> table, Condition condition) {
        try (CustomDSLContext context = getDSLContext()) {
            return context.fetchExists(table, condition);
        } catch (Exception e) {
            LOGGER.error("Error when check exists record: {}", e.getMessage());
            throw new DataException.QueryException(e.getMessage());
        }
    }

    @Override
    public <R extends Record> List<R> fetch(Table<R> table, Condition condition) {
        try (CustomDSLContext context = getDSLContext()) {
            return context.fetch(table, condition);
        } catch (Exception e) {
            LOGGER.error("Error when fetch record: {}", e.getMessage());
            throw new DataException.QueryException(e.getMessage());
        }
    }

    @Override
    public <T> QueryResponse<T> fetch(QueryRequest request, Table<?> table, Condition condition, Class<? extends T> type) {
        try (CustomDSLContext context = getDSLContext()) {
            condition = condition
                    .and(getSearchCondition(request.getSearch(), request.getSearchValue()))
                    .and(getFilterCondition(request.getFilter(), request.getFilterValue()));
            return QueryResponse.<T>builder()
                    .list(context.selectFrom(table)
                            .where(condition)
                            .orderBy(getSortCondition(request.getOrder(), request.getBy()))
                            .limit(request.getLimit())
                            .offset(request.getFrom())
                            .fetchInto(type))
                    .total(context.fetchCount(table, condition))
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error when fetch records: ", e);
            throw new DataException.QueryException(e.getMessage());
        }
    }

    @Override
    public <T> QueryResponse<T> fetch(QueryRequest request, Table<?> table, Condition condition, Class<? extends T> type, Field<?>[] fields) {
        try (CustomDSLContext context = getDSLContext()) {
            condition = condition
                    .and(getSearchCondition(request.getSearch(), request.getSearchValue()))
                    .and(getFilterCondition(request.getFilter(), request.getFilterValue()));
            return QueryResponse.<T>builder()
                    .list(context.select(fields)
                            .from(table)
                            .where(condition)
                            .orderBy(getSortCondition(request.getOrder(), request.getBy()))
                            .limit(request.getLimit())
                            .offset(request.getFrom())
                            .fetchInto(type))
                    .total(context.fetchCount(table, condition))
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error when fetch records: {}", e.getMessage());
            throw new DataException.QueryException(e.getMessage());
        }
    }

    @Override
    public void batchTransaction(CustomDSLContext context, Collection<Query> queries) {
        context.transaction(configuration -> {
            LOGGER.debug("Execute batch transaction");
            int[] results = context.batch(queries).execute();
            // Rollback if any result is less than 0
            if (Arrays.stream(results).anyMatch(result -> result <= 0)) {
                throw new DataException.ExecuteException("Error on execute batch transaction");
            }
        });
    }

    @Override
    public void batch(CustomDSLContext context, Collection<Query> queries) {
        LOGGER.debug("Execute batch");
        context.batch(queries).execute();
    }

    // -------------------------------------------------------------------------
    // XXX Private
    // -------------------------------------------------------------------------

    protected Condition getSearchCondition(String searchBy, String searchValue) {
        if (States.isNullOrEmpty(searchBy) || States.isNullOrEmpty(searchValue)) {
            return DSL.noCondition();
        }

        String[] searchInput = searchBy.split(Pattern.quote("."));
        Field<Object> searchField = DSL.field(DSL.name(searchInput[0], searchInput[1]));
        return searchField.contains(searchValue);
    }

    protected Condition getFilterCondition(String filterBy, String filterValue) {
        if (States.isNullOrEmpty(filterBy) || States.isNullOrEmpty(filterValue)) {
            return DSL.noCondition();
        }

        String[] filterInput = filterBy.split(Pattern.quote("."));
        Field<Object> filterField = DSL.field(DSL.name(filterInput[0], filterInput[1]));
        return filterField.eq(filterValue);
    }

    protected SortField<?> getSortCondition(String sortBy, String sortType) {
        if (States.isNullOrEmpty(sortBy) || States.isNullOrEmpty(sortType)) {
            return null;
        }

        return "desc".equals(sortType)
                ? DSL.field(sortBy, String.class).desc()
                : DSL.field(sortBy, String.class).asc();
    }

    private Table<?> getTable(Record record) {
        return ((TableRecord<?>) record).getTable();
    }

    private Condition equalKey(Table<?> table, Record record) {
        Condition condition = DSL.trueCondition();
        if (States.isNull(table.getPrimaryKey())) {
            return condition;
        }

        for (TableField field : table.getPrimaryKey().getFields()) {
            condition = condition.and(field.eq(field.getDataType().convert(record.getValue(field.getName()))));
        }
        return condition;
    }
}
