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
        // TODO: Complete the compress() method
        // Read Data
        String s = BinaryStdIn.readString();
        int index = 0;
        String prefix = "";
        int customCode = 81;
        // TST to keep all of our prefixes/strings
        TST tst = new TST();
        while (index < s.length()){
            // Get char
            prefix += s.charAt(index);
            int counter = 1;
            // Continue adding chars to the prefix if possible
            while(tst.lookup(prefix) != - 1 && index + counter < s.length()){
                prefix += s.charAt(index + counter);
                counter++;
            }
            // Look for the prefix code
            // Check if it not in the trie
            if(prefix.length() == 1){
                char letter = prefix.charAt(0);
                tst.insert(prefix, letter);
                BinaryStdOut.write(letter, 12);
            }
            else{
                // Write out the code associated with prefix as 12 bits
                BinaryStdOut.write(tst.lookup(prefix), 12);
            }
            /*
            if(tst.lookup(prefix) == -1){
                // Check if it is single letter
            }

             */
            // If possible look ahead onto the next character
            if(index != s.length() - 2){
                prefix += s.charAt(index + 1);
                // Insert new prefix
                tst.insert(prefix, customCode);
                customCode++;
            }
            index += prefix.length();
        }
        // Add EOF as 80 to signal the end for expand function
        BinaryStdOut.write(80, 12);
        BinaryStdOut.close();
    }

    private static void expand() {
        // Array where the code is the index and the value is the letter
        char[] codeToLetter = new char[52];
        int letter = 'A';
        int i = 0;
        while(i < codeToLetter.length){
            codeToLetter[i] = (char) letter;
            letter += 1;
        }
        codeToLetter[letter] = ' ';

        // TODO: Complete the expand() method

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
