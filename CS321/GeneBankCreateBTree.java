iimport java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * @GeneBankCreateBTree:creates a BTree from a given GeneBank file.
 * 
 * @authors Michael Kinsy, Oscar Filson,    ,   ,
 *
 */


public class GeneBankCreateBTree {

	public static final long CODE_A = 0b00L;
	public static final long CODE_T = 0b11L;
	public static final long CODE_C = 0b01L;
	public static final long CODE_G = 0b10L;

	public static final int MAX_SEQUENCE_LENGTH = 31;
	public static final int MAX_DEBUG_LEVEL = 1;
	
	public static void main(String[] args) throws IOException {

		if (args.length < 4) {
			badUsage();
		}

		// cache
		boolean useCache = false;

		try {
			int c = Integer.parseInt(args[0]);
			if (c == 0) useCache = false;
			else if (c == 1) useCache = true;
			else badUsage();
		} catch (NumberFormatException e) {
			badUsage();
		}

		// degree
		int BTreeDegree = 0;

		try {
			int deg = Integer.parseInt(args[1]);
			if (deg < 0) badUsage();
			else if (deg == 0) BTreeDegree = getOptimalDegree();
			else BTreeDegree = deg;
		} catch (NumberFormatException e) {
			badUsage();
		}
		int sequenceLength = 0;

		// sequence length
		try {
			int len = Integer.parseInt(args[3]);
			if (len < 1 || len > MAX_SEQUENCE_LENGTH) badUsage();
			else sequenceLength = len;
		} catch (NumberFormatException e) {
			badUsage();
		}
		
		// cache and debug level
		int cacheSize = 0;
		int debugLevel = 0;

		if (args.length > 4) {
			if (useCache) {
				try {
					int csize = Integer.parseInt(args[4]);
					if (csize < 1) badUsage();
					else cacheSize = csize;
				} catch (NumberFormatException e) {
					badUsage();
				}
			}
			if (!useCache || args.length > 5) {
				try {
					int dlevel = Integer.parseInt(useCache ? args[5] : args[4]);
					if (dlevel < 0 || dlevel > MAX_DEBUG_LEVEL) badUsage();
					else debugLevel = dlevel;
				} catch (NumberFormatException e) {
					badUsage();
				}
			}
		}

		// genebank file
		File gbk = new File(args[2]);

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(gbk));
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + gbk.getPath());
		}
		String BTreeFile = (gbk + ".btree.data." + sequenceLength + "." + BTreeDegree);
		BTree tree = new BTree(BTreeDegree, BTreeFile, useCache, cacheSize);

		String line = null;
		line = in.readLine().toLowerCase().trim();
		boolean inSequence = false;
		int sequencePosition = 0;
		int charPosition = 0;
		long sequence = 0L;

		while (line != null) { 
			if (inSequence) {
				if (line.startsWith("//")) { 
					inSequence = false;
					sequence = 0;
					sequencePosition = 0;
				} else {
					while (charPosition < line.length()) {
						char c = line.charAt(charPosition++);
						switch (c) {
						case 'a':
							sequence = ((sequence<<2) | CODE_A);
							if (sequencePosition < sequenceLength) sequencePosition++;
							break;
						case 't':
							sequence = ((sequence<<2) | CODE_T);
							if (sequencePosition < sequenceLength) sequencePosition++;
							break;
						case 'c':
							sequence = ((sequence<<2) | CODE_C);
							if (sequencePosition < sequenceLength) sequencePosition++;
							break;
						case 'g':
							sequence = ((sequence<<2) | CODE_G);
							if (sequencePosition < sequenceLength) sequencePosition++;
							break;
						case 'n':
							sequencePosition = 0;
							sequence = 0;
							continue;
						default: //none of the above
							continue;
						}
						if (sequencePosition >= sequenceLength) {
							tree.BTreeInsert(sequence & (~(0xffffffffffffffffL<<(sequenceLength<<1))));
						}
					}
				}
			} else if (line.startsWith("ORIGIN")) {
				inSequence = true;
			}
			line = in.readLine();
			charPosition = 0;
		}

		if (debugLevel > 0) {
			File dumpFile = new File("dump");
			dumpFile.delete();
			dumpFile.createNewFile();
			PrintWriter writer = new PrintWriter(dumpFile);
			tree.inOrderPrintToWriter(tree.getRoot(),writer,sequenceLength);
			writer.close();
		}
		System.out.println("done");
		if (useCache) tree.flushCache();
		in.close();
	}
	

	private static void badUsage() {
		System.err.println("Usage: java GeneBankCreateBTree <cache> <degree> <gbk file> <sequence length> [<cache size>] [<debuglevel>]");
		System.err.println("<cache>: 0/1 (no cache/cache)");
		System.err.println("<degree>: degree of the BTree (0 for default)");
		System.err.println("<gbk file>: GeneBank file");
		System.err.println("<sequence length>: 1-31");
		System.err.println("[<cache size>]: size of cache, optional");
		System.err.println("[<debug level>]: 0/1 (no/yes)");
		System.exit(1);
	}
	public static int getOptimalDegree(){
		double optimum;
		int sizeOfPointer = 4;
		int sizeOfObject = 12;
		int sizeOfMetadata = 5;
		optimum = 4096;
		optimum += sizeOfObject;
		optimum -= sizeOfPointer;
		optimum -= sizeOfMetadata;
		optimum /= (2 * (sizeOfObject + sizeOfPointer));
		return (int) Math.floor(optimum);
	}
}



