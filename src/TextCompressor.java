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
            // Check if it not in the trie
            if(tst.lookup(prefix) == -1){
                // Check if it is single letter
                if(prefix.length() == 1){
                    char letter = prefix.charAt(0);
                    tst.insert(prefix, letter);
                }
                // Don't need this
                else{
                    tst.insert(prefix, customCode);
                     customCode++;
                }
            }
            else{
                // Write out the code associated with prefix as 12 bits
                BinaryStdOut.write(tst.lookup(prefix), 12);
            }
            // If possible look ahead onto the next character

        }



















        /*
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
        // Convert all chars into a single string
        String s = BinaryStdIn.readString();
        String word = "";
        for(int i = 0; i < s.length(); i++){
            // Continue adding letters until we reach the end
            if(i != ' '){
                word += i;
            }
            else{
                // Check if in HashMap, if so, write the 10 bit code
                if(codes4Words.containsKey(word)){
                    // Write escape key to let the expand method know when to read 10 bits
                    BinaryStdOut.write(codes4Letters[' '], 6);
                    BinaryStdOut.write(codes4Words.get(word), 10);
                    // Reset word
                    word = "";
                }
                else{
                    // Convert letters to 6 bit codes
                    for(int j = 0; j < word.length(); j++){
                        BinaryStdOut.write(codes4Letters[word.charAt(i)], 6);
                    }
                    // Reset word
                    word = "";
                }
            }
        }
        BinaryStdOut.close();

         */
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
