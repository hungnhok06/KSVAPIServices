package vn.backend.ksv.repository.base;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultDSLContext;
import vn.backend.ksv.common.LogAdapter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:57 AM
 */
public class CustomDSLContext extends DefaultDSLContext implements AutoCloseable{

    private static final LogAdapter LOGGER = LogAdapter.newInstance(CustomDSLContext.class);

    private Connection connection;
    private SQLDialect dialect;
    private Settings settings;

    public CustomDSLContext(SQLDialect dialect) {
        super(dialect);
    }

    public CustomDSLContext(SQLDialect dialect, Settings settings) {
        super(dialect, settings);
    }

    public CustomDSLContext(Connection connection, SQLDialect dialect) {
        super(connection, dialect);
    }

    public CustomDSLContext(Connection connection, SQLDialect dialect, Settings settings) {
        super(connection, dialect, settings);
        this.connection = connection;
        this.dialect = dialect;
        this.settings = settings;
    }

    public CustomDSLContext(DataSource datasource, SQLDialect dialect) {
        super(datasource, dialect);
    }

    public CustomDSLContext(DataSource datasource, SQLDialect dialect, Settings settings) {
        super(datasource, dialect, settings);
    }

    public CustomDSLContext(ConnectionProvider connectionProvider, SQLDialect dialect) {
        super(connectionProvider, dialect);
    }

    public CustomDSLContext(ConnectionProvider connectionProvider, SQLDialect dialect, Settings settings) {
        super(connectionProvider, dialect, settings);
    }

    public CustomDSLContext(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.debug(e.getMessage());
            }
        }
    }
}
