/* MIT License

Copyright (c) 2018 Andrea Ligios

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.andrealigios.legendaryutils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.andrealigios.legendaryutils.WordsCapitalizer.Behavior;
import com.andrealigios.legendaryutils.WordsCapitalizer.Delimiter;
/**
 * Unit test for WordsCapitalizer.
 */
class WordsCapitalizerTest {

    @Test
    void givenTestString_whenCapitalizedFully_ThenCorrectOutput() {
		String testString = "cApItAlIzE this string after WHITE SPACES";
		String expected   = "Capitalize This String After White Spaces";
		
        assertEquals(WordsCapitalizer.capitalizeEveryWord(testString), expected);
    }

    @Test
    void givenSingleCustomDelimiter_whenCapitalizedFully_ThenCorrectOutput() {
		String testString = "capitalize this string ONLY before'and''after'''APEX";
		String expected   = "Capitalize this string only beforE'AnD''AfteR'''Apex";
		List<Delimiter> delimiters = new ArrayList<Delimiter>();
		delimiters.add(new Delimiter(Behavior.CAPITALIZE_BEFORE_AND_AFTER_MARKER, '\''));
		
		assertEquals(WordsCapitalizer.capitalizeEveryWord(testString,delimiters,null), expected);
    }

    @Test
    void givenMultipleCustomDelimiters_whenCapitalizedFully_ThenCorrectOutput() {
		String testString = "capitalize this string AFTER SPACES, BEFORE'APEX, and #AFTER AND BEFORE# NUMBER SIGN (#)";
		String expected   = "Capitalize This String After Spaces, BeforE'apex, And #After And BeforE# Number Sign (#)";		
		List<Delimiter> delimiters = new ArrayList<Delimiter>();
		delimiters.add(new Delimiter(Behavior.CAPITALIZE_AFTER_MARKER, ' '));
		delimiters.add(new Delimiter(Behavior.CAPITALIZE_BEFORE_MARKER, '\''));
		delimiters.add(new Delimiter(Behavior.CAPITALIZE_BEFORE_AND_AFTER_MARKER, '#'));

        assertEquals(WordsCapitalizer.capitalizeEveryWord(testString,delimiters,null), expected);
    }

    @Test
    void givenRootLocale_whenLoweringTurkishI_ThenCorrectOutputAccordingToTheLocale() {
		String testString								= "Lowering the different kind of 'i' of the Turkish word D[\u0130]YARBAK[\u0049]R (D\u0130YARBAK\u0049R)";
		String expectedWhenLoweringWithRootLocale    	= "Lowering The Different Kind Of 'i' Of The Turkish Word D[i\u0307]yarbak[i]r (di\u0307yarbakir)";
		String expectedWhenLoweringWithTurkishLocale  	= "Lowering The Different Kind Of 'i' Of The Turkish Word D[i]yarbak[\u0131]r (diyarbak\u0131r)";

		assertEquals(WordsCapitalizer.capitalizeEveryWord(testString,Locale.ROOT), expectedWhenLoweringWithRootLocale);
		assertEquals(WordsCapitalizer.capitalizeEveryWord(testString,Locale.forLanguageTag("tr-TR")), expectedWhenLoweringWithTurkishLocale);
    }

    
    @Test
    void givenSurrogatePair_whenCapitalizedFully_ThenCorrectOutput() {
		String testString = "ab 𐐂c de à";
		String expected   = "Ab 𐐪c De À";
		
        assertEquals(WordsCapitalizer.capitalizeEveryWord(testString), expected);
    }
    
    @Test
    void EnumValuesIntegrityCheck() {
    	assertEquals(Behavior.CAPITALIZE_AFTER_MARKER.getValue(),0);
    	assertEquals(Behavior.CAPITALIZE_BEFORE_MARKER.getValue(),1);
    	assertEquals(Behavior.CAPITALIZE_BEFORE_AND_AFTER_MARKER.getValue(),2);
    }
    

    
}