package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.Application;
import application.ConfigurationException;


 class Connector {
	public Connection getConnection()
			throws SQLException, ConfigurationException {
		Connection conn = null;
		String url = Application.getDatabaseUrl();
		String dbName = Application.getDatabaseName();
		String driver = Application.getDatabaseDriver();
		String variables = Application.getDatabaseConnectionVariables();
		String connectionUrl = url + dbName + variables;
		String userName = Application.getDatabaseUser();
		String password = Application.getDatabasePassword();

		
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				throw new ConfigurationException(e);
			}

			conn = DriverManager.getConnection(connectionUrl, userName,
					password);
		return conn;
	}

}
