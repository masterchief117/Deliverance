import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.gridfs.*;

public class Dao {

	private static Dao dao;
	private static final String URL_TO_SERVER = "jdbc:mysql://localhost/image_test?"
			+ "user=root&password=root";
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static final String GRAB_LAST_IMAGE = "SELECT image FROM image_test.pictures ORDER BY idpictures DESC LIMIT 1";
	private static final String DIRECTORY_LOCATION = "c:/temp/temp/";
	private MongoClient client;
	private Connection connection;
	private PreparedStatement statement;
	private ResultSet result;

	private Dao() {

	}

	/**
	 * Get instance of Dao
	 * 
	 * @return
	 */
	public static Dao getInstance() {
		if (dao == null) {
			dao = new Dao();
		}

		return dao;
	}

	/**
	 * Insert file into MySQL
	 * 
	 * @param fileToSave
	 * @return
	 */
	public boolean insertFileIntoMySQL(File fileToSave) {
		boolean success;
		try {
			FileInputStream stream = new FileInputStream(fileToSave);
			// loads the MySQL Driver. TODO (Look into this function)
			Class.forName(MYSQL_DRIVER);
			// setup the connection
			createConnection();
			// place file into DB
			prepareInsertFile(fileToSave, stream);
			// executes the save
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			// CLOSE THE DANG CONNECTION!
			success = closeMySQL();
		}
		return success;
	}

	/**
	 * Grab the file from the DB and push into the MongoDB
	 * 
	 * @param fileLocation
	 * @return
	 */
	public Object pushToMongoDB() {
		Object id;
		try {
			// Grabbing the image from the DB using getInputStream()
			InputStream stream = getImageFromSQL();
			// create a new MongoClient
			client = new MongoClient(new ServerAddress("localhost", 27017));
			// create the db (or get it)
			DB db = client.getDB("image_test");
			// create a new GridFS (used for documents of non-BSON type)
			GridFS fs = new GridFS(db);
			// grab the file;
			GridFSInputFile in = fs.createFile(stream);
			// save the file
			in.save();
			// gets the _id of the file ya just saved!
			id = in.getId();
			// close all the connections you made with getImageFromSQL
			closeMySQL();
		}

		catch (Exception e) {
			e.printStackTrace();
			id = 0;
		}
		return id;
	}

	/**
	 * I did not write this to create the directories(in dos from md C:\temp &&
	 * md C:\temp\temp\. Do it manually, change if you got Linux.
	 * 
	 * 
	 * @param id
	 * @return
	 */
	public boolean saveImageToDisk(Object id, String fileExtension) {
		boolean success;
		try {
			// setup client server, will change this as I find best practice
			client = new MongoClient(new ServerAddress("localhost", 27017));
			// create or get DB
			DB db = client.getDB("image_test");
			// create new instance of GridFS, needed for documents (not BSON)
			GridFS fs = new GridFS(db);
			// grab the object to save
			GridFSDBFile save = fs.findOne(new BasicDBObject("_id", id));
			// create the file (this will just assign a random
			// 2342323232354.yourFileExtensionFromBefore
			FileOutputStream out = new FileOutputStream(DIRECTORY_LOCATION
					+ (int) (Math.random() * 1000000) + fileExtension);
			// SAVES THE FILE, called from GriFSDBFile, uses FileOutputStream
			save.writeTo(out);
			// close the FileOutputStream
			out.close();
			success = true;
		} catch (Exception e) {

			e.printStackTrace();
			success = false;
		}
		return success;
	}

	/**
	 * prepares the file to be inserted by a stmnt.execute() commmand
	 * 
	 * @param fileToInsert
	 *            the file you want to insert
	 * @param stream
	 *            the stream that has been opened up
	 * @throws Exception
	 *             throwing exception, top level, what what!
	 */
	private void prepareInsertFile(File fileToInsert, FileInputStream stream)
			throws Exception {
		// setup the connection to grab the latest file (the one you just placed
		// in there!)
		statement = connection
				.prepareStatement("insert into image_test.pictures (image) VALUES ( ? )");
		// and slam it into the stream. Might want to move the execute to after
		// this method. But oh wells.
		statement.setBinaryStream(1, stream, (int) fileToInsert.length());
	}

	/**
	 * Just cuz I use it more than once.
	 * 
	 * @throws Exception
	 *             limiting the try/catch
	 */
	private void createConnection() throws Exception {
		connection = DriverManager.getConnection(URL_TO_SERVER);
	}

	/**
	 * Seperated this out for cleaner code;
	 * 
	 * @return
	 * @throws Exception
	 *             Just to limit try/catch
	 */
	private InputStream getImageFromSQL() throws Exception {
		createConnection();
		statement = connection.prepareStatement(GRAB_LAST_IMAGE);
		ResultSet image = statement.executeQuery();
		image.first();
		return image.getBinaryStream(1);
	}

	/**
	 * Closes the JDBC connection, as well as the ResultSet and Statement. ALL
	 * OPENINGS NEED TO BE CLOSED ON LEAVING DAO
	 * 
	 * @return
	 */
	private boolean closeMySQL() {
		boolean success;
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
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
}
