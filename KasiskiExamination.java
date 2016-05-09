/************************************
 * CS 1653 HW2					    *
 * University of Pittsburgh         *
 * Professor Bill Garrison          *
 * 4/10/2016	                    *
 * By:                              *
 *   	Carmen Condeluci            *
 ************************************/

/*
	This program utilizes Kasiski Examination with frequency analysis in order
	to attempt to determine the most probable keys for a given Vigenere cipher 
	input.

	All data is printed to the user in order for easier review of possible key 
	lengths and probable keys.
*/

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

@SuppressWarnings("unchecked")
public class KasiskiExamination {

	String alphabet = "abcdefghijklmnopqrstuvwxyz";
	String input;
	ArrayList<Integer> commonFactors;

	public static void main(String args[]) throws Exception {

		String content = new Scanner(new File(args[0])).useDelimiter("\\Z").next();
		String removed = content.replaceAll("[\\p{Punct}]", "").replaceAll("\\s+", "");
		removed = removed.replaceAll("[^A-Za-z]", "");

		KasiskiExamination examine = new KasiskiExamination(removed);
		int probable = examine.examineInput();
		
		char[][] groupedInput = new char[probable][(examine.input.length()/probable) + 1];
		int j = 0;
		int k = 0;
		for(char c : examine.input.toCharArray()){
			//System.out.print(c + " ");
			groupedInput[j][k] = c;
			j++;
			if(j == probable){
				j = 0;
				k++;
				//System.out.print("\n");
			}
		}

		System.out.println("\n");

		String probableKey = "";
		char[][] groupedKey = new char[3][probable];

		for(int i = 0; i < groupedInput.length; i++){
			String chToString = new String(groupedInput[i]);
			char[] probChars = examine.getTopFrequencies(chToString, 3);
			System.out.print("Probable characters for group " + i + " are ");
			for(int n = 0; n < probChars.length; n++){
				char c = probChars[n];
				groupedKey[n][i] = c;
				System.out.print(c + ", ");
			}

			System.out.print("\n\n");
		}

		System.out.println("Probable Keys (in order of probability): ");

		for( int i = 0; i < groupedKey.length; i++ ){
				probableKey = "";
				for( j = 0; j < groupedKey[i].length; j++ ){
					int keyVal = examine.alphabet.indexOf(groupedKey[i][j]) - 4;
					if(keyVal < 0){
						keyVal += 26;
					}
					probableKey += examine.alphabet.charAt(keyVal);
				}

				System.out.println(probableKey);
		}

	}


	public KasiskiExamination(String input){

		this.input = input;
		commonFactors = new ArrayList<Integer>();
	}

	//Find all factors for a given number.
	public int[] findFactors(int number){

		int[] factors = new int[100]; //More than enough for our purposes
		int index = 0;

		for( int i = 2; i <= number; i++ ){
			if( number % i == 0){
				factors[index] = i;
				commonFactors.add(i);
				index++;
			}
		}
		
		return factors;
	}

	/*
	* Perform Kasiski Examination on the input with group size >= 3.
	* Will perform frequency analysis for what is beleived to be the 
	* most probable key length as well as print data out to the user
	* for review.
	*/
	public int examineInput(){

		for( int start = 0; start < input.length()-3; start++ ){
			for( int test = start+3; test < input.length()-3; test++){
				
				int length = 1;
				String startGroup = input.substring(start, start+length);
				String testGroup = input.substring(test, test+length);

				while( 	startGroup.equals(testGroup) && 
						length < (test - start) && 
						length < (input.length() - test)	){

					length++;
					startGroup = input.substring(start, start+length);
					testGroup = input.substring(test, test+length);
				}	
				length -= 1;

				if( length >= 3 ){
					int matchDistance = test - start;
					int[] factors = findFactors(matchDistance);

					System.out.print(	input.substring(start,start+length)	
										+ "\t" + start + "\t" + test + "\t" 
										+ matchDistance + "\t"				);
					
					for(int i = 0; i < factors.length && factors[i] != 0; i++)
						System.out.print(factors[i] + ", ");
					
					System.out.print("\n");
				}
			}
		}

		System.out.println("-------------------------------------------------");
		System.out.println("\nFrequencies:\n");

		Collections.sort(commonFactors);
		Set<Integer> factorSet = new LinkedHashSet<Integer>(commonFactors);

		int maxFreq = 0;
		int maxFactor = 0;
		for(int i : factorSet){
			int tempFreq = Collections.frequency(commonFactors, i);
			System.out.println(i + "\t" + tempFreq);
			if(tempFreq > maxFreq){
				maxFreq = tempFreq;
				maxFactor = i;
			}
		}

		System.out.println("\nMost probable key length is: " + maxFactor + " with " + maxFreq + " repeated groups.");
		System.out.println("-------------------------------------------------");

		return maxFactor;
	}

	//Returns number of top frequencies for each group
	public char[] getTopFrequencies(String s, int numTopFreq) {

	    HashMap<Character,Integer> map = new HashMap<Character,Integer>();          
		for(int i = 0; i < s.length(); i++){
		   char c = s.charAt(i);
		   Integer val = map.get(new Character(c));
		   if(val != null){
		     map.put(c, new Integer(val + 1));
		   }else{
		     map.put(c,1);
		   }
		}

		map = sortHashMapByValuesD(map);
		char[] probableChars = new char[numTopFreq];
		Iterator it = map.entrySet().iterator();
		
		for(int i = 0; i < probableChars.length; i++){
			if(it.hasNext()){
				Map.Entry pair = (Map.Entry)it.next();
				probableChars[i] = (char)pair.getKey();
			}
			
		}

	    return probableChars;
	}	

	//"Sorts" hashmap for easier location of top group frequencies
	public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues, Collections.reverseOrder());
		Collections.sort(mapKeys, Collections.reverseOrder());

		LinkedHashMap sortedMap = new LinkedHashMap();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
		   Object val = valueIt.next();
		   Iterator keyIt = mapKeys.iterator();

		   while (keyIt.hasNext()) {
		       Object key = keyIt.next();
		       String comp1 = passedMap.get(key).toString();
		       String comp2 = val.toString();

		       if (comp1.equals(comp2)){
		           passedMap.remove(key);
		           mapKeys.remove(key);
		           sortedMap.put((Character)key, (Integer)val);
		           break;
		       }

		   }

		}

		return sortedMap;
	}
}