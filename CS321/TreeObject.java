import java.io.Serializable;

public class TreeObject implements Serializable {
	 
	private int freq;
	private long key;
	private static int seqLength = 0;
	
	/**
	 * Creates a tree object with a given frequency and key
	 * Can be used when a duplicate is found.
	 */
	public TreeObject(long key, int freq) {
		this.freq = freq;
		this.key = key;
	}
	
	/**
	 * Creates a tree object with only the key
	 * Can be used when no duplicates is found
	 * @param key
	 */
	public TreeObject(long key) {
		this.key = key;
		freq = 1;
	}
	
	/**
	 * Increases the freq count 
	 */
	public void increaseFreq() {
		freq++;
	}
	
	/**
	 * Returns the freq of the object
	 */
	public int getFreq() {
		return freq;
	}
	/**
	 * Returns the key of 
	 * @return
	 */
	public long getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		String str = convertKey() + ": " + freq;
		return str;
	}
	
	public void setSeqLen(int n) {
		seqLength = n;
	}
	
	private String convertKey() {
		String subString = "";

		for (int i = 1; i <= seqLength; i++) {
			long geneBit = (key & (3L << (seqLength - i) * 2));
			geneBit = geneBit >> (seqLength - i) * 2;

			if (geneBit == 0) { //00
				subString += "a";
			} else if (geneBit == 1) { //01
				subString += "c";
			} else if (geneBit == 2) { //10
				subString += "g";
			} else if (geneBit == 3) { //11
				subString += "t";
			}
		}

		return subString;
	}
}
