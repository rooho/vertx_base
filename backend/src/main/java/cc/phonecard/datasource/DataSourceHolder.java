package cc.phonecard.datasource;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.util.DriverDataSource;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;

public class DataSourceHolder {

  private static HikariDataSource dataSource;

  private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceHolder.class);

  public static void init(JsonObject dataSourceConfig) {
    //校验配置
//    validateConfig(dataSourceConfig);
    String dbFile = dataSourceConfig.getString("dbFile");
    File databaseFile = new File(dbFile);
    if (!databaseFile.exists()) {
      try {
        databaseFile.createNewFile();
      } catch (IOException exception) {
        LOGGER.info("Failed to created SQLite database.  Error: "
          + exception.getMessage());
      }
    }
    DataSource sqliteDataSource = new DriverDataSource("jdbc:sqlite:" + databaseFile.getAbsolutePath(),
      "org.sqlite.JDBC", new Properties(), null, null);

    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setDataSourceClassName(dataSourceConfig.getString("dataSourceClassName"));
    hikariDataSource.setPoolName(dataSourceConfig.getString("poolName"));
    hikariDataSource.setDataSource(sqliteDataSource);
    if (null != dataSourceConfig.getValue("maximumPoolSize")) {
      hikariDataSource.setMaximumPoolSize(dataSourceConfig.getInteger("maximumPoolSize"));
    }
    dataSource = hikariDataSource;
  }

  public static HikariDataSource getDataSource() {
    return dataSource;
  }

  private static void validateConfig(JsonObject dataSourceConfig) {
    String driverClassName = dataSourceConfig.getString("driverClassName");
    if (StringUtils.isBlank(driverClassName)) {
      throw new NullPointerException("dataSource.driverClassName is empty");
    }
    String jdbcUrl = dataSourceConfig.getString("jdbcUrl");
    if (StringUtils.isBlank(jdbcUrl)) {
      throw new NullPointerException("dataSource.jdbcUrl is empty");
    }
    String username = dataSourceConfig.getString("username");
    if (StringUtils.isBlank(username)) {
      throw new NullPointerException("dataSource.username is empty");
    }
    String password = dataSourceConfig.getString("password");
    if (StringUtils.isBlank(password)) {
      throw new NullPointerException("dataSource.password is empty");
    }
  }
}
