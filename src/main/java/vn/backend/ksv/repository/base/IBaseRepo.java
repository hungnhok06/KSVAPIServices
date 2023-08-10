package vn.backend.ksv.repository.base;

import org.jooq.*;
import vn.backend.ksv.common.exception.DataException;
import vn.backend.ksv.common.reponse.QueryResponse;
import vn.backend.ksv.common.request.common.QueryRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:57 AM
 */
public interface IBaseRepo {
    /**
     * save a record
     *
     * @param
     */
    int save(Record record) throws DataException.ExecuteException;

    /**
     * save a record if not existed.
     * if record existed just update valuse by primary key
     *
     * @param
     */
    int saveOrUpdate(Record record) throws DataException.ExecuteException;

    /**
     * Find only one record by id.
     *
     * @param record     Record of table
     * @param conditions
     * @param <T>
     * @return Record with condition
     * @throws DataException.MultiValuesException if get multi values
     * @throws DataException.NotFoundException    if not found any record
     * @throws DataException.QueryException       if has error on execute query
     */
    @Deprecated
    <T extends Record> T findOneById(T record, Condition... conditions) throws DataException.MultiValuesException, DataException.NotFoundException, DataException.QueryException;

    /**
     * Find only one object by condition
     *
     * @param record     record of table
     * @param clazz      return type, must be pojo
     * @param conditions condition
     * @return only one object (pojo)
     * @throws DataException.ExecuteException if has error on execute query
     */
    @Deprecated
    <T extends Record, R> R findOneIntoById(T record, Class<R> clazz, Condition... conditions) throws DataException.QueryException;

    /**
     * Find list by condition
     *
     * @param record     record of table
     * @param conditions
     * @param <T>
     * @return List of record
     * @throws DataException.ExecuteException if has error on execute query
     */
    @Deprecated
    <T extends Record> List<T> findByCondition(T record, Condition... conditions) throws DataException.QueryException;

    /**
     * Find list object by condition
     *
     * @param record     record of table
     * @param clazz      return type
     * @param conditions condition
     * @return List of class
     * @throws DataException.ExecuteException if has error on execute query
     */
    @Deprecated
    <T extends Record, R> List<R> findIntoByCondition(T record, Class<R> clazz, Condition... conditions) throws DataException.QueryException;

    /**
     * update a record with many condition
     *
     * @param record
     * @param conditions jooq condition(s)
     */
    @Deprecated
    int update(Record record, Condition... conditions) throws DataException.ExecuteException;

    /**
     * update a record with primary key
     *
     * @param record
     */
    int update(Record record) throws DataException.ExecuteException;

    /**
     * update a record with primary key if properties not null
     *
     * @param record
     */
    int updateNotNull(Record record) throws DataException.ExecuteException;

    /**
     * update a record with many conditions if properties not null
     *
     * @param record
     * @param conditions jooq condition(s)
     */
    @Deprecated
    int updateNotNull(Record record, Condition... conditions) throws DataException.ExecuteException;

    /**
     * update record(s) with many condition with multi values
     */
    int update(Table table, Map<Field, Object> params, Condition... conditions);

    /**
     * Count number of records
     *
     * @param table
     * @return number of records in table
     * @throws DataException.QueryException
     */
    int count(Table table) throws DataException.QueryException;

    int count(SelectSelectStep select, Table table, Condition condition);

    int count(SelectSelectStep select, Table table, Condition condition, SelectSelectStep select2, Table table2, Condition condition2);

    <R extends Record> R fetchOneNotNull(Table<R> table, Condition condition);

    <R extends Record> R fetchOneCanNull(Table<R> table, Condition condition);

    <T> T fetchOneNotNull(Table<?> table, Condition condition, Class<? extends T> type);

    <T> T fetchOneNotNull(Table<?> table, Condition condition, Class<? extends T> type, Field<?>[] fields);

    boolean fetchExists(Table<?> table, Condition condition);

    <R extends Record> List<R> fetch(Table<R> table, Condition condition);

    <T> QueryResponse<T> fetch(QueryRequest request,
                               Table<?> table,
                               Condition condition,
                               Class<? extends T> type);

    <T> QueryResponse<T> fetch(QueryRequest request,
                               Table<?> table,
                               Condition condition,
                               Class<? extends T> type,
                               Field<?>[] fields);

    /**
     * Execute multi queries with transaction
     *
     * @param context custom dsl context
     * @param queries list of query
     */
    void batchTransaction(CustomDSLContext context, Collection<Query> queries);

    /**
     * Execute multiple queries
     *
     * @param context custom dsl context
     * @param queries list of query
     */
    void batch(CustomDSLContext context, Collection<Query> queries);
}
