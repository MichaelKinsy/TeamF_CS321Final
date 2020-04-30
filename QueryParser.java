import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Parses a query file
 *
 */
public class QueryParser {

	private String geneString;

	/**
	 * Completes the parsing of the file and adds the gene queries to a
	 * string
	 * The geneString has queries separated by spaces
	 * @param file the file which needs to be parsed
	 */
	public QueryParser(File file) {
		geneString = "";
		try {
			Scanner scan = new Scanner(file);

			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				geneString += line;
				geneString += " ";
			}
		} catch (FileNotFoundException e) {
			System.out.println("The file, " + file + ", cannot be found.");
		}

	}

	/**
	 * Gets the geneString from parsing the file and coverts the geneString to an array
	 */
	public String[] getGeneStringArray() {
		return geneString.split(" ");
	}

}