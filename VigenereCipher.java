/************************************
 * CS 1653 HW2					    *
 * University of Pittsburgh         *
 * Professor Bill Garrison          *
 * 4/10/2016	                    *
 * By:                              *
 *   	Carmen Condeluci            *
 ************************************/

/*
	This program encrypts and decrypts text inputs via file with a 
	Vigenere Cipher.
*/

import java.io.File;
import java.util.Scanner;

public class VigenereCipher {

	String alphabet = "abcdefghijklmnopqrstuvwxyz";

	public static void main(String args[]) throws Exception {
		String input = new Scanner(new File(args[0])).useDelimiter("\\Z").next(); //Input file
		String key = args[1]; //Key
		String mode = args[2]; //'E' or 'D'

		VigenereCipher vCipher = new VigenereCipher();

		String output = vCipher.translateInput(input, key, mode);

		System.out.println("\n" + output);
	}

	public String translateInput(String input, String key, String mode){
		String output = "";
		int keyIndex = 0;
		key = key.toLowerCase();

		for( int i = 0; i < input.length(); i++ ){
			char c = input.charAt(i);
			int letterValue = alphabet.indexOf(c);
			if(letterValue != -1){
				if(mode.equals("E"))
					letterValue += alphabet.indexOf(key.charAt(keyIndex));
				else if(mode.equals("D"))
					letterValue -= alphabet.indexOf(key.charAt(keyIndex));

				letterValue %= 26;
				if(letterValue < 0)
					letterValue += 26;

				if(Character.isUpperCase(c))
					output += ("" + alphabet.charAt(letterValue)).toUpperCase();
				else
					output += alphabet.charAt(letterValue);

				keyIndex += 1;
				if( keyIndex == key.length())
					keyIndex = 0;
			}
			else {
				output += c;
			}
		}

		return output;
	}

}