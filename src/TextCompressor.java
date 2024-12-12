/******************************************************************************
 *  Compilation:  javac TextCompressor.java
 *  Execution:    java TextCompressor - < input.txt   (compress)
 *  Execution:    java TextCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   abra.txt
 *                jabberwocky.txt
 *                shakespeare.txt
 *                virus.txt
 *
 *  % java DumpBinary 0 < abra.txt
 *  136 bits
 *
 *  % java TextCompressor - < abra.txt | java DumpBinary 0
 *  104 bits    (when using 8-bit codes)
 *
 *  % java DumpBinary 0 < alice.txt
 *  1104064 bits
 *  % java TextCompressor - < alice.txt | java DumpBinary 0
 *  480760 bits
 *  = 43.54% compression ratio!
 ******************************************************************************/

import java.util.HashMap;

/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, Diego Villegas
 */
public class TextCompressor {
    // A specific code assigned to signal end of file
    final static int EOFCODE = 256;
    // How many bits are codes are going to be
    final static int LENGTHOFCODES = 12;
    // Max possible codes is 4096 because we have 12 bit codes so 2^12 total codes
    final static int MAXPOSSIBLECODES = 4096;
    private static void compress() {
        String s = BinaryStdIn.readString();
        int index = 0;
        String prefix = "";
        int customCode = 257;
        // TST to keep all of our prefixes/strings
        TST tst = new TST();
        // Add all the extended ASCII 256 letters to the TST
        for(int i = 0; i < 256; i++){
            char ASCIIChar = (char) i;
            prefix += ASCIIChar;
            tst.insert(prefix, i);
            prefix = "";
        }
        while(index < s.length()){
            prefix = tst.getLongestPrefix(s, index);
            // Write out the code assigned to the prefix
            BinaryStdOut.write(tst.lookup(prefix), LENGTHOFCODES);
            // If possible look ahead onto the next char
            if(index + prefix.length() < s.length() && customCode < MAXPOSSIBLECODES){
                // Creates new prefix with new code by adding next char to prefix
                tst.insert(prefix + s.charAt(index + prefix.length()), customCode);
                customCode++;
            }
            index += prefix.length();
        }
        // Add EOF as 256 to signal the end for expand function
        BinaryStdOut.write(EOFCODE, LENGTHOFCODES);
        BinaryStdOut.close();
    }

    private static void expand() {
        String[] codeToString = new String[MAXPOSSIBLECODES];
        // Add all 256 extended ascii characters to the map
        String symbol = "";
        for(int i = 0; i < 256; i++){
            symbol += (char) i;
            codeToString[i] = symbol;
            symbol = "";
        }
        int customCode = 257;
        int lookAheadCode = BinaryStdIn.readInt(LENGTHOFCODES);
        int currentCode = 0;
        while(lookAheadCode != EOFCODE){
            currentCode = lookAheadCode;
            BinaryStdOut.write(codeToString[currentCode]);
            lookAheadCode = BinaryStdIn.readInt(LENGTHOFCODES);
            // Edge case where we don't know what the next code is
            if(codeToString[lookAheadCode] == null){
                // Mystery lookahead String is actually the current prefix + the first char prefix
                codeToString[lookAheadCode] = codeToString[currentCode] + codeToString[currentCode].charAt(0);
            }
            else{
                // Creates lookahead string and adds it to map
                if(customCode < MAXPOSSIBLECODES){
                    codeToString[customCode] = codeToString[currentCode] + codeToString[lookAheadCode].charAt(0);
                }
            }
            customCode++;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
