package com.lin.bulter.business.autogenerator.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import java.io.Closeable;
import java.io.IOException;

public class MysqlManager implements Closeable {
    private static final String DB_DRIVER_NAME = "spring.datasource.driver-class-name";
    private static final String DB_URL = "spring.datasource.url";
    private static final String DB_USERNAME = "spring.datasource.username";
    private static final String DB_PASSWORD = "spring.datasource.password";

    /**
     * Singleton
     */
    private static final MysqlManager INSTANCE = new MysqlManager();

    /**
     * database configuration
     */
    private Configuration configuration = null;
    /**
     * database source
     */
    private HikariDataSource dataSource = null;

    /**
     * get DSL context
     *
     * @return DSLContext
     */
    public DSLContext getContext() {
        return DSL.using(this.configuration);
    }

    /**
     * Constructor
     */
    private MysqlManager() {
        this.dataSource = new HikariDataSource();

        this.dataSource.setDriverClassName(DB_DRIVER_NAME);
        this.dataSource.setJdbcUrl(DB_URL);
        this.dataSource.setUsername(DB_USERNAME);
        this.dataSource.setPassword(DB_PASSWORD);

        // 设置连接池的最大/最小 size
        this.dataSource.addDataSourceProperty("maxConnectionsPerPartition", "5");
        this.dataSource.addDataSourceProperty("minConnectionsPerPartition", "1");
        this.dataSource.addDataSourceProperty("idleConnectionTestPeriodInMinutes", "10");
        this.dataSource.addDataSourceProperty("maxConnectionAgeInSeconds", "3600");
        this.dataSource.addDataSourceProperty("idleMaxAgeInMinutes", "300");
        this.dataSource.addDataSourceProperty("cachePrepStmts", "true");
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource.addDataSourceProperty("connectionTimeout", "3000");

        TransactionAwareDataSourceProxy dataSourceProxy = new TransactionAwareDataSourceProxy(this.dataSource);
        DataSourceTransactionManager dataSourceTxMgr = new DataSourceTransactionManager(this.dataSource);
        this.configuration = new DefaultConfiguration().set(new DataSourceConnectionProvider(dataSourceProxy))
                .set(new SpringTransactionProvider(dataSourceTxMgr)).set(SQLDialect.MYSQL);
    }

    /**
     * Singleton
     *
     * @return instance
     */
    public static MysqlManager getInstance() {
        return INSTANCE;
    }

    /**
     * transaction
     *做事物处理时用
     * @param runnable
     */
    public static void transaction(Runnable runnable) {
        DSLContext context = MysqlManager.getInstance().getContext();
        context.transaction(config -> runnable.run());
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * <p> As noted in {@link AutoCloseable#close()}, cases where the
     * close may fail require careful attention. It is strongly advised
     * to relinquish the underlying resources and to internally
     * <em>mark</em> the {@code Closeable} as closed, prior to throwing
     * the {@code IOException}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            this.dataSource.close();
            this.dataSource = null;
        }

        this.configuration = null;
    }
}
