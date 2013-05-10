import java.util.Scanner;

public class Main {

	/**
	 * Run as a java application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Main main = new Main();
		main.gatherInput(input);
		input.close();
	}

	/**
	 * Input the line of a picture, or whatever. MongoDB will hold it in memory
	 * 
	 * @param input
	 */
	private void gatherInput(Scanner input) {
		Service service = Service.getInstance();
		String userInputOfFileLocation = input.nextLine();
		// returns a boolean if it worked. Will crash on false, do not expect to
		// see it.
		System.out.println("Insert picture into MySQL id = "
				+ service.insertFileIntoMySQL(userInputOfFileLocation));
		// grab the id!
		Object id = service.pushToMongDB();
		// shows the _id of the file inserted
		System.out.println("Insert picture into MongoDB " + id);
		// returns a boolean if it worked. Will crash on false. So don't expect
		// to see it.
		System.out.println("Retrive and save file "
				+ service.retrieveImage(id, userInputOfFileLocation));
	}
}
