package com.proof.of.concept;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import oracle.jdbc.driver.OracleDriver;
import oracle.sql.STRUCT;

public class OEDao {

	private static final String ORACLE_USERNAME = "oe";
	private static final String ORACLE_PASSWORD = "oe";
	private static final String URL_TO_ORACLE_DB = "jdbc:oracle:thin:@192.168.0.44:1521:MongoDemo";

	private ResultSet result;
	private Statement statement;
	private Connection connection;

	private MongoClient client;

	/**
	 * Does not works Look at TODO
	 * Then refactor, refactor, refactor!!
	 */
	public void getAndRip() {
		try {
			client = new MongoClient("192.168.0.44");
			DB db = client.getDB("Stachers");
			// WILL STORE Binary version. :-p
			GridFS gridFs = new GridFS(db);
			DBCollection collection = db.getCollection("OE");
			DriverManager.registerDriver(new OracleDriver());
			connection = DriverManager.getConnection(URL_TO_ORACLE_DB,
					ORACLE_USERNAME, ORACLE_PASSWORD);
			statement = connection.createStatement();
			result = statement
					.executeQuery("SELECT oi.PRODUCT_ID, o.CUSTOMER_ID, o.PROMOTION_ID, c.CUST_GEO_LOCATION FROM ORDER_ITEMS oi INNER JOIN ORDERS o ON o.order_id = oi.order_id INNER JOIN CUSTOMERS c ON o.customer_id = c.customer_id");
			while (result.next()) {
				STRUCT object = (STRUCT) result.getObject("CUST_GEO_LOCATION");
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				ObjectOutputStream output = new ObjectOutputStream(byteArray);
				//TODO Change so it works. Does not want to serialize.
				output.writeObject(object);
				GridFSInputFile input = gridFs.createFile(byteArray
						.toByteArray());
				// HOPEFULLY this will insert the GridFSInputFile
				// Not sure if the BasicDBObject will store said file.
				BasicDBObject dbObject = new BasicDBObject("ProductId",
						result.getInt("PRODUCT_ID"))
						.append("CustomerId", result.getInt("Customer_ID"))
						.append("PromotionId", result.getInt("Promotion_ID"))
						.append("GeoLocation", input);
				collection.insert(dbObject);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public OEDao() {
	}

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
}
