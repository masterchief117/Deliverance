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

	private Connection connection;
	private Statement statement;
	private ResultSet result;

	private MongoClient mongoClient;
	private DB mongoDb;
	private GridFS gridFs;
	private GridFSInputFile gridFsInputFile;

	/**
	 * Set the driver up!
	 * 
	 * @param driverType
	 *            driver to use EXAMPLE (new OracleDriver())
	 */
	public Dao(Object driverType) {
		setDriver(driverType);
	}

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
	 * Call close before exiting out of the Dao layer! Do not leave Connection
	 * open!
	 */
	public void close() {
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

	private void setDriver(Object o) {
		if (o instanceof OracleDriver) {
			try {
				DriverManager.registerDriver((OracleDriver) o);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

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
