import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBUtil {
	private static final String DB_USERNAME="db.username";
	private static final String DB_PASSWORD="db.password";
	private static final String DB_URL ="db.url";
	private static final String DB_DRIVER_CLASS="driver.class.name";
	
	private static Properties properties = null;
	private static HikariDataSource dataSource;
	static{
		try {

			properties = new Properties();
			properties.load(new FileInputStream("src/database.properties"));
			dataSource = new HikariDataSource();
			dataSource.setDriverClassName(properties.getProperty(DB_DRIVER_CLASS));
			dataSource.setJdbcUrl(properties.getProperty(DB_URL));
			dataSource.setUsername(properties.getProperty(DB_USERNAME));
			dataSource.setPassword(properties.getProperty(DB_PASSWORD));
			dataSource.setMinimumIdle(100);
			dataSource.setMaximumPoolSize(9000);//The maximum number of connections, idle or busy, that can be present in the pool.
			dataSource.setAutoCommit(false);
			dataSource.setLoginTimeout(3);
			
			HikariConfig config = new HikariConfig();
			config.setMinimumIdle(100);
			config.setMaximumPoolSize(9000);
			config.setInitializationFailFast(true);
			config.setConnectionTestQuery("VALUES 1");
			config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
			config.addDataSourceProperty("serverName", "localhost");
			config.addDataSourceProperty("port", "3306");
			config.addDataSourceProperty("databaseName", "lottery");
			config.addDataSourceProperty("user", DB_USERNAME);
			config.addDataSourceProperty("password", DB_PASSWORD);
            
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static DataSource getDataSource(){
		return dataSource;
	}
}