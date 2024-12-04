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
 *  @author Zach Blick, YOUR NAME HERE
 */
public class TextCompressor {

    private static void compress() {

        // TODO: Complete the compress() method
        // Representing letters with a 6 bit code
        int[] codes4Letters= new int['z' + 1];
        int code = 0b000000;
        // For loop which gives each letter a unique code
        for(int letter = 'A'; letter < codes4Letters.length; letter++){
            codes4Letters[letter] = code;
            code += 0b000001;
        }
        // Code for space = Escape character
        code += 0b000001;
        codes4Letters[' '] = code;
        // Apostrophe code
        code += 0b000001;
        codes4Letters[39] = code;
        // Representing the 15 most common words with a 10 bit code
        HashMap<String, Integer> codes4Words = new HashMap<String, Integer>();
        // Starting from 63 so that it doesn't have the same value as the 6 bit codes
        int wordCode = 0b0000111111;
        codes4Words.put("the", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("be", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("to", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("of", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("and", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("in", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("that", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("have", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("it", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("for", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("not", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("with", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("you", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("he", wordCode);
        wordCode += 0b0000000001;
        codes4Words.put("as", wordCode);
        // Going over the file word by word and then letter
        // test
        // Test
        while()
        BinaryStdOut.close();
    }

    private static void expand() {

        // TODO: Complete the expand() method

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
