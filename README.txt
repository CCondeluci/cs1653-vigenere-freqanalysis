/************************************
 * CS 1653 HW2					    *
 * University of Pittsburgh         *
 * Professor Bill Garrison          *
 * 4/10/2016	                    *
 * By:                              *
 *   	Carmen Condeluci            *
 ************************************/

COMPILATION:
----------------------

javac *.java


EXECUTION:
----------------------

For "KasiskiExamination", you simply run the program with your input file as 
your argument, like so:

Example: java KasiskiExamination hw2-vigenere.txt

For "VigenereCipher", you run the program with your input file, followed by 
your key, then followed by "E" or "D" for encryption or decryption, 
respectively.

Example: java VigenereCipher hw2-vigenere.txt bellaso D


APPROACH:
----------------------

The first step in decrypting the provided "hw2-vigenere.txt" Vigenere ciphertext 
was to strip the input of all non-alphabetic characters, such as punctuation and 
numbers. I then performed Kasiski Examination with repeated groups of three 
letters in length or more, calculating the factors of each distance between the 
repeated groups as they were found. Out of all the results, 7 proved to be the 
most probable key length, with there being 1146 repeated groups that held 7 as a 
common factor.

From this point, I grouped the ciphertext into 7 separate messages, as now each 
separate message was effectively encrypted using only a 1-character key. This 
allowed me to perform a very basic form of frequency analysis on each separated 
message, using the knowledge of "e" as the most common letter present in English 
language text. I calculated the frequency of each character present within each 
separate message, then took the three most frequently occurring characters and 
"decrypted" them via the Vigenere cipher as if the plaintext solution for that 
character was "e". This resulted in the three most probable keys for this 
ciphertext, "bellasd", "lauppwo", and "fiaaakhs", in decreasing order of 
probability.

At this point, I created a quick application to perform Vigenere encryption and 
decryption to observe the result of decrypting the "hw2-vigenere.txt" ciphertext 
with the three most probable keys that I had generated. Decrypting with the 
first key, "bellasd", resulted in nearly readable text with easily recognizable 
errors due to the last character of the key, "d". The first word of the 
plaintext, "congratulations", was easily recognizable despite having decrypting 
to "congraeulatioys" under the key "bellasd". At this point, it was trivial to 
substitute in the next most probable character for the seventh position in the 
key, "o", as per the frequency analysis that I performed earlier. This resulted 
in the key "bellaso" fully and correctly decrypting the ciphertext.