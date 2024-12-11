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

    private static void compress() {
        String s = BinaryStdIn.readString();
        int index = 0;
        String prefix = "";
        int EOFCode = 256;
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
        while (index < s.length()){
            prefix = tst.getLongestPrefix(s, index);
            // Write out the code assigned to the prefix
            BinaryStdOut.write(tst.lookup(prefix), 12);
            // If possible look ahead onto the next char
            if(index != s.length() - 1 && customCode < 4096){
                // Creates new prefix with new code by adding next char to prefix
                tst.insert(prefix + s.charAt(index + 1), customCode);
                customCode++;
            }
            index += prefix.length();
        }
        // Add EOF as 256 to signal the end for expand function
        BinaryStdOut.write(EOFCode, 12);
        BinaryStdOut.close();
    }

    private static void expand() {
        int EOFCode = 256;
        // Max possible codes is 4096 because we have 12 bit codes so 2^12 codes
        int maxPossibleCodes = 4096;
        String[] codeToString = new String[maxPossibleCodes];
        // Add all 256 extended ascii characters to the map
        String symbol = "";
        for(int i = 0; i < 256; i++){
            symbol += (char) i;
            codeToString[i] = symbol;
            symbol = "";
        }
        int customCode = 257;
        int lookAheadCode = BinaryStdIn.readInt(12);
        int currentCode = 0;
        while(lookAheadCode != EOFCode){
            currentCode = lookAheadCode;
            BinaryStdOut.write(codeToString[currentCode]);
            lookAheadCode = BinaryStdIn.readInt(12);
            // Edge case where we don't know what the next code is
            if(codeToString[lookAheadCode] == null){
                // Mystery lookahead String is actually the current prefix + the first char prefix
                codeToString[lookAheadCode] = codeToString[currentCode] + codeToString[currentCode].charAt(0);
            }
            else{
                // Creates lookahead string and adds it to map
                if(customCode < 4096){
                    codeToString[customCode] = codeToString[currentCode] + codeToString[lookAheadCode];
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
