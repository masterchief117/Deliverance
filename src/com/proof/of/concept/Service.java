package com.proof.of.concept;
import java.io.File;

public class Service {

	private static Service service;
	private Dao dao;

	private static final String PERIOD = ".";

	/**
	 * Singleton
	 * 
	 * @return
	 */
	public static Service getInstance() {

		if (service == null) {

			service = new Service();
		}
		return service;
	}

	private Service() {
	}

	/**
	 * Grab the file from the location. Insert into MySQL
	 * 
	 * @param fileLocation
	 * @return
	 */
	public boolean insertFileIntoMySQL(String fileLocation) {

		boolean worked;
		dao = Dao.getInstance();
		File file = null;
		try {
			file = new File(fileLocation);

			worked = dao.insertFileIntoMySQL(file);
		}

		catch (Exception e) {
			worked = false;
		}

		return worked;
	}

	/**
	 * Shove the file from MySQL to MongoDB
	 * 
	 * @return
	 */
	public Object pushToMongDB() {

		return dao.pushToMongoDB();
	}

	/**
	 * Retrieve the dang image/Save its!.
	 * 
	 * @param id
	 *            _id from MongoDB
	 * @param fileLocation
	 *            location the user inputed for file. Used to get the file
	 *            extension
	 * @return
	 */
	public boolean retrieveImage(Object id, String fileLocation) {
		String fileExtension = fileLocation.trim();

		int startOfFileExtension = fileExtension.lastIndexOf(PERIOD);
		// in case there is no extension.
		if (startOfFileExtension == -1) {
			startOfFileExtension = fileExtension.length();
		}
		return dao.saveImageToDisk(id,
				fileExtension.substring(startOfFileExtension));
	}
}
