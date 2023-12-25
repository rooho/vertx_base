package cc.phonecard.dao;

import cc.phonecard.datasource.DataSourceHolder;
import java.util.Objects;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;

public class DaoConfigurationHolder {

    private static Configuration daoConfiguration;

    public static void init() {
        ConnectionProvider connectionProvider = new DataSourceConnectionProvider(DataSourceHolder.getDataSource());
        Configuration configuration = new DefaultConfiguration();
        configuration.set(SQLDialect.SQLITE);
        configuration.set(connectionProvider);
        daoConfiguration = configuration;
    }

    public static Configuration getDaoConfiguration() {
        Objects.requireNonNull(daoConfiguration, "daoConfiguration is not initialized");
        return daoConfiguration;
    }
}
