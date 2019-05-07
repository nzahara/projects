package unccdb.database_connections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;



public abstract class AbstractDataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataSource.class);

	protected DataSource tomcatDatasource;

	private String dataSourcePrefix;

	public AbstractDataSource(String dataSourcePrefix) {
		this.dataSourcePrefix = dataSourcePrefix;
		PoolProperties p = getProperties(null);
		tomcatDatasource = new DataSource();
		tomcatDatasource.setPoolProperties(p);
	}


	protected PoolProperties getProperties(Environment environment) {
		if (environment == null) {
			environment =  SpringApplicationContext.getBean(Environment.class);
		}
		PoolProperties prop = new PoolProperties();
		StringBuilder url = new StringBuilder();
		url.append(environment.getProperty(dataSourcePrefix + ".database.jdbcUrlPrefix"));
		url.append(environment.getProperty(dataSourcePrefix + ".database.host"));
		url.append(":" + environment.getProperty(dataSourcePrefix + ".database.port"));
		url.append("/" + environment.getProperty(dataSourcePrefix + ".database.defaultSchema"));
		//url.append("?" + environment.getProperty(dataSourcePrefix + ".database.options"));
		url.append("?allowPublicKeyRetrieval=true&&useSSL=false");
		prop.setUrl(url.toString());

		LOGGER.info("Connection URL: {}", url.toString());

		prop.setDriverClassName(environment.getProperty(dataSourcePrefix + ".database.dbDriver"));
		prop.setUsername(environment.getProperty(dataSourcePrefix + ".database.username"));
		prop.setPassword(environment.getProperty(dataSourcePrefix + ".database.password"));
		prop.setJmxEnabled(false);
		prop.setTestWhileIdle(false);
		prop.setTestOnBorrow(true);
		prop.setValidationQuery("SELECT 1");
		prop.setTestOnReturn(false);
		prop.setValidationInterval(30000);
		prop.setTimeBetweenEvictionRunsMillis(5000);
	//	int numberOfInstance = Integer.parseInt(environment.getProperty("app.number.instance"));
		int maxConnections = Integer.valueOf(environment.getProperty(dataSourcePrefix + ".database.maxPoolSize"));
		prop.setMaxActive(maxConnections);
		prop.setInitialSize(Integer.valueOf(environment.getProperty(dataSourcePrefix + ".database.initPoolSize")));
		prop.setMaxWait(30000);
		prop.setRemoveAbandonedTimeout(60);
		prop.setMinEvictableIdleTimeMillis(10000);
		prop.setMinIdle(Integer.valueOf(environment.getProperty(dataSourcePrefix + ".database.minPoolSize")));
		prop.setMaxIdle(maxConnections);
		prop.setLogAbandoned(true);
		prop.setRemoveAbandoned(true);
		prop.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
		                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;"
		                + "org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer");
		return prop;
	}

	/**
	 * To close resources.
	 * 
	 * @param resultSet
	 *            the result set
	 * @param statement
	 *            the statement
	 * @param connection
	 *            the connection
	 */
	public static void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("SQLException occured while closing connection:", e);
				}

			}
			resultSet = null;
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("SQLException occured while closing statement:", e);
				}

			}
			statement = null;
		}
		if (connection != null) {
			setAutoCommitPropetry(true, connection);
			try {
				connection.close();
			} catch (SQLException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("SQLException occured while closing resultset:", e);
				}

			}
			connection = null;
		}
	}
	
	public static void closeResources(ResultSet resultSet, Statement statement, Connection connection) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("SQLException occured while closing connection:", e);
				}

			}
			resultSet = null;
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("SQLException occured while closing statement:", e);
				}

			}
			statement = null;
		}
		if (connection != null) {
			setAutoCommitPropetry(true, connection);
			try {
				connection.close();
			} catch (SQLException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("SQLException occured while closing resultset:", e);
				}

			}
			connection = null;
		}
	}

	public static void setAutoCommitPropetry(boolean autoCommit, Connection con) {
		try {
			if (con == null || con.isClosed()) {
				return;
			}
			if (autoCommit == con.getAutoCommit()) {
				return;
			}
			con.setAutoCommit(autoCommit);
		} catch (Exception e) {
			LOGGER.info(con.toString());
			LOGGER.error("Exception occured while resetting property for autoCommit:", e);
		}
	}

	/**
	 * Rollbcak a given database connection
	 * 
	 * @param con
	 */
	public static void rollbackConnection(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				LOGGER.error("Failed to rollback connection");
			}
		}
	}

	public Connection getConnection() throws SQLException {
		return tomcatDatasource.getConnection();
	}

	public DataSource getDatasource() throws SQLException {
		return tomcatDatasource;
	}
}
