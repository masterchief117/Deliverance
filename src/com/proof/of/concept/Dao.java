package com.proof.of.concept;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import oracle.jdbc.driver.OracleDriver;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Some objects you'll need for all Dao access!
 * 
 * @author masterchief117
 * 
 */
public abstract class Dao {

	// Used for connection to a SQL db
	// make sure to close if you open up a connection
	private Connection connection;
	// Used for creating statements to query for from the db
	// might want to switch to prepareStatement to prevent SQL injection
	// TODO
	private Statement statement;
	// results from a Query are stored in a ResultSet
	private ResultSet result;

	// Creates a conne
	private MongoClient mongoClient;
	private DB mongoDb;
	private GridFS gridFs;
	private GridFSInputFile gridFsInputFile;

	/**
	 * Set the driver and connection up!
	 * 
	 * @param driverType
	 *            driver to use EXAMPLE ( new OracleDriver())
	 * @param connection
	 *            string of the connection to the DB
	 * @param username
	 *            username for DB
	 * @param password
	 *            password for DB
	 */
	public Dao(Object driverType, String connection, String username,
			String password) {
		setDriver(driverType);
		setConnection(connection, username, password);
	}

	/**
	 * Any time mongoClient is opened. CLOSE BEFORE leaving Dao layer!
	 */
	public void closeMongo() {
		try {
			if (mongoClient != null) {
				mongoClient.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Call close before exiting out of the Dao layer! Do not leave Connection
	 * open!
	 */
	public void closeSQL() {
		try {
			if (result != null) {
				result.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the driver to DriverManager
	 * 
	 * @param o
	 */
	private void setDriver(Object o) {
		if (o instanceof OracleDriver) {
			try {
				DriverManager.registerDriver((OracleDriver) o);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set the connection string to DriverManager, to push to this.connection
	 * 
	 * @param connection
	 *            url
	 * @param username
	 *            un
	 * @param password
	 *            pw
	 */
	private void setConnection(String connection, String username,
			String password) {
		try {
			this.connection = DriverManager.getConnection(connection, username,
					password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return the statement
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 * @param statement
	 *            the statement to set
	 */
	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	/**
	 * @return the result
	 */
	public ResultSet getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ResultSet result) {
		this.result = result;
	}

	/**
	 * @return the mongoClient
	 */
	public MongoClient getMongoClient() {
		return mongoClient;
	}

	/**
	 * @param mongoClient
	 *            the mongoClient to set
	 */
	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	/**
	 * @return the mongoDb
	 */
	public DB getMongoDb() {
		return mongoDb;
	}

	/**
	 * @param mongoDb
	 *            the mongoDb to set
	 */
	public void setMongoDb(DB mongoDb) {
		this.mongoDb = mongoDb;
	}

	/**
	 * @return the gridFs
	 */
	public GridFS getGridFs() {
		return gridFs;
	}

	/**
	 * @param gridFs
	 *            the gridFs to set
	 */
	public void setGridFs(GridFS gridFs) {
		this.gridFs = gridFs;
	}

	/**
	 * @return the gridFsInputFile
	 */
	public GridFSInputFile getGridFsInputFile() {
		return gridFsInputFile;
	}

	/**
	 * @param gridFsInputFile
	 *            the gridFsInputFile to set
	 */
	public void setGridFsInputFile(GridFSInputFile gridFsInputFile) {
		this.gridFsInputFile = gridFsInputFile;
	}

}
