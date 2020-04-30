/**
 * Converts the long and string representations of geneString
 * 
 */
public class GeneConverter {

	private long key = 0;

	
	/**
	 * Converts a long subSequence into a String
	 * 
	 * @param subSeq the Long subSequence to be converted
	 * @param seqLength Length of the subSequence
	 * @return a String representation of a long
	 */
	public String convertLongToString(long subSeq, int seqLength) {
		String subString = "";

		for (int i = 1; i <= seqLength; i++) {
			/*
			 * We are working with two bit representations, we compare the sub
			 * sequence at the pairs to get the gene. We use 3 (11) and for matching each pair if a single
             * bit does not match it will return a 0. After comparing,
			 * we will shift back to two bits and find what value we found.
			 */
			long geneBit = (subSeq & (3L << (seqLength - i) * 2));
			/**
			 * Example: 3 shifted 4 times to the left 11 -> 00110000
			 * We compare that to original subSeq
			 * Subsequence:	11100001
			 * Shift:		00110000
			 * & Operation: 00100000
			 * Shift back: 	00000010
			 */
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
    
    /**
	 * Returns the long key created from the String
	 */
	public long getKey() {
		return key;
	}


	/**
	 * Converts geneString to a long by converting the bases into their binary
	 * representation
	 * 
	 * @param subSeq Sequence to be converted into a long
	 * @return Long representation of the string
	 */
	public long convertStringToLong(String subSeq) {

		for (int i = 0; i < subSeq.length(); i++) {
			switch (subSeq.charAt(i)) {
			/**
			 * The switch statement finds the representation for the char, then shifts the
			 * key left two to add the new two bits
			 */
			case 'a': // 00 = 0
				if (i == 0) {
					key = 0;
				} else {
					key = key << 2; // Shift bits left two, leave first two bits open for insertion
				}
				break;
			case 'c': // 01 = 1
				if (i == 0) {
					key = 1;
				} else {
					key = key << 2;
					key += 1; // Adding 01 to end
				}
				break;
			case 'g': // 10 = 2
				if (i == 0) {
					key = 2;
				} else {
					key = key << 2;
					key += 2; // Adding 10 to end
				}
				break;
			case 't': // 11 = 3
				if (i == 0) {
					key = 3;
				} else {
					key = key << 2;
					key += 3; // Adding 11 to end
				}
				break;
			}
		}
		return key;
	}
}